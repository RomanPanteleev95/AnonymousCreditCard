package rsa;

import java.math.BigInteger;

public class Key {
    private BigInteger e;
    private BigInteger d;
    private BigInteger n;

    public Key(BigInteger e, BigInteger d, BigInteger n) {
        this.e = e;
        this.d = d;
        this.n = n;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }

    public BigInteger getN() {
        return n;
    }
}