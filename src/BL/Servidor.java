package BL;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import DL.*;
import Exceptions.E404Exception;
import GUI.View;
import UI.UI;

public class Servidor {
    private Map<String, Gestor> listaGestores;
    private Map<String, Robot> robots_r; // par
    private Map<String, Robot> robots_e; // impar
    private Inventario inventario;
    private HashMap<String,Palete> temp;
    private GestorPedidos gestor_Pedidos;
    private Integer parking;
    private Integer[][] mapa; // -1 nao pode, 0 onde pode, 1 onde esta, prateleiras vazias 2, prateleiras cheias 3, 4-10 quantidade de stuff la
    private int tamanho_lateral;
    private int tamanho_altura;
    private ExecutorService threadpool;
    private ArrayList<Future<Robot>> robosEmProgresso;
    private final int N_MAX = 10;
    private static final int MAX_E = 80;

    public Servidor(){
        this.listaGestores = new TreeMap<>();
        this.robots_r = new TreeMap<>();
        this.robots_e = new TreeMap<>();
        this.inventario = new Inventario();
        this.temp = new HashMap<>();
        this.gestor_Pedidos = new GestorPedidos();
        this.parking = 2;
        this.robosEmProgresso = new ArrayList<>();
        this.threadpool = Executors.newCachedThreadPool();

        tamanho_lateral = 8;
        tamanho_altura = 6;
        this.mapa = new Integer[tamanho_altura][tamanho_lateral];
        for(int i = 0; i < tamanho_altura; i++)
            for(int j = 0; j < tamanho_lateral; j++){
                if (j == 0) mapa[i][j] = -1;
                else if (j == 7) mapa[i][j] = -1;
                else if (j == 1 && (i == 0 || i == 5)) mapa[i][j] = -1;
                else if ((i == 2 || i == 3) && j >= 2 && j < 6) mapa[i][j] = -1;
                else mapa[i][j] = 0;
            }

        mapa[0][1] = 0;
        mapa[2][7] = 0;
        for(int a = 2; a <=6;a++ ) {
            mapa[0][a] = 2;
            mapa[5][a] = 2;
        }

        for (Gestor g : GestorDAO.getInstance().values()) {
            listaGestores.put(g.getCodeID(), g);
        }

        for (Robot r : RobotsDAO.getInstance().values()) {
            if( Integer.parseInt(r.getCodeID().substring(1)) % 2 != 0 ){
                robots_e.put(r.getCodeID(), r);
            }

            if( Integer.parseInt(r.getCodeID().substring(1)) % 2 == 0 ){
                robots_r.put(r.getCodeID(), r);
            }
        }

        for (Palete p : InventarioDAO.getInstance().values()) {
            inventario.add(p);
            addToMap(p);
        }

        for (Entrega e : EntregaDAO.getInstance().values()) {
            gestor_Pedidos.addEF(e);
        }

        for (Requisicao r : RequisicaoDAO.getInstance().values()) {
            gestor_Pedidos.addRF(r);
        }

    }

    private void addToMap(Palete p) {
        if(p.isArmazenado()){
            mapa[p.getLocalizacao().x][p.getLocalizacao().y]++;
        }
    }
    private Robot getAvailable(Map<String, Robot> robots){
        boolean b = true;
        while (b){
            for (Robot r1 : robots.values()) {
                if (!r1.getAtivo()) {
                    r1.setAtivo(true);
                    return r1;
                }
            }
        }

        return new Robot();
    }
    private Point getEspacoLivre(){
        int i = 2;
        Point pointReturn = new Point();

        for(; i < 7; i++){
            if (mapa[0][i] < N_MAX && mapa[0][i] >= 2) {
                if(mapa[0][i]+1 > N_MAX)
                    return where(0,i);
                mapa[0][i]++;
                pointReturn.setLocation(0,i);
                return pointReturn;
            }
        }

        for(i = 2; i < 7; i++){
            if (mapa[5][i] < N_MAX && mapa[5][i] >= 2) {
                if(mapa[5][i]+1 > N_MAX)
                    return where(5,i);
                mapa[5][i]++;
                pointReturn.setLocation(5,i);
                return pointReturn;
            }
        }

        return pointReturn;
    }
    private Point where(int x, int y){
        if(mapa[x][y]+1 > N_MAX){
            return where(x,y+1);
        }
        else if(x == 0 && y == 6){
            return where(5,2);
        }
        else{
            Point p = new Point();
            p.setLocation(x,y);
            return p;
        }
    }

    public List<String> getEntAtivas(){
         ArrayList<Entrega> el = gestor_Pedidos.listaEntrega_ATIVAS();
         List<String> s = new ArrayList<>();
         for (Entrega e : el)
            s.add(gestor_Pedidos.toStringEA(e));

         return s.stream().sorted().collect(Collectors.toList());
    }
    public List<String> getEntFeitas(){
        ArrayList<Entrega> el = gestor_Pedidos.listaEntrega_FEITAS();
        List<String> s = new ArrayList<>();
        for (Entrega e : el)
            s.add(gestor_Pedidos.toStringEF(e));

        return s.stream().sorted().collect(Collectors.toList());
    }
    public List<String> getReqFeitas(){
        ArrayList<Requisicao> rl = gestor_Pedidos.listaRequisicoes_FEITAS();
        List<String> s = new ArrayList<>();
        for (Requisicao r : rl)
            s.add(gestor_Pedidos.toStringRF(r));

        return s.stream().sorted().collect(Collectors.toList());
    }
    public List<String> getReqAtivas(){
        ArrayList<Requisicao> rl = gestor_Pedidos.listaRequisicoes_ATIVAS();
        List<String> s = new ArrayList<>();
        for (Requisicao r : rl)
            s.add(gestor_Pedidos.toStringRA(r));

        return s.stream().sorted().collect(Collectors.toList());
    }
    public List<String> RobotsDisponiveis() {
        ArrayList<String> disponiveis = new ArrayList<>();
        for(Robot r : robots_r.values()){
            if(!r.getAtivo())
                disponiveis.add(r.toString());
        }
        for(Robot r : robots_e.values()){
            if(!r.getAtivo())
                disponiveis.add(r.toString());
        }
        return disponiveis.stream().sorted().collect(Collectors.toList());
    }
    public List<String> lista_robots() {
        ArrayList<String> disponiveis = new ArrayList<>();
        for(Robot r : robots_r.values()){
            disponiveis.add(r.toString());
        }
        for(Robot r : robots_e.values()){
            disponiveis.add(r.toString());
        }
        return disponiveis.stream().sorted().collect(Collectors.toList());
    }

    public void removeGestor (String codID){
        listaGestores.remove(codID);
        GestorDAO.getInstance().remove(codID);
    }
    public void addGestor (String codID, String nome, String pwd){
        Gestor g = new Gestor(nome, pwd, codID, false);
        listaGestores.put(codID, g);
        GestorDAO.getInstance().put(g);
    }

    public void removeEntrega(String e){
        this.gestor_Pedidos.removeEntrega(e);
    }
    public void removeRequisicao(String r){
        this.gestor_Pedidos.removeRequisicao(r);
    }

    public void addEntrega(Entrega e){
        this.gestor_Pedidos.addEntrega(e);
    }
    public void addRequisicao(Requisicao r){
        this.gestor_Pedidos.addRequisicao(r);
    }

    public boolean isParkingAvailable(){
        return parking != 0;
    }
    public void minusSpot(){
        parking--;
    }
    public void plusSpot(){
        parking++;
    }
    public Integer getParking() {
        return parking;
    }

    public void offline(String codID) {
        listaGestores.get(codID).setOnline(false);
    }
    public void online(String codID) {
        listaGestores.get(codID).setOnline(true);
    }

    public Palete criaPalete (String c){
        int t = 1;
        String s = "p1";
        while(inventario.contains(s) || temp.containsKey(s)){
            s = "p" + t;
            t++;
        }
        Palete p = new Palete(s,c);
        temp.putIfAbsent(p.getCodID(),p);
        return p;
    }

    public List<String> inventario (){
        List<String> s = new ArrayList<>();
        for(Palete p : inventario.values()){
            s.add(p.toStringID_C());
        }
        return s.stream().sorted().collect(Collectors.toList());
    }
    public List<String> listagem (){
        List<String> s = new ArrayList<>();
        for(Palete p : inventario.values()){
            s.add(p.toStringListagem());
        }
        return s.stream().sorted().collect(Collectors.toList());
    }
    public List<String> listar_entregas() {
        ArrayList<Entrega> rl = gestor_Pedidos.listaEntregas();
        List<String> s = new ArrayList<>();
        for (Entrega r : rl){
            s.add(gestor_Pedidos.toStringEA(r));
        }
        return s.stream().sorted().collect(Collectors.toList());
    }
    public List<String> listar_requisicoes() {
        ArrayList<Requisicao> rl = gestor_Pedidos.listaRequisicoes();
        List<String> s = new ArrayList<>();
        for (Requisicao r : rl){
            s.add(gestor_Pedidos.toStringRA(r));
        }
        return s.stream().sorted().collect(Collectors.toList());
    }

    public Palete search(String s){
        return inventario.search(s);
    }

    public boolean searchEA(String codID){
        return gestor_Pedidos.searchEA(codID);
    }
    public boolean searchEF(String codID){
        return gestor_Pedidos.searchEF(codID);
    }
    public boolean searchRA(String s){
        return gestor_Pedidos.searchRA(s);
    }
    public boolean searchRF(String s){
        return gestor_Pedidos.searchRF(s);
    }

    public boolean containsGestor(String codID){
        return listaGestores.containsKey(codID);
    }
    public String getGP(String s){
        return listaGestores.get(s).getPassword();
    }
    public boolean getGO(String s){
        return listaGestores.get(s).getOnline();
    }
    public Gestor getGestor(String c){
        return listaGestores.get(c);
    }

    public Requisicao getRA(String s) {
        return gestor_Pedidos.getRA(s);
    }
    public Entrega getEA(String s) {
        return gestor_Pedidos.getEA(s);
    }

    public void print_map(){
        UI.notifica("Mapa atual do armazem: ");
        UI.print_mapa(mapa, 6, 8);
    }
    private List<Palete> getNPaletes(int n) {
        return inventario.listar(n);
    }

    private List<Entrega> automatico_e(int n){

        List<Entrega> eL = new ArrayList<>();
        List<String> lista = new ArrayList<>(Automatico.create(n));
        int t = 1;

        String s1 = "E1";

        while (searchEA(s1) || searchEF(s1)) {
            t++;
            s1 = "E" + t;
        }

        for(int i = 0; i < n; i++) {
            Palete p = criaPalete(lista.get(i));
            eL.add(new Entrega(p, s1));
            t++;
            s1 = "E" + t;
        }
        return eL;
    }
    private List<Requisicao> automatico_r(int n) throws IndexOutOfBoundsException  {
        List<Requisicao> rL = new ArrayList<>();
        List<Palete> lista = getNPaletes(n);

        int t = 1;
        String s1 = "R1";
        while(searchRA(s1) || searchRF(s1)){
            t++;
            s1 = "R" + t;
        }

        for(int i = 0; i < n; i++){
            rL.add(new Requisicao(lista.get(i),s1));
            t++;
            s1 = "R" + t;
        }

        return rL;
    }

    public void giveWork(Entrega e){
        Robot wallie = getAvailable(robots_e);
        Palete p = e.conteudo;

        UI.notifica("O Robot: " + wallie.getCodeID()
                + " vai agora iniciar a recolha da Palete " + p.getCodID() + " no ponto de Recolha (E).");

        entregaPalete(p, wallie); // vai de (0,1) a (x,y)
        InventarioDAO.getInstance().put(p);
        inventario.add(p);
        gestor_Pedidos.removeEA(e.codeID);
        RobotsDAO.getInstance().put(wallie);
    }
    public void giveWork(Requisicao r){
        Robot wallie = getAvailable(robots_r);
        Palete p = r.conteudo;

        UI.notifica("O Robot: " + wallie.getCodeID() + " vai agora iniciar a recolha da Palete "
                + p.getCodID() + " na localização (" + p.getLocalizacao().x + ", " + p.getLocalizacao().y + ") (R).");

        requisicaoPalete (p, wallie); // vai de (x,y) a (7,2)
        InventarioDAO.getInstance().remove(p.getCodID());
        inventario.remove(p.getCodID());
        gestor_Pedidos.removeRA(r.codeID);
        RobotsDAO.getInstance().put(wallie);
    }

    private Robot run_r(Palete p, Robot wallie){
        UI.notifica("O Robot: " + wallie.getCodeID() + " vai agora iniciar a recolha da Palete "
                + p.getCodID() + " na localização (" + p.getLocalizacao().x + ", " + p.getLocalizacao().y + ") (R).");

        requisicaoPalete (p, wallie); // vai de (x,y) a (7,2)
        InventarioDAO.getInstance().remove(p.getCodID());
        inventario.remove(p.getCodID());

        return wallie;
    }
    private Robot run_e(Palete p, Robot wallie){
        UI.notifica("O Robot: " + wallie.getCodeID()
                + " vai agora iniciar a recolha da Palete " + p.getCodID() + " no ponto de Recolha (E).");

        entregaPalete(p, wallie); // vai de (0,1) a (x,y)
        InventarioDAO.getInstance().put(p);
        inventario.add(p);

        return wallie;
    }

    public void run_both(int e, int r) throws E404Exception {
        if(inventario.size() == MAX_E || inventario.size()+e > MAX_E){
            throw new E404Exception("ERRO", "Não há espaço no Inventário, é impossivel Entregar Paletes de momento.");
        }

        if(inventario.size() == 0 && r > 0){
            throw new E404Exception("ERRO", "Não há nenhuma palete no Inventário, é impossivel criar Requisições de momento.");
        }

        List<Entrega> lista_e = automatico_e(e);
        List<Requisicao> lista_r = automatico_r(r);

        do {

            robosEmProgresso.removeIf(Future::isDone);
            if (RobotsDisponiveis() != null) {
                if (e != 0 && mapa[1][1] == 0) {
                    Entrega ne = lista_e.get(0);
                    Future<Robot> futureTask = threadpool.submit(() -> run_e(lista_e.get(0).conteudo, getAvailable(robots_e)));
                    EntregaDAO.getInstance().put(ne);
                    lista_e.remove(0);
                    robosEmProgresso.add(futureTask);
                    e--;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    View.alert("ERRO", "Thread failed to zzz.");
                }
                if (r != 0 && mapa[1][1] == 0) {
                    Requisicao nr = lista_r.get(0);
                    Future<Robot> futureTask = threadpool.submit(() -> run_r(lista_r.get(0).conteudo, getAvailable(robots_r)));
                    RequisicaoDAO.getInstance().put(nr);
                    lista_r.remove(0);
                    robosEmProgresso.add(futureTask);
                    r--;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    View.alert("ERRO", "Thread failed to zzz.");
                }
            }

        } while (r != 0 || e != 0 || robosEmProgresso.size() != 0);
        UI.notifica("Todas as tarefas foram realizadas.");
        update_all();
    }

    private void update_all() {
        for(Robot wallie : robots_r.values()){
            RobotsDAO.getInstance().put(wallie);
        }
        for(Robot wallie : robots_e.values()){
            RobotsDAO.getInstance().put(wallie);
        }
    }

    private void recolherRobo(Robot robot){
        boolean voltou = false;
        while (!voltou) voltou = robot.takeBreak(mapa);
    }
    private Robot entregaPalete(Palete p, Robot robot) {

        Point destino = getEspacoLivre();

        boolean iniciado = false;
        boolean entregou = false;

        while (!iniciado) iniciado = robot.startWork(this.mapa);
        while (!entregou) entregou = robot.andaParaPalete(mapa, destino.x, destino.y);

        UI.notifica("   -> O Robot: " + robot.getCodeID() + " acabou de movimentar a Palete " + p.getCodID() + " e vai cessar atividade.");

        p.setArmazenado(true);
        p.setLocalizacao(destino);
        recolherRobo(robot);

        return robot;
    }
    private Robot requisicaoPalete (Palete p, Robot r){
        Point destino = p.getLocalizacao();
        boolean temPalete = false;
        boolean iniciado = false;
        boolean entregouPalete = false;

        while (!iniciado) iniciado = r.startWork(this.mapa);
        while(!temPalete) temPalete = r.andaParaPalete(mapa, destino.x, destino.y);

        if(mapa[destino.x][destino.y] > 2)mapa[destino.x][destino.y]--;
        inventario.remove(p.getCodID());

        while(!entregouPalete) entregouPalete = r.entregaPalete(mapa);
        UI.notifica("   -> O Robot: " + r.getCodeID() + " acabou de movimentar a Palete " + p.getCodID() + " e vai cessar atividade.");
        recolherRobo(r);

        return r;
    }
}
