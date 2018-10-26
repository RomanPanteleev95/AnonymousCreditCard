package entities.banks;

import entities.Customer;
import entities.blocks.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class CreditCardBank extends Bank{
    private Map<String, DoubleBlock> customerBlockInDepositBank = new HashMap<>();

    public CreditCardBank(int id, String name) {
        super(id, name);
    }

    public CreditCardBank(){

    }

    public void addDepositBanckDoubleBlock(Bank bank, DoubleBlock doubleBlock){
        customerBlockInDepositBank.put(bank.getName(), doubleBlock);
    }

    public void addSharedKey(Customer customer){
        sharedCustomerKey.put(customer.getName(), customer.getSecretKeySharedWithCreditCardBank());
    }
}
