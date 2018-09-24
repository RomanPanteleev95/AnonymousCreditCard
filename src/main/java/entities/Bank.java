package entities;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Bank {
    private int id;
    private String privateKey;
    private Map<Integer, Integer> customersBill;

    public Bank(int id) {
        this.id = id;
    }

    public SecretKey getKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        return kgen.generateKey();
    }
}
