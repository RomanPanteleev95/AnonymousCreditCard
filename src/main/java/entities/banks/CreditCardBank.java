package entities.banks;

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
}
