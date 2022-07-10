package BL;

import DL.Dados;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Gestor implements Dados<Gestor>{

private String nome;
private String password;
private String codeID;
private boolean online;


    public Gestor(String nome, String password, String codID, boolean online) {
        this.nome = nome;
        this.password = password;
        this.codeID = codID;
        this.online = online;
    }

    public Gestor(Gestor g) {
        this.nome = g.getNome();
        this.password = g.getPassword();
        this.codeID = g.getCodeID();
        this.online = g.getOnline();
    }

    public Gestor(){
    }

    public Gestor(List<String> l){
        this.codeID = l.get(0);
        this.nome = l.get(1);
        this.password = l.get(2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gestor gestor = (Gestor) o;
        return online == gestor.online &&
                Objects.equals(nome, gestor.nome) &&
                Objects.equals(password, gestor.password) &&
                Objects.equals(codeID, gestor.codeID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, password, codeID, online);
    }

    public boolean getOnline() {
        return online;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeID() {
        return codeID;
    }

    public void setCodID(String codID) {
        this.codeID = codID;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "Gestor{" +
                "nome='" + nome + '\'' +
                ", password='" + password + '\'' +
                ", codeID='" + codeID + '\'' +
                ", online=" + online +
                '}';
    }

    public Gestor clone(){
        return new Gestor(this);
    }

    private boolean checkPassID (String password, String codID){
        boolean r = false;
        if (password.equals(this.password) && codID.equals(this.codeID)) r = true;

        return r;
}

    public Dados<Gestor> fromRow(final List<String> l) {
        return new Gestor(l);
    }

    public List<String> toRow() {
        List<String> r = new ArrayList<>();
        r.add(this.codeID);
        r.add(this.nome);
        r.add(this.password);
        return r;
    }

}
