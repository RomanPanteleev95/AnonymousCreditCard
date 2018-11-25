package rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dasha on 18.04.2017.
 */
public class RsaHelper {
    long x0;
    long c;
    int t = 1024;
    BigInteger twoIn32 = new BigInteger("4294967296");
    BigInteger two = new BigInteger("" + 2);
    ArrayList<BigInteger> masX = new ArrayList<>();
    ArrayList<Integer> masT = new ArrayList<>();

    String[] minPrimes = new String[]{"0", "0", "2", "5", "11", "17", "37", "67", "131", "257", "521", "1031", "2053", "4099",
            "8209", "16411", "32771", "65537", "131101", "262147", "524309", "1048583", "2097169", "4194319", "8388617", "16777259",
            "33554467", "67108879", "134217757", "268435459", "536870923", "1073741827", "2147483659"};


    ArrayList<BigInteger> masP;
    ArrayList<BigInteger> primes;
    Pair ans;

    BigInteger p;
    BigInteger q;
    BigInteger g;

    public Map<String, BigInteger> getPQ(){
        Map<String, BigInteger> pq = new HashMap<>();
        ans = getPrimes();
        p = ans.e;
        q = ans.d;
        pq.put("p", p);
        pq.put("q", q);
        return pq;
    }

    public Pair getPrimes() {

        masX.add(BigInteger.valueOf(x0));
        masT.add(t);

        BigInteger y0 = BigInteger.valueOf(x0);


        primes = new ArrayList<>();
        for (String num : minPrimes) {
            primes.add(new BigInteger(num));
        }


        while (t >= 33) {
            t = t / 2;
            masT.add(t);
        }


        int s = masT.size() - 1;


        masP = new ArrayList<>(Collections.nCopies(s + 1, BigInteger.ZERO));
        masP.set(s, primes.get(masT.get(s)));


        int m = s - 1;
        int rm = 0;
        int k = 0;
        BigInteger N = BigInteger.ZERO;

        int st = 5;
        BigInteger degOf2 = two.pow(masT.get(m));
        while (true) {
            switch (st) {
                case 5: {
                    rm = (masT.get(m) + 31) / 32;
                    st = 6;
                    break;
                }
                case 6: {
                    ArrayList<BigInteger> masY = new ArrayList<>();
                    masY.add(LCgenerator(y0, BigInteger.valueOf(c)));
                    //while (masP.get((int) m).compareTo(n1.multiply(BigInteger.valueOf(2))) == 1) {
                    for (int i = 0; i < rm - 1; i++) {
                        BigInteger y = LCgenerator(masY.get(masY.size() - 1), BigInteger.valueOf(c)).mod(twoIn32);
                        masY.add(y);
                    }

                    BigInteger ym = BigInteger.ZERO;
                    for (int i = 0; i < rm; i++) {
                        ym = ym.add(masY.get(i).multiply(two.pow(32 * i)));
                    }

                    y0 = masY.get(rm - 1);

                    BigInteger n1 = two.pow(masT.get(m) - 1);
                    BigInteger n2 = ((n1.add(masP.get(m + 1)).subtract(BigInteger.ONE)).divide(masP.get(m + 1)));
                    BigInteger n3 = n1.multiply(ym);
                    BigInteger n4 = two.pow(32 * rm);
                    BigInteger n5 = n3.divide(masP.get(m + 1).multiply(n4));

                    N = n2.add(n5);

                    if (!N.mod(two).equals(BigInteger.ZERO))
                        N = N.add(BigInteger.ONE);


                    k = 0;
                    st = 11;
                    break;
                }

                case 11: {
                    masP.set(m, (masP.get(m + 1).multiply(N.add(BigInteger.valueOf(k)))).add(BigInteger.ONE));
                    if (masP.get(m).compareTo(degOf2) > 0) {
                        st = 6;
                        break;
                    }
                    BigInteger deg1 = masP.get(m + 1).multiply(N.add(BigInteger.valueOf(k)));
                    BigInteger op1 = two.modPow(deg1, masP.get(m));
                    BigInteger deg2 = N.add(BigInteger.valueOf(k));
                    BigInteger op2 = two.modPow(deg2, masP.get(m));
                    if (op1.equals(BigInteger.ONE) && !op2.equals(BigInteger.ONE)) {
                        m--;
                        if (m >= 0) {
                            degOf2 = two.pow(masT.get(m));
                            st = 5;
                        } else {
                            ans = new Pair(masP.get(0), masP.get(1));
                            return ans;
                        }
                    } else {
                        k += 2;
                        st = 11;
                    }

                    break;
                }

            }

        }
    }

    public BigInteger LCgenerator(BigInteger x, BigInteger c) {
        return (BigInteger.valueOf(97781173).multiply(x).add(c).mod(twoIn32));
    }

    class Pair {
        BigInteger e;
        BigInteger d;

        public Pair(BigInteger e, BigInteger d) {
            this.e = e;
            this.d = d;

        }
    }

}
