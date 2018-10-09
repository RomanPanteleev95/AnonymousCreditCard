package entities.banks;

import entities.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class DepositBank extends Bank{

    private Map<String, DoubleBlock> customerBlockInCreditCardBank = new HashMap<>();

    public DepositBank(String id) {
        super(id);
    }

    public void addCreditBankDoubleBlock(Bank bank, DoubleBlock doubleBlock){
        customerBlockInCreditCardBank.put(bank.getId(), doubleBlock);
    }
}
