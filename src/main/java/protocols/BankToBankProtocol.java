package protocols;

import entities.DoubleBlock;
import entities.InnerBlock;
import entities.Intermediary;
import entities.Message;
import entities.banks.Bank;
import utils.Utils;

public class BankToBankProtocol {
    private static final Intermediary intermediary = Intermediary.getIntermediary();
    private Bank sourceBank;
    private Bank destinationBank;
    private Message message;
    private String messageHeader;

    public BankToBankProtocol(Bank sourceBank, Bank destinationBank, Message message) {
        this.sourceBank = sourceBank;
        this.destinationBank = destinationBank;
        this.message = message;
        this.messageHeader = sourceBank.getId();
    }

    public void runProtocol(){

        //encryption by source banck
        DoubleBlock destinationDoubleBlock = intermediary.getDoubleBlock(destinationBank.getId());
        message.setBody(Utils.encryptString(message.getBody(), sourceBank.getSharedKeyWithIntermediary()));
        DoubleBlock encryptionDoubleBlock = encryptDoubleBlock(destinationDoubleBlock);

        //decryptionByIntermediary
        String sharedKeyWithSourceBank = intermediary.getSharedKey(messageHeader);
        DoubleBlock decryptionDoubleBlock = decryptDoubleBlock(encryptionDoubleBlock, sharedKeyWithSourceBank);
        message.setBody(Utils.decryptString(message.getBody(),sharedKeyWithSourceBank));
        String decryptionDestanationBankId = Utils.decryptString(decryptionDoubleBlock.getBankId(), intermediary.getPrivateKey());
        InnerBlock innerBlock = decryptionDoubleBlock.getInnerBlock();
        innerBlock.setEncryptInformation(Utils.decryptString(innerBlock.getEncryptInformation(), intermediary.getPrivateKey()));
        String sharedKeyWithDestinationBank = intermediary.getSharedKey(decryptionDestanationBankId);
        message.setBody(Utils.encryptString(message.getBody(), sharedKeyWithDestinationBank));
        innerBlock.setEncryptInformation(Utils.encryptString(innerBlock.getEncryptInformation(), sharedKeyWithDestinationBank));



    }


    private DoubleBlock encryptDoubleBlock(DoubleBlock doubleBlock){
        String encryptionBankIdFromDoubleBlock = Utils.encryptString(sourceBank.getSharedKeyWithIntermediary(), doubleBlock.getBankId());
        InnerBlock innerBlock = doubleBlock.getInnerBlock();
        innerBlock.setEncryptInformation(Utils.encryptString(sourceBank.getSharedKeyWithIntermediary(), innerBlock.getEncryptInformation()));
        return new DoubleBlock(encryptionBankIdFromDoubleBlock, innerBlock);
    }

    private DoubleBlock decryptDoubleBlock(DoubleBlock doubleBlock, String sharedKeyWithSourceBank){
        String decriptionBankIdFromDoubleBlock = Utils.decryptString(sharedKeyWithSourceBank, doubleBlock.getBankId());
        InnerBlock innerBlock = doubleBlock.getInnerBlock();
        innerBlock.setEncryptInformation(Utils.decryptString(sharedKeyWithSourceBank, innerBlock.getEncryptInformation()));
        return new DoubleBlock(decriptionBankIdFromDoubleBlock, innerBlock);
    }
}
