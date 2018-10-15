package utils;

import db.DataBaseAnalog;
import entities.Customer;
import entities.Location;
import entities.blocks.DoubleBlock;
import entities.blocks.InnerBlock;
import entities.Intermediary;
import entities.banks.Bank;
import entities.banks.CreditCardBank;
import entities.banks.DepositBank;
import org.apache.log4j.Logger;

public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class);
    private static final DataBaseAnalog DATA_BASE_ANALOG = DataBaseAnalog.getDataBaseAnalog();
    public static DoubleBlock getDLB(Bank bank, String customerId, InnerBlock innerBlock, String intermediaryKey){
        String encryptBanckId = encryptString(bank.getId(), intermediaryKey);
        String encryptInnerBox = encryptString(innerBlock.getEncryptInformation(), intermediaryKey);
        innerBlock.setEncryptInformation(encryptInnerBox);
        return new DoubleBlock(encryptBanckId, innerBlock);
    }

    public static InnerBlock getInnerBlock(String bankPrivateKey, String customerBillId){
        return new InnerBlock(encryptString(customerBillId, bankPrivateKey));
    }

    public static String encryptString(String infotmation, String key){
        char[] inf = infotmation.toCharArray();
        for (int i = 0; i < Math.min(infotmation.length(), key.length()); i++){
            inf[i] = (char) (infotmation.charAt(i) + key.charAt(i));
        }

        return new String(inf);
    }

    public static String decryptString(String information, String key){
        char[] inf = information.toCharArray();
        for (int i = 0; i < Math.min(information.length(), key.length()); i++){
            inf[i] = (char) (information.charAt(i) - key.charAt(i));
        }

        return new String(inf);
    }

    public static void createCustomer(String customerName){
        DATA_BASE_ANALOG.addCustomer(new Customer(customerName));
        logger.info("Customer was created: " +  customerName);
    }

    public static void createCreditCardBank(String name){
        DATA_BASE_ANALOG.addCreditBank(new CreditCardBank(name));
    }

    public static void createDepositBank(String name){
        DATA_BASE_ANALOG.addDepositBank(new DepositBank(name));
    }

    public static void registrationCustomer(Customer customer, Bank creditCardBank, Bank depositBank){
        DataBaseAnalog dataBaseAnalog = DataBaseAnalog.getDataBaseAnalog();
        String billIdInCreditCardCardBank = "billIdInCreditCardCardBank"; //random
        String billIdInDepositBank = "billIdInDepositBank"; //random
        String keySharedWithCreditBank = "keySharedWithCreditBank"; //random
        String keySharedWithDepositBank = "keySharedWithDepositBank"; //random


        customer.setBillIdInCreditCardBank(billIdInCreditCardCardBank);
        customer.setBillIdInDepositBank(billIdInDepositBank);
        customer.setSecretKeySharedWithCreditCardBank(keySharedWithCreditBank);
        customer.setSecretKeySharedWithDepositBank(keySharedWithDepositBank);



        InnerBlock innerBlockForCreditBank = getInnerBlock(creditCardBank.getPrivateKey(), billIdInCreditCardCardBank);
        InnerBlock innerBlockForDepositBanck = getInnerBlock(depositBank.getPrivateKey(), billIdInDepositBank);

        Intermediary intermediary = Intermediary.getIntermediary();
        DoubleBlock doubleBlockForCreditBanck = (DoubleBlock) getDLB(creditCardBank, customer.getId(), innerBlockForCreditBank, intermediary.getPrivateKey());
        DoubleBlock doubleBlockForDepositBanck = (DoubleBlock) getDLB(depositBank, customer.getId(), innerBlockForDepositBanck, intermediary.getPrivateKey());

        intermediary.addBankDoubleBlock(creditCardBank, doubleBlockForCreditBanck);
        intermediary.addBankDoubleBlock(depositBank, doubleBlockForDepositBanck);

        if (creditCardBank instanceof CreditCardBank){
            CreditCardBank cardBank = (CreditCardBank) creditCardBank;
            cardBank.addDepositBanckDoubleBlock(depositBank, doubleBlockForDepositBanck);
            cardBank.addCustomerBill(customer);
            cardBank.addSharedKey(customer);
        }

        if (depositBank instanceof DepositBank){
            DepositBank depBanck = (DepositBank) depositBank;
            depBanck.addCreditBankDoubleBlock(creditCardBank, doubleBlockForCreditBanck);
            depBanck.addCustomerBill(customer);
            depBanck.addSharedKey(customer);
            depBanck.addNewBill(customer);
        }

       dataBaseAnalog.addCustomer(customer);

    }

    public static void registrationBank(Bank bank){
        DataBaseAnalog dataBaseAnalog = DataBaseAnalog.getDataBaseAnalog();
        String bankPrivateKey = "banckPrivateKey";
        bank.setPrivateKey(bankPrivateKey);
        String sharedKeyWithIntermediary = "sharedKeyWithIntermediary";
        Intermediary intermediary = Intermediary.getIntermediary();
        intermediary.addBanksSharedKey(bank, sharedKeyWithIntermediary);
        bank.setSharedKeyWithIntermediary(sharedKeyWithIntermediary);
        if (bank instanceof CreditCardBank){
            dataBaseAnalog.addCreditBank(bank);
        }
        if (bank instanceof DepositBank){
            dataBaseAnalog.addDepositBank(bank);
        }
    }

    public static void registrationLocation(Location location){
        location.setLocationId("locationId");
        String sharedKeyLocationWithIntermediary = "sharedKeyLocationWithIntermediary";
        Intermediary intermediary = Intermediary.getIntermediary();
        location.setSharedKeyWithIntermediary(sharedKeyLocationWithIntermediary);
        intermediary.addLocationSharedKey(location);
    }

    public static DoubleBlock encryptDoubleBlock(DoubleBlock doubleBlock, String key){
        String encryptionBankIdFromDoubleBlock = Utils.encryptString(doubleBlock.getBankId(), key);
        InnerBlock innerBlock = doubleBlock.getInnerBlock();
        innerBlock.setEncryptInformation(Utils.encryptString( innerBlock.getEncryptInformation(), key));
        return new DoubleBlock(encryptionBankIdFromDoubleBlock, innerBlock);
    }

    public static DoubleBlock decryptDoubleBlock(DoubleBlock doubleBlock, String key){
        String decriptionBankIdFromDoubleBlock = Utils.decryptString(doubleBlock.getBankId(), key);
        InnerBlock innerBlock = doubleBlock.getInnerBlock();
        innerBlock.setEncryptInformation(Utils.decryptString(innerBlock.getEncryptInformation(), key));
        return new DoubleBlock(decriptionBankIdFromDoubleBlock, innerBlock);
    }

}
