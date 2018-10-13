package protocols;

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

    public BankToBankProtocol(Bank sourceBank, DoubleBlock destinationBankDoubleBlock, Message message, Bank destinationBank) {
        this.sourceBank = sourceBank;
        this.destinationBankDoubleBlock = destinationBankDoubleBlock;
        this.message = message;
        this.messageHeader = sourceBank.getId();
        this.destinationBank = destinationBank;
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
        InnerBlock tmpInnerBlock = new InnerBlock(Utils.decryptString(innerBlock.getEncryptInformation(), INTERMEDIARY.getPrivateKey()));



        String sharedKeyWithDestinationBank = INTERMEDIARY.getBankSharedKey(decryptionDestanationBankId);
        message.setBody(Utils.encryptString(message.getBody(), sharedKeyWithDestinationBank));
        tmpInnerBlock.setEncryptInformation(Utils.encryptString(tmpInnerBlock.getEncryptInformation(), sharedKeyWithDestinationBank));

        message.setBody(Utils.decryptString(message.getBody(), sharedKeyWithDestinationBank));
        tmpInnerBlock.setEncryptInformation(Utils.decryptString(tmpInnerBlock.getEncryptInformation(), sharedKeyWithDestinationBank));
        tmpInnerBlock.setEncryptInformation(Utils.decryptString(tmpInnerBlock.getEncryptInformation(), destinationBank.getPrivateKey()));

        System.out.println(message.getBody() + "$ на счет клиента " + tmpInnerBlock.getEncryptInformation());
    }



}
