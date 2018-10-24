package utils;

import constants.Constant;
import entities.Customer;
import entities.banks.Bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

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

    public static ResultSet getAllDepositBanks() throws SQLException, ClassNotFoundException {
        Statement statement = Utils.getStatement();
        return statement.executeQuery(Constant.SqlQuery.GET_ALL_DEPOSIT_BANKS);
    }

    public static ResultSet getAllCreditBanks() throws SQLException, ClassNotFoundException {
        Statement statement = Utils.getStatement();
        return statement.executeQuery(Constant.SqlQuery.GET_ALL_CREDIT_BANKS);
    }
}
