package Exceptions;

import GUI.*;

public class E404Exception extends Exception{
    public E404Exception(String titulo, String erro){
        View.alert(titulo,erro);
    }
}
