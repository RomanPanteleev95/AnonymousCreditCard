package utils;

import constants.Constant;
import entities.Customer;
import entities.banks.Bank;
import entities.banks.CreditCardBank;
import entities.banks.DepositBank;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseUtils {
//    private Map<String, Bank> depositBanks = new HashMap<>();
//    private Map<String, Bank> creditCradBanks = new HashMap<>();
//    private Map<String, Customer> customers = new HashMap<>();
//
//    private static DataBaseUtils dataBaseAnalog;
//
//    private DataBaseUtils(){
//
//    }
//
//    public static DataBaseUtils getDataBaseAnalog(){
//        if (dataBaseAnalog == null){
//            dataBaseAnalog = new DataBaseUtils();
//        }
//        return dataBaseAnalog;
//    }
//
//    public void addDepositBank(Bank bank){
//        depositBanks.put(bank.getName(), bank);
//    }
//
//    public void addCreditBank(Bank bank){
//        creditCradBanks.put(bank.getName(), bank);
//    }
//
//    public void addCustomer(Customer customer){
//        customers.put(customer.getName(), customer);
//    }
//
//    public Bank getDepositBank(String depositBankId){
//        return depositBanks.get(depositBankId);
//    }
//
//    public Bank getCreditCardBank(String creditCardBankId){
//        return creditCradBanks.get(creditCardBankId);
//    }
//
//    public Customer getCustomer(String customerId){
//        return customers.get(customerId);
//    }
//
//    public Map<String, Customer> getCustomers() {
//        return customers;
//    }
//
//    public Map<String, Bank> getDepositBanks() {
//        return depositBanks;
//    }
//
//    public Map<String, Bank> getCreditCradBanks() {
//        return creditCradBanks;
//    }
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

    private static Bank fillBankParametrs(ResultSet resultSet) throws SQLException {
        Bank bank = new Bank();
        if (resultSet.next()){
            Bank creditBank = new CreditCardBank();
            creditBank.setId(resultSet.getInt("id"));
            creditBank.setPrivateKey(resultSet.getString("private_key"));
            creditBank.setSharedKeyWithIntermediary(resultSet.getString("shared_intermediary_key"));
            creditBank.setName(resultSet.getString("name"));
            creditBank.setType(resultSet.getString("type"));
        }

        if (bank.getType().equals("Credit")){
            return (CreditCardBank) bank;
        }else {
            return (DepositBank) bank;
        }

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


}
