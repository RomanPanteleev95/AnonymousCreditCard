package utils;

import entities.Customer;
import entities.DoubleBlock;
import entities.InnerBlock;
import entities.Intermediary;
import entities.banks.Bank;
import entities.banks.CreditCardBank;
import entities.banks.DepositBank;

public class Utils {
    public static DoubleBlock getDLB(Bank bank, String customerId, InnerBlock innerBlock, String intermediaryKey){
        String encryptBanckId = encryptString(bank.getId(), intermediaryKey);
        String encryptInnerBox = encryptString(innerBlock.getEncryptInformation(), intermediaryKey);
        innerBlock.setEncryptInformation(encryptBanckId);
        return new DoubleBlock(encryptBanckId, innerBlock);
    }

    public static InnerBlock getInnerBlock(String bankPrivateKey, String customerBillId){
        return new InnerBlock(encryptString(bankPrivateKey, customerBillId));
    }

    private static String encryptString(String bankPrivateKey, String customerBillId){
        char[] inf = customerBillId.toCharArray();
        for (int i = 0; i < Math.min(bankPrivateKey.length(), customerBillId.length()); i++){
            inf[i] = (char) (customerBillId.charAt(i) + bankPrivateKey.charAt(i));
        }

        return new String(inf);
    }

    private static String decryptString(String bankPrivateKey, String customerBillId){
        char[] inf = customerBillId.toCharArray();
        for (int i = 0; i < Math.min(bankPrivateKey.length(), customerBillId.length()); i++){
            inf[i] = (char) (customerBillId.charAt(i) - bankPrivateKey.charAt(i));
        }

        return new String(inf);
    }

    public static void registrationCustomer(Customer customer, Bank creditCardBank, Bank depositBank){
        String billIdInCreditCardCardBank = "billIdInCreditCardCardBank"; //random
        String billIdInCreditDepositBank = "billIdInCreditDepositBank"; //random

        customer.setBillIdInCreditCardBank(billIdInCreditCardCardBank);
        customer.setBillIdInDepositBank(billIdInCreditDepositBank);


        InnerBlock innerBlockForCreditBank = getInnerBlock(creditCardBank.getPrivateKey(), billIdInCreditCardCardBank);
        InnerBlock innerBlockForDepositBanck = getInnerBlock(depositBank.getPrivateKey(), billIdInCreditDepositBank);

        Intermediary intermediary = Intermediary.getIntermediary();
        DoubleBlock doubleBlockForCreditBanck = (DoubleBlock) getDLB(creditCardBank, customer.getId(), innerBlockForCreditBank, intermediary.getPrivateKey());
        DoubleBlock doubleBlockForDepositBanck = (DoubleBlock) getDLB(depositBank, customer.getId(), innerBlockForDepositBanck, intermediary.getPrivateKey());

        intermediary.addBankDoubleBlock(creditCardBank, doubleBlockForCreditBanck);
        intermediary.addBankDoubleBlock(depositBank, doubleBlockForDepositBanck);

        if (creditCardBank instanceof CreditCardBank){
            CreditCardBank cardBank = (CreditCardBank) creditCardBank;
            cardBank.addDepositBanckDoubleBlock(depositBank, doubleBlockForDepositBanck);
        }

        if (depositBank instanceof DepositBank){
            DepositBank depBanck = (DepositBank) depositBank;
            depBanck.addCreditBankDoubleBlock(creditCardBank, doubleBlockForCreditBanck);
        }
    }

    public static void registrationBank(Bank bank){
        String bankPrivateKey = "banckPrivateKey";
        bank.setPrivateKey(bankPrivateKey);
        String sharedKeyWithIntermediary = "sharedKeyWithIntermediary";
        Intermediary intermediary = Intermediary.getIntermediary();
        intermediary.addBanksSharedKey(bank, sharedKeyWithIntermediary);
        bank.setSharedKeyWithIntermediary(sharedKeyWithIntermediary);
    }

}
