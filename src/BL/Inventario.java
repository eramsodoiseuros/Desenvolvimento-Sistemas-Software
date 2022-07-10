package BL;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Inventario {

    private HashMap<String,Palete> inventario;
    private int size_t1;
    private int size_t2;

    protected Inventario() {
        this.inventario = new HashMap<>();
        this.size_t1 = 0;
        this.size_t2 = 0;
    }

    protected Inventario(HashMap<String, Palete> inventario, int size_t1, int size_t2) {
        this.inventario = inventario;
        this.size_t1 = size_t1;
        this.size_t2 = size_t2;
    }

    protected int getSize_t1() {
        return size_t1;
    }
    protected void setSize_t1(int size_t1) {
        this.size_t1 = size_t1;
    }
    protected int getSize_t2() {
        return size_t2;
    }
    protected void setSize_t2(int size_t2) {
        this.size_t2 = size_t2;
    }

    protected void add(Palete p){
        this.inventario.putIfAbsent(p.getCodID(),p);
    }

    protected void remove(String codID){
        this.inventario.remove(codID);
    }

    protected boolean existe(String codID){
        return inventario.containsKey(codID);
    }

    protected boolean contains(String s) {
        return inventario.containsKey(s);
    }

    protected  int size(){return this.inventario.size();}

    protected Collection<Palete> values(){return this.inventario.values();}

    public Palete search(String s) {
        Palete pal = null;
        for(Palete p : inventario.values())
            if(p.getConteudo().equals(s)) {
                return p;
            }
        return pal;
    }

    protected List<Palete> listar(int n){
        return inventario.values().stream().filter(palete -> !palete.getLocalizacao().equals(new Point(1, 0))).limit(n).collect(Collectors.toList());
    }
}
