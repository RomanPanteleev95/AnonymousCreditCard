package entities.banks;

import entities.Customer;
import entities.blocks.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class CreditCardBank extends Bank{
    private Map<String, DoubleBlock> customerBlockInDepositBank = new HashMap<>();

    public CreditCardBank(String name) {
        super(name);
    }

    public void addDepositBanckDoubleBlock(Bank bank, DoubleBlock doubleBlock){
        customerBlockInDepositBank.put(bank.getName(), doubleBlock);
    }

    public void addCustomerBill(Customer customer){
        customersBill.put(customer.getName(), customer.getBillIdInCreditCardBank());
    }

    public void addSharedKey(Customer customer){
        sharedCustomerKey.put(customer.getName(), customer.getSecretKeySharedWithCreditCardBank());
    }
}
