import constants.Constant;
import db.DataBaseAnalog;
import entities.Customer;
import entities.Intermediary;
import entities.banks.Bank;
import entities.banks.CreditCardBank;
import entities.banks.DepositBank;
import utils.Utils;

import java.util.Scanner;

public class Main {

    private static final DataBaseAnalog dataBaseAnalog = DataBaseAnalog.getDataBaseAnalog();
    private static final Intermediary INTERMEDIARY = Intermediary.getIntermediary();
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String operation = "";
       while (!operation.equals(Constant.EXIT)){
           if (operation.equals(Constant.Create.CREATE_CUSTOMER)){
               System.out.println("Введите имя клиента:");
               String customerName = scan.nextLine();
               Utils.createCustomer(customerName);
           }
           if (operation.equals(Constant.Create.CREATE_DEPOSIT_BANK)){
               System.out.println("Введите название банка:");
               String depositBankName = scan.nextLine();
               Utils.createDepositBank(depositBankName);
           }
           if (operation.equals(Constant.Create.CREATE_CREDIT_CARD_BANK)){
               System.out.println("Введите название банка:");
               String createCreditCardBankName = scan.nextLine();
               Utils.createCreditCardBank(createCreditCardBankName);
           }
           if (operation.equals(Constant.Registration.REGISTRATION_DEPOSIT_BANK)){
               System.out.println("Введите название банка:");
               String bankName = scan.nextLine();
               Utils.registrationBank(dataBaseAnalog.getDepositBank(bankName));
           }
           if (operation.equals(Constant.Registration.REGISTRATION_CREDIT_CARD_BANK)){
               System.out.println("Введите название банка:");
               String bankName = scan.nextLine();
               Utils.registrationBank(dataBaseAnalog.getCreditCardBank(bankName));
           }
           if (operation.equals(Constant.Registration.REGISTRATION_CUSTOMER)){
               System.out.println("Введите название банка:");
               String customerName = scan.nextLine();
               String depositBankName = scan.nextLine();
               String creditCardBankName = scan.nextLine();
               Customer customer = dataBaseAnalog.getCustomer(customerName);
               DepositBank depositBank = (DepositBank) dataBaseAnalog.getDepositBank(depositBankName);
               CreditCardBank creditCardBank = (CreditCardBank) dataBaseAnalog.getCreditCardBank(creditCardBankName);
               Utils.registrationCustomer(customer, creditCardBank, depositBank);
           }
           if (operation.equals(Constant.REPLENISH_BANK_BILL)){
               System.out.println("Введите имя клиента:");
               String customerName = scan.nextLine();
               Customer customer = dataBaseAnalog.getCustomer(customerName);
               System.out.println("Введите название банка:");
               String banckName = scan.nextLine();
               DepositBank bank = (DepositBank) dataBaseAnalog.getDepositBank(banckName);
               System.out.println("Введите сумму:");
               String money = scan.nextLine();
               bank.addMoneyToBill(customer, money);

           }
           operation = scan.nextLine();
       }
    }

}
