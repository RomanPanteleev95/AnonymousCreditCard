package entities.banks;

import entities.Customer;
import entities.blocks.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class DepositBank extends Bank{

    private Map<String, DoubleBlock> customerBlockInCreditCardBank = new HashMap<>();

    /* map: customerId -> billId */
    private Map<String, String> customerBill = new HashMap<>();

    /* map: customerId -> billId */
    private Map<String, String> customerMoney = new HashMap<>();

    /* map: locationId -> sharedKey */
    private Map<String, String> sharedLocationKey = new HashMap<>();

    public DepositBank(int id, String name) {
        super(id, name);
    }

    public void addCreditBankDoubleBlock(Bank bank, DoubleBlock doubleBlock){
        customerBlockInCreditCardBank.put(bank.getName(), doubleBlock);
    }

    public void addCustomerBill(Customer customer){
        customerBill.put(customer.getName(), customer.getAccountIdInDepositBank());
    }


    public void addSharedKey(Customer customer){
        sharedCustomerKey.put(customer.getName(), customer.getSecretKeySharedWithDepositBank());
    }

    public void addMoneyToBill(Customer customer, String money){
        Integer currentBill = Integer.parseInt(customerMoney.get(customer.getName()));
        currentBill += Integer.parseInt(money);
        customerMoney.put(customer.getName(), currentBill.toString());
    }

    public void getMoneyFromBill(Customer customer, String money){
        Integer currentBill = Integer.parseInt(customerMoney.get(customer.getName()));
        currentBill -= Integer.parseInt(money);
        if (currentBill < 0){
            System.out.println("Недостаточно средств!");
        }else {
            customerMoney.put(customer.getName(), currentBill.toString());
        }
    }

    public void addNewBill(Customer customer){
        customerMoney.put(customer.getName(), "0");
    }

    public Map<String, String> getCustomerMoney() {
        return customerMoney;
    }
}
