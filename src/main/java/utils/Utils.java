package utils;

import constants.Constant;
import db.DataBaseAnalog;
import entities.Customer;
import entities.Location;
import entities.blocks.DoubleBlock;
import entities.blocks.InnerBlock;
import entities.Intermediary;
import entities.banks.Bank;
import entities.banks.CreditCardBank;
import entities.banks.DepositBank;

import java.sql.*;

public class Utils {
    private static final DataBaseAnalog DATA_BASE_ANALOG = DataBaseAnalog.getDataBaseAnalog();

    public static PreparedStatement getPreparedStatement(String query) throws SQLException, ClassNotFoundException {
        return getConnection().prepareStatement(query);
    }

    public static Statement getStatement() throws ClassNotFoundException, SQLException {
       return getConnection().createStatement();
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql://ec2-54-217-236-201.eu-west-1.compute.amazonaws.com:5432" +
                "/ddh2dcreu9it5p?sslfactory=org.postgresql.ssl.NonValidatingFactory&user=wqdmxutohfcxex&password=78b1c206c96557b79a4b3a0968eea4f4975f8f3462a95c255ae68a7e86c95a66&ssl=true");
    }




    public static DoubleBlock getDLB(Bank bank, String customerId, InnerBlock innerBlock, String intermediaryKey){
        String encryptBanckId = encryptString(bank.getName(), intermediaryKey);
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
        DoubleBlock doubleBlockForCreditBanck = (DoubleBlock) getDLB(creditCardBank, customer.getName(), innerBlockForCreditBank, intermediary.getPrivateKey());
        DoubleBlock doubleBlockForDepositBanck = (DoubleBlock) getDLB(depositBank, customer.getName(), innerBlockForDepositBanck, intermediary.getPrivateKey());

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

    public static void registrationBank() throws ClassNotFoundException, SQLException {
        Statement statement = getStatement();
        ResultSet rs = statement.executeQuery(Constant.SqlQuery.SELECT_ALL_BANKS);
        while (rs.next()){
            int bankId = rs.getInt(1);
            String bankName = rs.getString(2);
            String bankType = rs.getString(3);
            String bankPrivateKey = rs.getString(4);
            String bankKeySharedWithIntemediary = rs.getString(5);

            if (bankType.equals("Deposit")){
                DepositBank depositBank = new DepositBank(bankName);
                depositBank.setPrivateKey(bankPrivateKey);
                depositBank.setSharedKeyWithIntermediary(bankKeySharedWithIntemediary);
                Intermediary intermediary = Intermediary.getIntermediary();
                intermediary.addBanksSharedKey(depositBank, bankKeySharedWithIntemediary);
                DATA_BASE_ANALOG.addDepositBank(depositBank);
            }

            if (bankType.equals("Credit")){
                CreditCardBank creditCardBankBank = new CreditCardBank(bankName);
                creditCardBankBank.setPrivateKey(bankPrivateKey);
                creditCardBankBank.setSharedKeyWithIntermediary(bankKeySharedWithIntemediary);
                Intermediary intermediary = Intermediary.getIntermediary();
                intermediary.addBanksSharedKey(creditCardBankBank, bankKeySharedWithIntemediary);
                DATA_BASE_ANALOG.addCreditBank(creditCardBankBank);
            }
        }
    }

    public static void registrationLocation(Location location){
        location.setLocationName("locationId");
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
