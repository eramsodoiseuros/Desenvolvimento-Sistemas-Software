package BL;

import DL.EntregaDAO;
import DL.RequisicaoDAO;
import UI.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GestorPedidos {

    private HashMap<String, Requisicao> requisicoes;
    private HashMap<String, Entrega> entrega;

    private HashMap<String, Requisicao> requisicoes_ATIVAS;
    private HashMap<String, Entrega> entrega_ATIVAS;

    private HashMap<String, Requisicao> requisicoes_FEITAS;
    private HashMap<String, Entrega> entrega_FEITAS;

    protected GestorPedidos(){
        this.requisicoes = new HashMap<>();
        this.entrega = new HashMap<>();
        this.requisicoes_FEITAS = new HashMap<>();
        this.entrega_FEITAS = new HashMap<>();
        this.requisicoes_ATIVAS = new HashMap<>();
        this.entrega_ATIVAS = new HashMap<>();
    }

    protected ArrayList<Requisicao> listaRequisicoes_FEITAS(){
        return new ArrayList<>(requisicoes_FEITAS.values());
    }
    protected ArrayList<Requisicao> listaRequisicoes_ATIVAS(){
        return new ArrayList<>(requisicoes_ATIVAS.values());
    }
    protected ArrayList<Entrega> listaEntrega_FEITAS(){
        return new ArrayList<>(entrega_FEITAS.values());
    }
    protected ArrayList<Entrega> listaEntrega_ATIVAS(){
        return new ArrayList<>(entrega_ATIVAS.values());
    }

    protected void removeRA(String codID){
        this.requisicoes_FEITAS.putIfAbsent(codID, requisicoes_ATIVAS.get(codID));
        UI.notifica("NOTIFICAÇÃO DE MUDANÇA DE ESTADO: " + "R->" + codID + "    ||      Conteudo ->" + requisicoes_FEITAS.get(codID).conteudo.toStringID_C() + " de momento está FEITA.");
        RequisicaoDAO.getInstance().put(requisicoes_ATIVAS.get(codID));
        this.requisicoes_ATIVAS.remove(codID);
    }

    protected void removeEA(String codID){
        this.entrega_FEITAS.putIfAbsent(codID, entrega_ATIVAS.get(codID));
        UI.notifica("NOTIFICAÇÃO DE MUDANÇA DE ESTADO: " + "E->" + codID + "    ||      Conteudo ->" + entrega_FEITAS.get(codID).conteudo.toStringID_A_C() + " de momento está FEITA.");
        EntregaDAO.getInstance().put(entrega_ATIVAS.get(codID));
        this.entrega_ATIVAS.remove(codID);
    }
    protected void addEF(Entrega e) {
        entrega_FEITAS.putIfAbsent(e.codeID,e);
    }
    protected void addRF(Requisicao r) {
        requisicoes_FEITAS.putIfAbsent(r.codeID,r);
    }

    @Override
    public String toString() {
        return "GestorPedidos{" +
                "requisicoes_FEITAS=" + requisicoes_FEITAS +
                ", entrega_FEITAS=" + entrega_FEITAS +
                ", requisicoes_ATIVAS=" + requisicoes_ATIVAS +
                ", entrega_ATIVAS=" + entrega_ATIVAS +
                '}';
    }

    public String toStringEA(Entrega e){
        return "Entrega: " + e.getCodID() + " - " + e.toStingConteudoAtivas();
    }
    public String toStringEF(Entrega e){
        return "Entrega: " + e.getCodID() + " - " + e.toStringConteudoFeitas();
    }
    public String toStringRA(Requisicao r){
        return "Requisição: " + r.getCodID() + " - " + r.toStingConteudoAtivas();
    }
    public String toStringRF(Requisicao r){
        return "Requisição: " + r.getCodID() + " - " + r.toStringConteudoFeitas();
    }

    protected void addEntrega(Entrega e) {
        UI.notifica("NOTIFICAÇÃO DE MUDANÇA DE ESTADO: " + "Entrega ->" + e.codeID + "     ||      Conteudo ->" + e.conteudo.toStringID_NA_C() + " de momento está NÃO PROCESSADA.");
        entrega.putIfAbsent(e.codeID,e);
    }
    protected void addRequisicao(Requisicao r) {
        UI.notifica("NOTIFICAÇÃO DE MUDANÇA DE ESTADO: " + "Requisição ->" + r.codeID + "     ||      Conteudo ->" + r.conteudo.toStringID_C() + " de momento está NÃO PROCESSADA.");
        requisicoes.putIfAbsent(r.codeID, r);
    }
    protected void removeEntrega(String e) {
        this.entrega_ATIVAS.putIfAbsent(e, entrega.get(e));
        UI.notifica("NOTIFICAÇÃO DE MUDANÇA DE ESTADO: " + "E->" + e + "    ||      Conteudo ->" + entrega.get(e).conteudo.toStringID_C() + " de momento está ATIVA.");
        this.entrega.remove(e);
    }
    protected void removeRequisicao(String r) {
        this.requisicoes_ATIVAS.putIfAbsent(r, requisicoes.get(r));
        UI.notifica("NOTIFICAÇÃO DE MUDANÇA DE ESTADO: " + "R->" + r + "    ||      Conteudo ->" + requisicoes.get(r).conteudo.toStringID_C() + " de momento está ATIVA.");
        this.requisicoes.remove(r);
    }

    protected ArrayList<Requisicao> listaRequisicoes() {
        return new ArrayList<>(requisicoes.values());
    }
    protected ArrayList<Entrega> listaEntregas() {
        return new ArrayList<>(entrega.values());
    }

    protected boolean searchEA(String codID) {
        return entrega_ATIVAS.containsKey(codID);
    }
    protected boolean searchRA(String s) {
        return requisicoes_ATIVAS.containsKey(s);
    }
    protected boolean searchEF(String codID) {
        return (entrega_FEITAS.containsKey(codID) || EntregaDAO.getInstance().find(codID).size() > 0);
    }
    protected boolean searchRF(String s) {
        return (requisicoes_FEITAS.containsKey(s)|| RequisicaoDAO.getInstance().find(s).size() > 0);
    }

    public Requisicao getRA(String s) {
        return requisicoes_ATIVAS.get(s);
    }
    public Entrega getEA(String s) {
        return entrega_ATIVAS.get(s);
    }
}
