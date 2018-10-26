package protocols;

import entities.Customer;
import entities.banks.DepositBank;
import entities.blocks.DoubleBlock;
import entities.blocks.InnerBlock;
import entities.Intermediary;
import entities.Message;
import entities.banks.Bank;
import utils.Utils;

public class BankToBankProtocol {
    private static final Intermediary INTERMEDIARY = Intermediary.getIntermediary();
    private Bank sourceBank;
    private DoubleBlock destinationBankDoubleBlock;
    private Message message;
    private String messageHeader;
    private Bank destinationBank;
    private Customer customer;

    public BankToBankProtocol(Bank sourceBank, DoubleBlock destinationBankDoubleBlock, Message message, Bank destinationBank) {
        this.sourceBank = sourceBank;
        this.destinationBankDoubleBlock = destinationBankDoubleBlock;
        this.message = message;
        this.messageHeader = sourceBank.getName();
        this.destinationBank = destinationBank;
    }

    public BankToBankProtocol(Customer customer, Bank sourceBank, Bank destinationBank, Message message){
        //TODO: add validation
        DoubleBlock destinationDoubleBlock = INTERMEDIARY.getDoubleBlock(destinationBank.getName());
        this.customer = customer;
        this.sourceBank = sourceBank;
        this.destinationBank = destinationBank;
        this.destinationBankDoubleBlock = destinationDoubleBlock;
        this.message = message;
        this.messageHeader = sourceBank.getName();
    }

    public void runProtocol(){

        //encryption by source banck
        message.setBody(Utils.encryptString(message.getBody(), sourceBank.getSharedKeyWithIntermediary()));
        DoubleBlock encryptionDoubleBlock = Utils.encryptDoubleBlock(destinationBankDoubleBlock, sourceBank.getSharedKeyWithIntermediary());

        //decryptionByIntermediary
        String sharedKeyWithSourceBank = INTERMEDIARY.getBankSharedKey(messageHeader);
        DoubleBlock decryptionDoubleBlock = Utils.decryptDoubleBlock(encryptionDoubleBlock, sharedKeyWithSourceBank);
        message.setBody(Utils.decryptString(message.getBody(),sharedKeyWithSourceBank));
        String decryptionDestanationBankId = Utils.decryptString(decryptionDoubleBlock.getBankId(), INTERMEDIARY.getPrivateKey());
        InnerBlock innerBlock = decryptionDoubleBlock.getInnerBlock();
        InnerBlock tmpInnerBlock = new InnerBlock(Utils.decryptString(innerBlock.getInformation(), INTERMEDIARY.getPrivateKey()));



        String sharedKeyWithDestinationBank = INTERMEDIARY.getBankSharedKey(decryptionDestanationBankId);
        message.setBody(Utils.encryptString(message.getBody(), sharedKeyWithDestinationBank));
        tmpInnerBlock.setInformation(Utils.encryptString(tmpInnerBlock.getInformation(), sharedKeyWithDestinationBank));

        message.setBody(Utils.decryptString(message.getBody(), sharedKeyWithDestinationBank));
        tmpInnerBlock.setInformation(Utils.decryptString(tmpInnerBlock.getInformation(), sharedKeyWithDestinationBank));
        tmpInnerBlock.setInformation(Utils.decryptString(tmpInnerBlock.getInformation(), destinationBank.getPrivateKey()));
        DepositBank depositBank = (DepositBank) destinationBank;
        depositBank.addMoneyToBill(customer, message.getBody());

        System.out.println(message.getBody() + "$ на счет клиента " + tmpInnerBlock.getInformation());
    }

}
