package entities;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Intermediary {
    private SecretKey privateKey;
    private Map<Integer, String> banksKey;
    private Map<Integer, DoubleBlock> doubleBlocks;

    public static SecretKey getIntermediarySecretKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        return kgen.generateKey();
    }
}
