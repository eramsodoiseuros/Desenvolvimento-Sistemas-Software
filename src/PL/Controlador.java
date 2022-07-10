package PL;

import javafx.event.ActionEvent;
import java.util.List;

public interface Controlador {

    /* *
     * Fecha uma Scene do JavaFX
     * */
    void end_scene(ActionEvent e);

    /**
     * Verifica todos os codID de Gestores e ve se e possivel registar este novo
     * @param nome email do utilizador a ser validado
     * @param codID nome do utilizador a ser validado
     * @param pwd palavra-passe do utilizador a ser validado
     * */
    void validaRegisto(String nome, String codID, String pwd);

    /**
     * Verifica as informações de Login de um Gestor
     * @param codID codeID do Gestor
     * */
    void logOutGestor(String codID);

    /**
     * Verifica as informações de Login de um Gestor
     * @param codID codeID do Gestor
     * @param password password do Gestor
     * */
    boolean iniciaSessao(String codID, String password);

    /**
     * Função que
     * */
    void delete(String user);

    /**
     * Função que
     * */
    void addE(String s);

    /**
     * Função que
     * */
    void addR(String s);

    /**
     * Função que
     * */
    List<String> listagem();

    /**
     * Função que
     * */
    List<String> inventario();

    /**
     * Função que
     * */
    List<String> robots();

    /**
     * Função que
     * */
    List<String> lista_requisicoes();

    /**
     * Função que
     * */
    List<String> lista_entregas();

    /**
     * Função que
     * */
    int parking();

    /**
     * Função que
     * */
    List<String> getReqFeitas();

    /**
     * Função que
     * */
    List<String> getReqAtivas();

    /**
     * Função que
     * */
    List<String> getEntFeitas();

    /**
     * Função que
     * */
    List<String> getEntAtivas();

    void addRA(String s);
    void addEA(String s);

    List<String> lista_robots();

    void map();

    void both(int e1, int r1);
}
