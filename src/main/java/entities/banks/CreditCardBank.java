package entities.banks;

import entities.Customer;
import entities.blocks.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class CreditCardBank extends Bank{

    public CreditCardBank(int id, String name) {
        super(id, name);
    }

    public CreditCardBank(){

    }

    public void addSharedKey(Customer customer){
        sharedCustomerKey.put(customer.getName(), customer.getSecretKeySharedWithCreditCardBank());
    }
}
