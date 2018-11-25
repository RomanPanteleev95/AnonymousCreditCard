package utils;

import constants.Constant;
import entities.Customer;
import entities.Location;
import entities.banks.Bank;
import entities.blocks.DoubleBlock;
import entities.blocks.InnerBlock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static utils.Utils.getStatement;

public class DataBaseUtils {

    public static void createNewCustomer(String name, String email, String password) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.CREATE_NEW_CUSTOMER);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, email);
        preparedStatement.executeUpdate();
    }

    public static Bank getBankByID(int id) throws SQLException, ClassNotFoundException {
        Bank bank = new Bank();
        PreparedStatement bankStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_BANK_BY_ID);
        bankStatement.setInt(1, id);
        ResultSet resultSet = bankStatement.executeQuery();
        if (resultSet.next()){
            bank.setId(resultSet.getInt("id"));
            bank.setName(resultSet.getString("name"));
            bank.setType("type");
        }

        bank.setPrivateKey(getBanksPrivateKey(id));
        bank.setSharedKeyWithIntermediary(getBanksKeySharedWithIntermediary(id));
        return bank;
    }

    public static Bank getBankByName(String name) throws SQLException, ClassNotFoundException {
        Bank bank = new Bank();
        PreparedStatement bankStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_BANK_BY_NAME);
        bankStatement.setString(1, name);
        ResultSet resultSet = bankStatement.executeQuery();
        if (resultSet.next()){
            bank.setId(resultSet.getInt("id"));
            bank.setName(resultSet.getString("name"));
            bank.setType("type");
        }

        bank.setPrivateKey(getBanksPrivateKey(bank.getId()));
        bank.setSharedKeyWithIntermediary(getBanksKeySharedWithIntermediary(bank.getId()));
        return bank;
    }

    public static String getBanksPrivateKey(int bankId) throws SQLException, ClassNotFoundException {
        PreparedStatement bankStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_BANKS_PRIVATE_KEY);
        bankStatement.setInt(1, bankId);
        ResultSet resultSet = bankStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getString("private_key");
        }
        return null;
    }

    public static String getBanksKeySharedWithIntermediary(int bankId) throws SQLException, ClassNotFoundException {
        PreparedStatement bankStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_BANKS_KEY_SHARED_WITH_INTERMEDIARY);
        bankStatement.setInt(1, bankId);
        ResultSet resultSet = bankStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getString("shared_key_with_intermediary");
        }
        return null;
    }

    public static Customer getCustomerByEmail(String email) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_CUSTOMER_BY_EMAIL);
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        return fillCustomerParamets(resultSet);
    }

    public static Customer getCustomerByID(int id) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_CUSTOMER_BY_ID);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return fillCustomerParamets(resultSet);
    }

    private static Customer fillCustomerParamets(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        if (resultSet.next()) {
            customer.setId(resultSet.getInt("id"));
            customer.setName(resultSet.getString("name"));
            customer.setEmail(resultSet.getString("email"));
            customer.setCustomerPassword(resultSet.getString("customer_password"));
            customer.setCreditBankId(resultSet.getInt("credit_bank_id"));
            customer.setAccountIdInCreditCardBank(resultSet.getString("account_id_in_credit_bank"));
            customer.setDepositBankId(resultSet.getInt("deposit_bank_id"));
            customer.setAccountIdInDepositBank(resultSet.getString("account_id_in_deposit_bank"));
        }
        return customer;
    }

    public static ResultSet getAllDepositBanks() throws SQLException, ClassNotFoundException {
        Statement statement = getStatement();
        return statement.executeQuery(Constant.SqlQuery.GET_ALL_DEPOSIT_BANKS);
    }

    public static ResultSet getAllCreditBanks() throws SQLException, ClassNotFoundException {
        Statement statement = getStatement();
        return statement.executeQuery(Constant.SqlQuery.GET_ALL_CREDIT_BANKS);
    }

    public static void registrationCustomerInBank(int creditBankId, String accountIdInCreditCardCardBank, int depositBankId, String accountIdInDepositBank, String email) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.REGISTRATION_CUSTOMER_IN_BANK);
        preparedStatement.setInt(1, creditBankId);
        preparedStatement.setString(2, accountIdInCreditCardCardBank);
        preparedStatement.setInt(3, depositBankId);
        preparedStatement.setString(4, accountIdInDepositBank);
        preparedStatement.setString(5, email);
        preparedStatement.executeUpdate();
    }

    public static void createDoubleBox(int customerId, int bank_id, DoubleBlock doubleBlock) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.CREATE_DOUBLE_BLOCK);
        preparedStatement.setInt(1, customerId);
        preparedStatement.setInt(2, bank_id);
        preparedStatement.setString(3, doubleBlock.getBankName());
        preparedStatement.setString(4, doubleBlock.getInnerBlock().getInformation());
        preparedStatement.executeUpdate();
    }

    public static void addAlliesDoubleBox(int bankId, int doubleBlockId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.ADD_ALIES_DOUBLE_BLOCK);
        preparedStatement.setInt(1, bankId);
        preparedStatement.setInt(2, doubleBlockId);
        preparedStatement.executeUpdate();
    }

    public static int getLastDoubleBlockId() throws SQLException, ClassNotFoundException {
        Statement statement = getStatement();
        ResultSet resultSet = statement.executeQuery(Constant.SqlQuery.GET_LAST_DOUBLE_BLOCK);
        int id = 0;
        if (resultSet.next()){
            id = resultSet.getInt("id");
        }
        return id;
    }

    public static void addMoneyOnCustomerAccount(String customerAccountId, float money) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_MONEY_FROM_CUSTOMER_ACCOUNT);
        preparedStatement.setString(1, customerAccountId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Float currentMoney = Float.parseFloat(resultSet.getString("current_money"));
        currentMoney += money;
        preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.UPDATE_MONEY_ON_ACCPUNT);
        preparedStatement.setFloat(1, currentMoney);
        preparedStatement.setString(2, customerAccountId);
        preparedStatement.executeUpdate();
    }

    public static void removeMoneyFromCustomerAccount(String customerAccountId, float money) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_MONEY_FROM_CUSTOMER_ACCOUNT);
        preparedStatement.setString(1, customerAccountId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Float currentMoney = Float.parseFloat(resultSet.getString("current_money"));
        currentMoney -= money;
        preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.UPDATE_MONEY_ON_ACCPUNT);
        preparedStatement.setFloat(1, currentMoney);
        preparedStatement.setString(2, customerAccountId);
        preparedStatement.executeUpdate();
    }

    public static DoubleBlock getCustomerDoubleBlockByBankId(int customerId, int bankId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_CUSTOMER_DOUBLE_BLOCK_BY_BANK_ID);
        preparedStatement.setInt(1, customerId);
        preparedStatement.setInt(2, bankId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        InnerBlock innerBlock = new InnerBlock(resultSet.getString("innner_box"));
        DoubleBlock doubleBlock = new DoubleBlock(resultSet.getString("encode_bank_name"), innerBlock);
        return doubleBlock;
    }

    public static void createAccounMoney(String accountId, float money) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.CREATE_ACOOUNT_MONEY);
        preparedStatement.setString(1, accountId);
        preparedStatement.setFloat(2, money);
        preparedStatement.executeUpdate();
    }

    public static List<Location> getAllLocation() throws SQLException, ClassNotFoundException {
        List<Location> locations = new ArrayList<>();
        Statement statement = getStatement();
        ResultSet rs = statement.executeQuery(Constant.SqlQuery.GET_ALL_LOCATIONS);
        while (rs.next()){
            int locationId = rs.getInt("id");
            String locationName = rs.getString("name");
            locations.add(new Location(locationId, locationName));
        }
        return locations;
    }

    public static Location getLocationByName(String name) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_LOCATION_BY_NAME);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Location location = new Location(resultSet.getInt("id"), resultSet.getString("name"));
        location.setDepositBankId(resultSet.getInt("deposit_bank_id"));
        location.setAccountIdInDepositBank(resultSet.getString("account_id_in_deposit_bank"));
        return location;
    }

    public static DoubleBlock getLocationDoubleBlockByBankId(int locationId, int bankId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_LOCATION_DOUBLE_BLOCK_BY_BANK_ID);
        preparedStatement.setInt(1, locationId);
        preparedStatement.setInt(2, bankId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        InnerBlock innerBlock = new InnerBlock(resultSet.getString("innner_box"));
        DoubleBlock doubleBlock = new DoubleBlock(resultSet.getString("encode_bank_name"), innerBlock);
        return doubleBlock;
    }

    public static Float getCuurentBalance(Customer customer) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_CURRENT_BALANCE_BY_CUSTOMER_ID);
        preparedStatement.setInt(1, customer.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getFloat("current_money");
        }
        return 0f;
    }

    public static void addCommonParametr(String name, String value) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.ADD_COMMON_PARAMETR);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, value);
        preparedStatement.executeUpdate();
    }

    public static Map<String, String> getCommonParametrs() throws SQLException, ClassNotFoundException {
        Map<String, String> commonParametrs = new HashMap<>();
        Statement statement = getStatement();
        ResultSet rs = statement.executeQuery(Constant.SqlQuery.GET_COMMON_PARAMETRS);
        while (rs.next()){
            String name = rs.getString("name");
            String value = rs.getString("value");
            commonParametrs.put(name, value);
        }

        return commonParametrs;
    }

    public static Map<String, String> getBanksPublicKeyForRsa(int bankId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_BANKS_PUBLIC_KEY_FOR_RSA);
        preparedStatement.setInt(1, bankId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, String> rsaPublicKey = new HashMap<>();
        if(resultSet.next()){
            rsaPublicKey.put("e", resultSet.getString("e"));
            rsaPublicKey.put("n", resultSet.getString("n"));
        }

        return rsaPublicKey;
    }

    public static Map<String, String> getBanksPrivateKeyForRsa(int bankId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_BANKS_PRIVATE_KEY_FOR_RSA);
        preparedStatement.setInt(1, bankId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Map<String, String> rsaPrivateKey = new HashMap<>();
        if(resultSet.next()){
            rsaPrivateKey.put("d", resultSet.getString("d"));
            rsaPrivateKey.put("n", resultSet.getString("n"));
        }

        return rsaPrivateKey;
    }

    public static void setSharedKeyWithCustomer(int bankId, int customerId, String key) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.SET_SHARED_KEY_SITH_CUSTOMER);
        preparedStatement.setInt(1, bankId);
        preparedStatement.setInt(2, customerId);
        preparedStatement.setString(3, key);
        preparedStatement.executeUpdate();
    }

    public static void setEncryptSharedKeyWithCustomer(int bankId, int customerId, String key) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.SET_ENCRYPT_SHARED_KEY_WITH_CUSTOMER);
        preparedStatement.setInt(1, bankId);
        preparedStatement.setInt(2, customerId);
        preparedStatement.setString(3, key);
        preparedStatement.executeUpdate();
    }

    public static  String getIntermediaryKeySharedWithBank(int bankId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_INTERMEDIARY_KEY_SHARED_WITH_BANK);
        preparedStatement.setInt(1, bankId);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("banks_shared_key");
    }

    public static String getIntermediaryKeySharedWithLocation(int locationID) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_INTERMEDIARY_KEY_SHARED_WITH_LOCATION);
        preparedStatement.setInt(1, locationID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("locations_shared_key");
    }

    public static String getLocationKeySharedWithIntermediary(int locationID) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_LOCATION_KEY_SHARED_WITH_NTERMEDIARY);
        preparedStatement.setInt(1, locationID);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("shared_key_with_intermediary");
    }

    public static String getBankKeySharedWithCustomer(int bankId, int customerId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_SHARED_KEY_WITH_CUSTOMER);
        preparedStatement.setInt(1, bankId);
        preparedStatement.setInt(2, customerId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            return resultSet.getString("shared_key");
        }

        return null;
    }
}

