package BL;

import DL.Dados;

import java.util.ArrayList;
import java.util.List;

public class Entrega extends Pedido implements Dados<Entrega>{
    // estado == ATIVA
    public String getCodID(){
        return codeID;
    }

    public Entrega(Palete p, String codID) {
        this.codeID = codID;
        this.conteudo = p;
        this.estado = true;
    }

    public Entrega(){
    }

    public Entrega(List<String> l){
        this.codeID = l.get(0);
        this.conteudo = new Palete(l.get(1), l.get(2));
        this.estado = false;
    }

    public Palete conteudo(){
        return conteudo;
    }

    boolean estado() {
        this.estado = !this.estado;
        return this.estado;
    }

    public Dados<Entrega> fromRow(final List<String> l) {
        return new Entrega(l);
    }

    public List<String> toRow() {
        List<String> r = new ArrayList<>();
        r.add(codeID);
        r.add(conteudo.getCodID());
        r.add(conteudo.conteudo());
        return r;
    }

    public String toStingConteudoAtivas() {
        return conteudo.toStringID_C();
    }

    public String toStringConteudoFeitas() {
        return conteudo.toStringID_A_C();
    }
}
