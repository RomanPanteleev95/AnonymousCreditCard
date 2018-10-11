package entities.banks;

import entities.Customer;
import entities.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class CreditCardBank extends Bank{
    private Map<String, DoubleBlock> customerBlockInDepositBank = new HashMap<>();

    public CreditCardBank(String id) {
        super(id);
    }

    public void addDepositBanckDoubleBlock(Bank bank, DoubleBlock doubleBlock){
        customerBlockInDepositBank.put(bank.getId(), doubleBlock);
    }

    public void addCustomerBill(Customer customer){
        customersBill.put(customer.getId(), customer.getBillIdInCreditCardBank());
    }

    public void addSharedKey(Customer customer){
        sharedCustomerKey.put(customer.getId(), customer.getSecretKeySharedWithCreditCardBank());
    }
}
