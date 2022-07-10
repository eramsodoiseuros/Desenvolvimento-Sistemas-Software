package PL;

import BL.*;

import Exceptions.E404Exception;
import GUI.View;
import UI.UI;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class ControladorSessoes implements Controlador{

    private Servidor servidor;
    public ControladorSessoes(){
        servidor = new Servidor();
    }

    public void logOutGestor(String codID){
        if(servidor.getGO(codID))
            servidor.offline(codID);
    }
    public boolean iniciaSessao(String codID, String password){
        boolean b = false;
        if(servidor.containsGestor(codID))
            if (b = !(servidor.getGP(codID).equals(password) && servidor.getGO(codID)))
                servidor.online(codID);
        return b;
    }

    public void add(String c, String n, String p) {
        servidor.addGestor(c,n,p);
    }
    public void delete(String c) {
        if(servidor.containsGestor(c)) {
            Gestor g = servidor.getGestor(c);
            View.alert("Aviso", "Gestor " + g.getNome() + " foi permanentemente apagado do sistema." );
            servidor.removeGestor(c);
        } else View.alert("ERRO", "Não existe nenhum Gestor com o código " + c + ".");
    }

    public  void addE (String s){
        Palete p = servidor.criaPalete(s);
        if(servidor.isParkingAvailable()){
            int t = 1;
            String s1 = "E1";
            while(servidor.searchEA(s1) || servidor.searchEF(s1)){
                s1 = "E" + t;
                t++;
            }
            Entrega e = new Entrega(p,s1);
            servidor.addEntrega(e);
        }
    }

    public void addR(String s){
        Palete p = servidor.search(s);
        if(p != null && servidor.isParkingAvailable()){
            int t = 1;
            String s1 = "R1";
            while(servidor.searchRA(s1) || servidor.searchRF(s1)){
                s1 = "R" + t;
                t++;
            }
            Requisicao r = new Requisicao(p,s1);
            servidor.addRequisicao(r);
        }
        else View.alert("ERRO", "Tentou requisitar algo não existente no armazem.");
    }

    public void addRA(String s) {
        servidor.removeRequisicao(s);
        aceitou(servidor.getRA(s));
    }

    public void addEA(String s) {
        servidor.removeEntrega(s);
        aceitou(servidor.getEA(s));
    }

    public List<String> lista_robots() {
        return servidor.lista_robots();
    }
    public List<String> inventario (){
    return servidor.inventario();
    }
    public List<String> listagem (){
        return servidor.listagem();
    }
    public List<String> robots() {
        return servidor.RobotsDisponiveis();
    }
    public List<String> lista_requisicoes() {
        return servidor.listar_requisicoes();
    }
    public List<String> lista_entregas() {
        return servidor.listar_entregas();
    }
    public void map(){servidor.print_map();}

    public int parking() {
        return servidor.getParking();
    }
    public void aceitou(Requisicao r) {
        servidor.minusSpot();
        servidor.giveWork(r);
        servidor.plusSpot();
    }
    public void aceitou(Entrega e) {
        servidor.minusSpot();
        servidor.giveWork(e);
        servidor.plusSpot();
    }

    public void both(int e, int r){
        try {
            servidor.run_both(e,r);
        } catch (E404Exception | IndexOutOfBoundsException e404Exception) {
            UI.notifica("erro.");
        }
    }

    public List<String> getReqFeitas() {
        return servidor.getReqFeitas();
    }
    public List<String> getReqAtivas() {
        return servidor.getReqAtivas();
    }
    public List<String> getEntFeitas() {
        return servidor.getEntFeitas();
    }
    public List<String> getEntAtivas() {
        return servidor.getEntAtivas();
    }

    public void validaRegisto(String nome, String codID, String pwd){
        if(nome == null) {
            View.alert("ERRO","Não introduziu um Nome.");
            return;
        }

        if(codID == null) {
            View.alert("ERRO","Não introduziu um Código ID.");
            return;
        }

        if(pwd == null) {
            View.alert("ERRO","Não introduziu uma Password.");
            return;
        }

        if (servidor.containsGestor(codID)) {
            View.alert("Erro.", "O Código ID introduzido já pertence a outro Gestor. Tente novamente com um novo ID.");
        } else add(codID, nome, pwd);
    }

    public void end_scene(ActionEvent e) {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
