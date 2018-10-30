package utils;

import constants.Constant;
import entities.Customer;
import entities.banks.Bank;
import entities.banks.CreditCardBank;
import entities.banks.DepositBank;
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

public class DataBaseUtils {
    public static Bank getBankByID(int id) throws SQLException, ClassNotFoundException {
        PreparedStatement bankStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_BANK_BY_ID);
        bankStatement.setInt(1, id);
        ResultSet resultSet = bankStatement.executeQuery();
        return fillBankParametrs(resultSet);
    }

    public static Bank getBankByName(String name) throws SQLException, ClassNotFoundException {
        PreparedStatement bankStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_BANK_BY_NAME);
        bankStatement.setString(1, name);
        ResultSet resultSet = bankStatement.executeQuery();
        return fillBankParametrs(resultSet);
    }

    private static Bank fillBankParametrs(ResultSet resultSet) throws SQLException, ClassNotFoundException {
        Bank bank = new Bank();
        if (resultSet.next()){
            bank.setId(resultSet.getInt("id"));
            bank.setPrivateKey(resultSet.getString("private_key"));
            bank.setSharedKeyWithIntermediary(resultSet.getString("shared_intermediary_key"));
            bank.setName(resultSet.getString("name"));
            bank.setType(resultSet.getString("type"));
        }
        bank.setAlliesDoubleBlocks(getAliesDoubleBlocks(bank.getId()));

      return bank;

    }

    public static void createNewCustomer(String name, String email, String password) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.CREATE_NEW_CUSTOMER);
        preparedStatement.setString(1,name);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, email);
        preparedStatement.executeUpdate();
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
            customer.setSecretKeySharedWithCreditCardBank(resultSet.getString("shared_key_with_credit_bank"));
            customer.setAccountIdInCreditCardBank(resultSet.getString("account_id_in_credit_bank"));
            customer.setDepositBankId(resultSet.getInt("deposit_bank_id"));
            customer.setSecretKeySharedWithDepositBank(resultSet.getString("shared_key_with_deposit_bank"));
            customer.setAccountIdInDepositBank(resultSet.getString("account_id_in_deposit_bank"));
        }
        return customer;
    }

    public static ResultSet getAllDepositBanks() throws SQLException, ClassNotFoundException {
        Statement statement = Utils.getStatement();
        return statement.executeQuery(Constant.SqlQuery.GET_ALL_DEPOSIT_BANKS);
    }

    public static ResultSet getAllCreditBanks() throws SQLException, ClassNotFoundException {
        Statement statement = Utils.getStatement();
        return statement.executeQuery(Constant.SqlQuery.GET_ALL_CREDIT_BANKS);
    }

    public static void registrationCustomerInBank(int creditBankId, String accountIdInCreditCardCardBank, String keySharedWithCreditBank, int depositBankId, String accountIdInDepositBank, String keySharedWithDepositBank, String email) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.REGISTRATION_CUSTOMER_IN_BANK);
        preparedStatement.setInt(1, creditBankId);
        preparedStatement.setString(2, accountIdInCreditCardCardBank);
        preparedStatement.setString(3, keySharedWithCreditBank);
        preparedStatement.setInt(4, depositBankId);
        preparedStatement.setString(5, accountIdInDepositBank);
        preparedStatement.setString(6, keySharedWithDepositBank);
        preparedStatement.setString(7, email);
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

    public static Map<Integer, DoubleBlock> getAliesDoubleBlocks(int bank_id) throws SQLException, ClassNotFoundException {
        Map<Integer, DoubleBlock> resultMap = new HashMap<>();
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.GET_ALIES_DOUBLE_BLOCKS);
        preparedStatement.setInt(1, bank_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet == null || !resultSet.next()){
            return resultMap;
        }

        while (resultSet.next()){
            int customerId = resultSet.getInt("customer_id");
            InnerBlock innerBlock = new InnerBlock(resultSet.getString("innner_box"));
            DoubleBlock doubleBlock = new DoubleBlock(resultSet.getString("encode_bank_name"), innerBlock);
            resultMap.put(customerId, doubleBlock);
        }

        return resultMap;
    }

    public static void addAlliesDoubleBox(int bankId, int doubleBlockId) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.ADD_ALIES_DOUBLE_BLOCK);
        preparedStatement.setInt(1, bankId);
        preparedStatement.setInt(2, doubleBlockId);
        preparedStatement.executeUpdate();
    }

    public static int getLastDoubleBlockId() throws SQLException, ClassNotFoundException {
        Statement statement = Utils.getStatement();
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
}
