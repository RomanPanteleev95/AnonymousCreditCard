package db;

import entities.Customer;
import entities.banks.Bank;

import java.util.HashMap;
import java.util.Map;

public class DataBaseAnalog {
    private Map<String, Bank> depositBanks = new HashMap<>();
    private Map<String, Bank> creditCradBanks = new HashMap<>();
    private Map<String, Customer> customers = new HashMap<>();

    private static DataBaseAnalog dataBaseAnalog;

    private DataBaseAnalog(){

    }

    public static DataBaseAnalog getDataBaseAnalog(){
        if (dataBaseAnalog == null){
            dataBaseAnalog = new DataBaseAnalog();
        }
        return dataBaseAnalog;
    }

    public void addDepositBank(Bank bank){
        depositBanks.put(bank.getName(), bank);
    }

    public void addCreditBank(Bank bank){
        creditCradBanks.put(bank.getName(), bank);
    }

    public void addCustomer(Customer customer){
        customers.put(customer.getName(), customer);
    }

    public Bank getDepositBank(String depositBankId){
        return depositBanks.get(depositBankId);
    }

    public Bank getCreditCardBank(String creditCardBankId){
        return creditCradBanks.get(creditCardBankId);
    }

    public Customer getCustomer(String customerId){
        return customers.get(customerId);
    }

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public Map<String, Bank> getDepositBanks() {
        return depositBanks;
    }

    public Map<String, Bank> getCreditCradBanks() {
        return creditCradBanks;
    }
}
