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

    public DepositBank(String id) {
        super(id);
    }

    public void addCreditBankDoubleBlock(Bank bank, DoubleBlock doubleBlock){
        customerBlockInCreditCardBank.put(bank.getId(), doubleBlock);
    }

    public void addCustomerBill(Customer customer){
        customersBill.put(customer.getId(), customer.getBillIdInDepositBank());
    }


    public void addSharedKey(Customer customer){
        sharedCustomerKey.put(customer.getId(), customer.getSecretKeySharedWithDepositBank());
    }

    public void addMoneyToBill(Customer customer, String money){
        Integer currentBill = Integer.parseInt(customerMoney.get(customer.getId()));
        currentBill += Integer.parseInt(money);
        customerMoney.put(customer.getId(), currentBill.toString());
    }

    public void getMoneyFromBill(Customer customer, String money){
        Integer currentBill = Integer.parseInt(customerMoney.get(customer.getId()));
        currentBill -= Integer.parseInt(money);
        if (currentBill < 0){
            System.out.println("Недостаточно средств!");
        }else {
            customerMoney.put(customer.getId(), currentBill.toString());
        }
    }

    public void addNewBill(Customer customer){
        customerMoney.put(customer.getId(), "0");
    }

    public Map<String, String> getCustomerMoney() {
        return customerMoney;
    }
}
