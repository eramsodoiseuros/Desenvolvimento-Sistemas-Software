package DL;

import BL.Gestor;
import BL.Palete;

import java.util.*;

public class GestorDAO extends DataAcessObject<String, Gestor>{
    private static GestorDAO singleton = new GestorDAO();

    public GestorDAO() {
        super(new Gestor(), "Gestor", Arrays.asList("codID", "nome", "password"));
    }

    public static GestorDAO getInstance(){
        return GestorDAO.singleton;
    }

    public ArrayList<Gestor> values(){
        return (ArrayList<Gestor>) super.values();
    }

    public Gestor get(final String key) {
        return super.get(key);
    }

    public Gestor put(final Gestor value) {
        return super.put(value, value.getCodeID());
    }

    public Gestor remove(final String key) {
        return super.remove(key);
    }

    public Set<Gestor> search(final String value) {
        return super.search(value, 0);
    }

    public Set<Gestor> searchName(final String value) {
        return super.search(value, 1);
    }
}
