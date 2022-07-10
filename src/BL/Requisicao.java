package BL;

import DL.Dados;
import DL.InventarioDAO;
import GUI.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Requisicao extends Pedido implements Dados<Requisicao> {
    // estado == ATIVA
    public String getCodID(){
        return codeID;
    }

    public Requisicao(Palete p, String codID) {
        this.codeID = codID;
        this.conteudo = p;
        this.estado = true;
    }

    public Requisicao(){
    }

    public Requisicao(List<String> l){
        this.codeID = l.get(0);
        this.conteudo = new Palete("palete_outOfStock",l.get(1));
        this.estado = false;
    }

    boolean estado() {
        this.estado = !this.estado;
        return this.estado;
    }

    public Dados<Requisicao> fromRow(final List<String> l) {
        return new Requisicao(l);
    }

    public List<String> toRow() {
        List<String> r = new ArrayList<>();
        r.add(this.codeID);
        r.add(this.conteudo.conteudo());
        return r;
    }

    public String toStingConteudoAtivas() {
        return conteudo.toStringID_A_C();
    }

    public String toStringConteudoFeitas() {
        return conteudo.toStringID_C();
    }
}
