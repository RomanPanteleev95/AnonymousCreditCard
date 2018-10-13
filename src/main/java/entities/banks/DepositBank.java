package entities.banks;

import entities.Customer;
import entities.blocks.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class DepositBank extends Bank{

    private Map<String, DoubleBlock> customerBlockInCreditCardBank = new HashMap<>();

    /* map: customerId -> money */
    private Map<String, String> customerBill = new HashMap<>();

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
}
