package utils;

import com.sun.xml.internal.bind.api.impl.NameConverter;
import entities.Bank;
import entities.Customer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import entities.Intermediary;

public class Utils {
    public static void getDoubleBox(Bank bank, Customer customer) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipherCustomerBill = Cipher.getInstance("AES");
        cipherCustomerBill.init(Cipher.ENCRYPT_MODE, bank.getKey());
        byte[] blockWithCustomerBill = cipherCustomerBill.doFinal(customer.getBillId().getBytes());
        logByte("Inner box has been created", blockWithCustomerBill);

        Cipher cipherMessageWithBox = Cipher.getInstance("AES");
        cipherMessageWithBox.init(Cipher.ENCRYPT_MODE, Intermediary.getIntermediarySecretKey());
        byte[] doubleBox = cipherMessageWithBox.doFinal(blockWithCustomerBill);
        logByte("Double box has been created", doubleBox);

    }

    private static void logByte(String message, byte[] bytes) throws IOException {
       String file = "double_box_logs.txt";
        Files.write(Paths.get(file), message.getBytes(), StandardOpenOption.APPEND);
        Files.write(Paths.get(file), "\n".getBytes(), StandardOpenOption.APPEND);
        Files.write(Paths.get(file), bytes, StandardOpenOption.APPEND);
        Files.write(Paths.get(file), "\n".getBytes(), StandardOpenOption.APPEND);
    }
}
