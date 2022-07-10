package DL;

import BL.Entrega;
import BL.Robot;

import java.util.*;

public class EntregaDAO extends DataAcessObject<String, Entrega>{
    private static EntregaDAO singleton = new EntregaDAO();

    public EntregaDAO() {
        super(new Entrega(), "Entrega", Arrays.asList("codID", "palete", "conteudo"));
    }

    public static EntregaDAO getInstance(){
        return EntregaDAO.singleton;
    }

    public ArrayList<Entrega> values(){
        return (ArrayList<Entrega>) super.values();
    }

    public Entrega get(final String key) {
        return super.get(key);
    }

    public Entrega put(final Entrega value) {
        return super.put(value, value.getCodID(), value.conteudo().getCodID(), value.conteudo().conteudo());
    }

    public Entrega remove(final String key) {
        return super.remove(key);
    }

    public Set<Entrega> search(final String value) {
        return super.search(value, 0);
    }
}
