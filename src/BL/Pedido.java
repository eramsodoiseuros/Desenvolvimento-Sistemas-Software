package BL;

import java.util.ArrayList;

public abstract class Pedido {
    String codeID;
    Palete conteudo;
    boolean estado;

    abstract boolean estado();
}
