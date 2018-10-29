package utils;

import constants.Constant;
import entities.Customer;
import entities.Location;
import entities.blocks.DoubleBlock;
import entities.blocks.InnerBlock;
import entities.Intermediary;
import entities.banks.Bank;

import java.sql.*;
import java.util.UUID;

public class Utils {

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
        String encryptInnerBox = encryptString(innerBlock.getInformation(), intermediaryKey);
        innerBlock.setInformation(encryptInnerBox);
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


    public static void registrationCustomer(Customer customer, Bank creditCardBank, Bank depositBank) throws SQLException, ClassNotFoundException {
        String accountIdInCreditCardCardBank = UUID.randomUUID().toString().replaceAll("-", "");
        String accountIdInDepositBank = UUID.randomUUID().toString().replaceAll("-", "");
        String keySharedWithCreditBank = UUID.randomUUID().toString().replaceAll("-", "");
        String keySharedWithDepositBank = UUID.randomUUID().toString().replaceAll("-", "");

        DataBaseUtils.registrationCustomerInBank(creditCardBank.getId(), accountIdInCreditCardCardBank, keySharedWithCreditBank, depositBank.getId(), accountIdInDepositBank, keySharedWithDepositBank, customer.getEmail());

        InnerBlock innerBlockForCreditBank = getInnerBlock(creditCardBank.getPrivateKey(), accountIdInCreditCardCardBank);
        InnerBlock innerBlockForDepositBanck = getInnerBlock(depositBank.getPrivateKey(), accountIdInDepositBank);

        Intermediary intermediary = Intermediary.getIntermediary();
        DoubleBlock doubleBlockForCreditBanck = (DoubleBlock) getDLB(creditCardBank, customer.getName(), innerBlockForCreditBank, intermediary.getPrivateKey());
        DoubleBlock doubleBlockForDepositBanck = (DoubleBlock) getDLB(depositBank, customer.getName(), innerBlockForDepositBanck, intermediary.getPrivateKey());

        DataBaseUtils.createDoubleBox(customer.getId(), creditCardBank.getId(), doubleBlockForCreditBanck);
        DataBaseUtils.addAlliesDoubleBox(depositBank.getId(), DataBaseUtils.getLastDoubleBlockId());
        DataBaseUtils.createDoubleBox(customer.getId(), depositBank.getId(), doubleBlockForDepositBanck);
        DataBaseUtils.addAlliesDoubleBox(creditCardBank.getId(), DataBaseUtils.getLastDoubleBlockId());
    }

    public static void startRefill() throws ClassNotFoundException, SQLException {
        Intermediary intermediary = Intermediary.getIntermediary();
        Statement statement = getStatement();
        ResultSet rs = statement.executeQuery(Constant.SqlQuery.GET_ALL_BANKS);
        while (rs.next()){
            String bankName = rs.getString(2);
            String bankKeySharedWithIntemediary = rs.getString(5);
            intermediary.addBanksSharedKey(bankName, bankKeySharedWithIntemediary);
        }

        rs = statement.executeQuery(Constant.SqlQuery.GET_ALL_LOCATIONS);
        while (rs.next()){
            int locationId = rs.getInt("id");
            String sharedKeyWirhIntemediary = rs.getString("shared_key_with_intermediary");
            intermediary.addLocationSharedKey(locationId, sharedKeyWirhIntemediary);
        }
    }

    public static void registrationLocation(Location location){
        location.setLocationName("locationId");
        String sharedKeyLocationWithIntermediary = "sharedKeyLocationWithIntermediary";
        Intermediary intermediary = Intermediary.getIntermediary();
        location.setSharedKeyWithIntermediary(sharedKeyLocationWithIntermediary);
    }

    public static DoubleBlock encryptDoubleBlock(DoubleBlock doubleBlock, String key){
        String encryptionBankIdFromDoubleBlock = Utils.encryptString(doubleBlock.getBankName(), key);
        InnerBlock innerBlock = doubleBlock.getInnerBlock();
        innerBlock.setInformation(Utils.encryptString( innerBlock.getInformation(), key));
        return new DoubleBlock(encryptionBankIdFromDoubleBlock, innerBlock);
    }

    public static DoubleBlock decryptDoubleBlock(DoubleBlock doubleBlock, String key){
        String decriptionBankIdFromDoubleBlock = Utils.decryptString(doubleBlock.getBankName(), key);
        InnerBlock innerBlock = doubleBlock.getInnerBlock();
        innerBlock.setInformation(Utils.decryptString(innerBlock.getInformation(), key));
        return new DoubleBlock(decriptionBankIdFromDoubleBlock, innerBlock);
    }

}
