package constants;

public interface Constant {
    interface UiText{
        String MENU = "Меню";
        String SIGN_UP = "Зарегистрироваться";
        String SIGN_IN = "Войти";
        String DEPOSIT_BANK = "Депозитный банк";
        String CREDIT_BANK = "Кредитный банк";
        String NAME = "Имя";
        String EMAIL = "Email";
        String PASSWORD = "Пароль";
        String NOT_REGISTERED = "не зараегистрирован";
        String PERSONAL_AREA = "Личный кабинет: ";
        String REPLENISH_ACCOUNT = "Пополнить счет";
        String YOUR_CREDIT_BANK = "Ваш кредитный банк: ";
        String YOUR_DEPOSIT_BANK = "Ваш депозитный банк: ";
        String REGISTRATION_IN_BANK = "Зарегистрироваться в банках";
    }

    interface SqlQuery{
        String SELECT_ALL_BANKS = "select * from banks";
        String GET_BANK_BY_NAME = "select * from banks where name = ?";
        String SELCT_USER_PASSWORD_BY_NAME = "select * from customers where name = ?";
        String CREATE_NEW_CUSTOMER = "insert into customers  (\"name\", \"customer_password\", \"email\") values (?, ?, ?)";
        String REGISTRATION_CUSTOMER_IN_BANK = "update customers set credit_bank_id = ?, account_id_in_credit_bank = ?, shared_key_with_credit_bank = ?, deposit_bank_id = ?, account_id_in_deposit_bank = ?, shared_key_with_deposit_bank = ?, current_money = ? where name = ?";
        String GET_ALL_DEPOSIT_BANKS = "select * from banks where type like 'Deposit'";
        String GET_ALL_CREDIT_BANKS = "select * from banks where type like 'Credit'";
        String GET_BANK_BY_ID = "select * from banks where id = ?";
        String GET_CUSTOMER_BY_ID = "select * from customers where id = ?";
        String GET_CUSTOMER_BY_EMAIL = "select * from customers where email = ?";
        String GET_CUSTOMER_BY_NAME = "select * from customers where name = ?";

    }
}
