package rsa;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by Dasha on 13.04.2017.
 */
public class RSA {
    BigInteger p;
    BigInteger q;
    BigInteger N;
    Alph alphObj = new Alph();

    public RSA(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
        N = p.multiply(q);
    }

    public RSA(){

    }

    public Key genKey() {
        BigInteger f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger d = BigInteger.valueOf(3);
        while (f.gcd(d).intValue() != 1) {
            d = d.add(BigInteger.ONE);
        }
        BigInteger e = d.modInverse(f);
        return new Key(e, d, N);
    }

    public String encrypt(BigInteger key, BigInteger module, String message) throws IOException, BadAttributeValueExpException {
        alphObj.getAlph();

        StringBuilder newS = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            newS.append(alphObj.alph.get(message.charAt(i)));
        }

        return toEncrypt(new BigInteger(newS.toString()), key, module);
    }

    public String toEncrypt(BigInteger bi, BigInteger key, BigInteger module) throws IOException {
        bi = bi.modPow(key, module);
        return bi.toString();
    }

    public String decrypt(BigInteger key, BigInteger module, String message) throws IOException {
        alphObj.getAlph();
        StringBuilder newS = new StringBuilder(message);
        BigInteger bigint = new BigInteger(newS.toString());

        return toDecrypt(bigint, key, module);
    }

    public String toDecrypt(BigInteger bi, BigInteger key, BigInteger module) {
        bi = bi.modPow(key, module);
        String s = bi.toString();
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < s.length(); i += 3) {
            int idx = Integer.parseInt(s.substring(i, i + 1)) * 100 + Integer.parseInt(s.substring(i + 1, i + 2)) * 10 + Integer.parseInt(s.substring(i + 2, i + 3));
            ans.append(alphObj.revAlph.get(idx));
        }
        return ans.toString();
    }

}
