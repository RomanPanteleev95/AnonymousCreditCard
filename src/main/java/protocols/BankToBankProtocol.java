package protocols;

import entities.Customer;
import entities.banks.DepositBank;
import entities.blocks.DoubleBlock;
import entities.blocks.InnerBlock;
import entities.Intermediary;
import entities.Message;
import entities.banks.Bank;
import utils.DataBaseUtils;
import utils.Utils;

import java.sql.SQLException;

public class BankToBankProtocol {
    private static final Intermediary INTERMEDIARY = Intermediary.getIntermediary();
    private Bank sourceBank;
    private DoubleBlock destinationBankDoubleBlock;
    private Message message;
    private String messageHeader;
    private Customer customer;

    public BankToBankProtocol(Bank sourceBank, DoubleBlock destinationBankDoubleBlock, Message message) {
        //TODO: add validation
        this.sourceBank = sourceBank;
        this.destinationBankDoubleBlock = destinationBankDoubleBlock;
        this.message = message;
        this.messageHeader = sourceBank.getName();
    }

    public void runProtocol() throws SQLException, ClassNotFoundException {
        //1. Source bank encrypt DLB + M
        message.setBody(Utils.encryptString(message.getBody(), sourceBank.getSharedKeyWithIntermediary()));
        DoubleBlock encryptionDoubleBlock = Utils.encryptDoubleBlock(destinationBankDoubleBlock, sourceBank.getSharedKeyWithIntermediary());

        //2. INTERMEDIARY decrypt DLB + M
        String sharedKeyWithSourceBank = INTERMEDIARY.getBankSharedKey(messageHeader);
        DoubleBlock decryptionDoubleBlock = Utils.decryptDoubleBlock(encryptionDoubleBlock, sharedKeyWithSourceBank);
        message.setBody(Utils.decryptString(message.getBody(),sharedKeyWithSourceBank));

        //3. INTERMEDIARY decrypt DLB
        String decryptionDestanationBankName = Utils.decryptString(decryptionDoubleBlock.getBankName(), INTERMEDIARY.getPrivateKey());
        InnerBlock innerBlock = decryptionDoubleBlock.getInnerBlock();
        InnerBlock tmpInnerBlock = new InnerBlock(Utils.decryptString(innerBlock.getInformation(), INTERMEDIARY.getPrivateKey()));

        //4. INTERMEDIARY encrypt M + inner block
        String sharedKeyWithDestinationBank = INTERMEDIARY.getBankSharedKey(decryptionDestanationBankName);
        message.setBody(Utils.encryptString(message.getBody(), sharedKeyWithDestinationBank));
        tmpInnerBlock.setInformation(Utils.encryptString(tmpInnerBlock.getInformation(), sharedKeyWithDestinationBank));

        //5. Destanation bank decrypt customerAccount + M
        Bank destanationBank = DataBaseUtils.getBankByName(decryptionDestanationBankName);
        message.setBody(Utils.decryptString(message.getBody(), destanationBank.getSharedKeyWithIntermediary()));
        tmpInnerBlock.setInformation(Utils.decryptString(tmpInnerBlock.getInformation(), destanationBank.getSharedKeyWithIntermediary()));
        String customerAccountId = Utils.decryptString(tmpInnerBlock.getInformation(), destanationBank.getPrivateKey());
        DataBaseUtils.updateMoneyOnCustomerAccount(customerAccountId);
    }

}
