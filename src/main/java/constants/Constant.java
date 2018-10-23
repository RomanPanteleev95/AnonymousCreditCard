package constants;

public interface Constant {

    String REPLENISH_BANK_BILL = "replanishBanckBill";
    String EXIT = "exit";

    interface Registration{
        String REGISTRATION_CUSTOMER = "regestrationCustomer";
        String REGISTRATION_DEPOSIT_BANK = "registrationDepositBank";
        String REGISTRATION_CREDIT_CARD_BANK = "registrationCreditCardBank";
    }

    interface Create{
        String CREATE_CUSTOMER = "createCustomer";
        String CREATE_DEPOSIT_BANK = "createDepositBank";
        String CREATE_CREDIT_CARD_BANK = "createCreditCardBank";
    }

    interface MenuMessage{
        String ENTER_CLIENT_NAME = "Введите имя клиента: ";
        String ENTER_BANK_NAME = "Введите название банка: ";
        String ENTER_SUM = "Введите сумму: ";
    }

    interface SqlQuery{
        String SELECT_ALL_BANKS = "select * from banks";
    }
}
