package protocols;

import entities.Customer;
import entities.Intermediary;
import entities.Location;
import entities.Message;
import entities.banks.Bank;
import entities.blocks.DoubleBlock;
import entities.blocks.InnerBlock;
import utils.Utils;

public class CustomerToBankProtocol {
    private static final Intermediary INTERMEDIARY = Intermediary.getIntermediary();
    private Location location;
    private Bank depositBank;
    private Customer customer;
    private Message message;

    public CustomerToBankProtocol(Location location, Bank depositBank, Customer customer, Message message) {
        this.location = location;
        this.depositBank = depositBank;
        this.customer = customer;
        this.message = message;
    }

    public void runProtocol(){
        //customer encrypt message $
        message.setBody(Utils.encryptString(message.getBody(), customer.getSecretKeySharedWithDepositBank()));

        //market encrypt DB + message
        DoubleBlock depositBankDoubleBlock = INTERMEDIARY.getDoubleBlock(depositBank.getId());
        DoubleBlock encryptionDoubleBlock = Utils.encryptDoubleBlock(depositBankDoubleBlock, location.getSharedKeyWithIntermediary());
        message.setBody(Utils.encryptString(message.getBody(), location.getSharedKeyWithIntermediary()));

        //intermed decrypt DB + message
        String sharedKeyWithLocation = INTERMEDIARY.getLocationSharedKey(location.getLocationId());
        message.setBody(Utils.decryptString(message.getBody(), sharedKeyWithLocation));
        DoubleBlock decriptionDoubleBlock = Utils.decryptDoubleBlock(encryptionDoubleBlock, sharedKeyWithLocation);

        //intermed decrypt DB
        String decriptionBankId = Utils.decryptString(decriptionDoubleBlock.getBankId(), INTERMEDIARY.getPrivateKey());
        InnerBlock innerBlock = decriptionDoubleBlock.getInnerBlock();
        innerBlock.setEncryptInformation(Utils.decryptString(innerBlock.getEncryptInformation(), INTERMEDIARY.getPrivateKey()));

        String sharedKeyWithDepositBank = INTERMEDIARY.getBankSharedKey(decriptionBankId);
        message.setBody(Utils.encryptString(message.getBody(), sharedKeyWithDepositBank));
        innerBlock.setEncryptInformation(Utils.encryptString(innerBlock.getEncryptInformation(), sharedKeyWithDepositBank));

        //deposit bank decryptMes
        message.setBody(Utils.decryptString(message.getBody(), depositBank.getSharedKeyWithIntermediary()));
        innerBlock.setEncryptInformation(Utils.decryptString(innerBlock.getEncryptInformation(), depositBank.getSharedKeyWithIntermediary()));
        innerBlock.setEncryptInformation(Utils.decryptString(innerBlock.getEncryptInformation(), depositBank.getPrivateKey()));

        message.setBody(Utils.decryptString(message.getBody(), customer.getSecretKeySharedWithDepositBank()));

        System.out.println("Клиент " + innerBlock.getEncryptInformation() + " запросил " + message.getBody() + "$");
    }

}