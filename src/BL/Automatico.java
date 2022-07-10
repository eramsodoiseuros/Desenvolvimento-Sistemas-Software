package BL;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class Automatico {
    public static String generateQR(final String digest) {
        Random rand = new Random();
        int r = rand.nextInt(1000);
        String yolo = r + "!?XD";
        try {
            byte[] hash = MessageDigest.getInstance("SHA-512").digest((digest + yolo).getBytes());
            return Base64.getEncoder().encodeToString(hash).substring(0,5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<String> create(int e){
        List<String> lista = new ArrayList<>();
        for(int i = 0; i < e; i++){
            lista.add(generateQR("!"));
        }
        return lista;
    }
}
