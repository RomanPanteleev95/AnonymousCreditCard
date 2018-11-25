package utils;

import entities.Customer;
import entities.Intermediary;
import entities.banks.Bank;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KeyDistributionUtils {
    private static BigInteger P;
    private static BigInteger A;

    public static  Map<String, String> getsharedKeyForEncryptPassword(String customerPrivateKey) throws SQLException, ClassNotFoundException {
        BigInteger serverPrivateKey = new BigInteger(128, new Random());
        Map<String, String> sharedKeys = new HashMap<>();
        Map<String, String> commonParametrs = DataBaseUtils.getCommonParametrs();
        if (!commonParametrs.isEmpty()){
            P = new BigInteger(commonParametrs.get("P"));
            A = new BigInteger(commonParametrs.get("A"));
        } else {
            do {
                P = new BigInteger(128, new Random());
            } while (P.mod(BigInteger.valueOf(2L)).compareTo(BigInteger.ZERO) != 0);


            do {
                A = new BigInteger(128, new Random());
            } while (A.compareTo(P) >= 0);

            DataBaseUtils.addCommonParametr("P", P.toString());
            DataBaseUtils.addCommonParametr("A", A.toString());
        }

        BigInteger customerPublicKey = A.modPow(new BigInteger(customerPrivateKey), P);
        BigInteger banksPublickKey = A.modPow(serverPrivateKey, P);

        BigInteger sharedKeyForServer = customerPublicKey.modPow(serverPrivateKey, P);

        sharedKeys.put("customer", banksPublickKey.toString());
        sharedKeys.put("server", sharedKeyForServer.toString());

        return sharedKeys;
    }

    public static String getSharedKeyForCustomer(String customerPrivateKey, String banksPublickKey) throws SQLException, ClassNotFoundException {
        Map<String, String> commonParametrs = DataBaseUtils.getCommonParametrs();
        if (!commonParametrs.isEmpty()){
            P = new BigInteger(commonParametrs.get("P"));
            A = new BigInteger(commonParametrs.get("A"));
        } else {
            do {
                P = new BigInteger(128, new Random());
            } while (P.mod(BigInteger.valueOf(2L)).compareTo(BigInteger.ZERO) != 0);


            do {
                A = new BigInteger(128, new Random());
            } while (A.compareTo(P) >= 0);

            DataBaseUtils.addCommonParametr("P", P.toString());
            DataBaseUtils.addCommonParametr("A", A.toString());
        }

        return new BigInteger(banksPublickKey).modPow(new BigInteger(customerPrivateKey), P).toString();
    }
}
