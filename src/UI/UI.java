package UI;

import java.util.ArrayList;
import java.util.List;

public class UI {

    public static void notifica(String texto) {
        System.out.println(texto);
    }
    public static void print_mapa(Integer[][] mapa, int l, int c){
        List<String> linha = new ArrayList<>();
        for (int i = 0; i < l; i++){
            for(int j = 0; j < c; j++){
                if(mapa[i][j]==-1)
                    linha.add("X");
                else if(mapa[i][j]==2)
                    linha.add("0");
                else if(mapa[i][j]==0)
                    linha.add("o");
                else linha.add( mapa[i][j]-2 +"");
            }
            System.out.println(linha);
            linha.removeAll(linha);
        }
    }

}
