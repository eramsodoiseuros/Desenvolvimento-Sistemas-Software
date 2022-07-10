package BL;

import DL.Dados;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Palete implements Dados<Palete> {
    private boolean refrigerado;
    private String conteudo;
    private boolean armazenado;
    private String codeID;
    private Point localizacao;

    protected String getConteudo() {
        return conteudo;
    }
    protected boolean isArmazenado() {
        return armazenado;
    }
    protected void setArmazenado(boolean armazenado) {
        this.armazenado = armazenado;
    }
    public String getCodID() {
        return codeID;
    }
    protected void setLocalizacao(Point l) {this.localizacao = l;}
    protected Point getLocalizacao(){return this.localizacao;}

    protected Palete(boolean refrigerado, String conteudo, String codID) {
        this.refrigerado = refrigerado;
        this.conteudo = conteudo;
        this.codeID = codID;
    }

    public Palete(String codID, String c){
        codeID = codID;
        conteudo = c;
        armazenado = false;
        refrigerado = false;
        localizacao = new Point(1,0);
    }

    public Palete(List<String> l){
        this.codeID = l.get(0);
        this.conteudo = l.get(1);
        this.armazenado = true;
        if(l.get(2).equals("1"))
            refrigerado = true;
        if(l.get(2).equals("0"))
            refrigerado = false;
        this.localizacao = new Point(Integer.parseInt(l.get(3)), Integer.parseInt(l.get(4)));
    }

    public Palete(Palete p){
        codeID = p.codeID;
        conteudo = p.conteudo;
        armazenado = p.armazenado;
        refrigerado = p.refrigerado;
        localizacao = p.localizacao;
    }

    public Palete(){
    }

    @Override
    public String toString() {
        return "Palete{" +
                "refrigerado=" + refrigerado +
                ", conteudo=" + conteudo +
                ", armazenado=" + armazenado +
                ", codID='" + codeID + '\'' +
                ", localizacao='" + localizacao.x +' '+localizacao.y+ '\'' +
                '}';
    }

    public String toStringID_NA_C(){
        return "Palete: " + codeID + " - NÃO ARMAZENADA - " + conteudo;
    }
    public String toStringID_A_C(){
        return "Palete: " + codeID + " - ARMAZENADA - " + conteudo + " - " + toStringLocalizacao(localizacao);
    }
    public String toStringID_C(){
        return "Palete: " + codeID + " - " + conteudo;
    }
    public String toStringLocalizacao(Point p){
        return "Localização: " + "(" + p.x + ", " + p.y + ")";
    }
    public String toStringListagem(){
        return "Palete: " + codeID + " - " + toStringLocalizacao(localizacao);
    }
    public String conteudo(){
        return conteudo;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Palete palete = (Palete) o;
        return refrigerado == palete.refrigerado &&
                armazenado == palete.armazenado &&
                localizacao.equals(palete.getLocalizacao()) &&
                Objects.equals(conteudo, palete.conteudo) &&
                Objects.equals(codeID, palete.codeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refrigerado, conteudo, armazenado, codeID, localizacao);
    }

    @Override
    public Dados<Palete> fromRow(List<String> row) {
        return new Palete(row);
    }

    @Override
    public List<String> toRow() {
        List<String> r = new ArrayList<>();
        r.add(this.codeID);
        r.add(this.conteudo);
        if(this.refrigerado)
            r.add("1");
        if (!this.refrigerado)
            r.add("0");
        r.add(this.localizacao.x + "");
        r.add(this.localizacao.y + "");
        return r;
    }
}
