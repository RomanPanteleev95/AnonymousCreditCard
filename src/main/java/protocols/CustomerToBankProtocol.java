package protocols;

import entities.Customer;
import entities.Intermediary;
import entities.Location;
import entities.Message;
import entities.banks.Bank;
import entities.blocks.DoubleBlock;
import entities.blocks.InnerBlock;
import utils.DataBaseUtils;
import utils.Utils;

import java.sql.SQLException;

public class CustomerToBankProtocol {
    private static Intermediary INTERMEDIARY = null;

    static {
        try {
            INTERMEDIARY = Intermediary.getIntermediary();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    public void runProtocol() throws SQLException, ClassNotFoundException {
        //customer encrypt message $
        message.setBody(Utils.encryptString(message.getBody(), customer.getSecretKeySharedWithDepositBank()));

        //market encrypt DB + message
        DoubleBlock depositBankDoubleBlock = DataBaseUtils.getCustomerDoubleBlockByBankId(customer.getId(), depositBank.getId());
        DoubleBlock encryptionDoubleBlock = Utils.encryptDoubleBlock(depositBankDoubleBlock, location.getSharedKeyWithIntermediary());
        message.setBody(Utils.encryptString(message.getBody(), location.getSharedKeyWithIntermediary()));

        //intermed decrypt DB + message
        String sharedKeyWithLocation = INTERMEDIARY.getLocationSharedKey(location.getLocationId());
        message.setBody(Utils.decryptString(message.getBody(), sharedKeyWithLocation));
        DoubleBlock decriptionDoubleBlock = Utils.decryptDoubleBlock(encryptionDoubleBlock, sharedKeyWithLocation);

        //intermed decrypt DB
        String decriptionBankId = Utils.decryptString(decriptionDoubleBlock.getBankName(), INTERMEDIARY.getPrivateKey());
        InnerBlock innerBlock = decriptionDoubleBlock.getInnerBlock();
        innerBlock.setInformation(Utils.decryptString(innerBlock.getInformation(), INTERMEDIARY.getPrivateKey()));

        String sharedKeyWithDepositBank = INTERMEDIARY.getBankSharedKey(decriptionBankId);
        message.setBody(Utils.encryptString(message.getBody(), sharedKeyWithDepositBank));
        innerBlock.setInformation(Utils.encryptString(innerBlock.getInformation(), sharedKeyWithDepositBank));

//        deposit bank decryptMes
        message.setBody(Utils.decryptString(message.getBody(), depositBank.getSharedKeyWithIntermediary()));
        innerBlock.setInformation(Utils.decryptString(innerBlock.getInformation(), depositBank.getSharedKeyWithIntermediary()));
        innerBlock.setInformation(Utils.decryptString(innerBlock.getInformation(), depositBank.getPrivateKey()));

        //TODO: get sharedKey from BANK DB
        message.setBody(Utils.decryptString(message.getBody(), customer.getSecretKeySharedWithDepositBank()));

        float money = Float.parseFloat(message.getBody());
        DataBaseUtils.removeMoneyFromCustomerAccount(innerBlock.getInformation(), money);
    }

}
