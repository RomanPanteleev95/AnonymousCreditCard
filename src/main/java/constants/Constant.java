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
        String PAY_FOR_PURCHASE = "Оплатить покупку";
        String CURRENT_BALANCE = "Ваш баланс: ";
    }

    interface SqlQuery{

        String CREATE_DOUBLE_BLOCK = "insert into double_blocks (\"customer_id\", \"bank_id\", \"encode_bank_name\", \"innner_box\") values (?,?,?,?)";
        String ADD_ALIES_DOUBLE_BLOCK = "insert into alies_double_blocks (\"bank_id\", \"alies_double_block_id\") values (?,?)";
        String GET_LAST_DOUBLE_BLOCK = "select * from double_blocks order by id desc limit 1";
        String GET_ALL_LOCATIONS = "select * from locations";
        String GET_CUSTOMER_DOUBLE_BLOCK_BY_BANK_ID = "select * from double_blocks where customer_id = ? and bank_id = ?";

        String UPDATE_MONEY_ON_ACCPUNT = "update account_money set current_money = ? where account_id = ?";
        String CREATE_ACOOUNT_MONEY = "insert into account_money (\"account_id\", \"current_money\") values (?, ?)";

        String CREATE_INTERMEDIARY_PRIVATE_KEY = "insert into intermediary_private_key (\"private_key\") values (?)";
        String GET_LOCATION_BY_NAME = "select * from locations where name = ?";
        String GET_LOCATION_DOUBLE_BLOCK_BY_BANK_ID = "select * from double_blocks_location where location_id = ? and bank_id = ?";
        String GET_CURRENT_BALANCE_BY_CUSTOMER_ID = "select current_money from account_money am, customers c where c.id = ? and c.account_id_in_deposit_bank = am.account_id";
        String ADD_COMMON_PARAMETR = "insert into common_parametrs (\"name\", \"value\") values (?, ?)";
        String GET_COMMON_PARAMETRS = "select * from common_parametrs";

        //Customer
        String CREATE_NEW_CUSTOMER = "insert into customers  (\"name\", \"customer_password\", \"email\") values (?, ?, ?)";
        String REGISTRATION_CUSTOMER_IN_BANK = "update customers set credit_bank_id = ?, account_id_in_credit_bank = ?, deposit_bank_id = ?, account_id_in_deposit_bank = ? where email = ?";
        String GET_CUSTOMER_BY_ID = "select * from customers where id = ?";
        String GET_CUSTOMER_BY_EMAIL = "select * from customers where email = ?";
        String GET_MONEY_FROM_CUSTOMER_ACCOUNT = "select * from account_money where account_id = ?";

        //Bank
        String GET_ALL_DEPOSIT_BANKS = "select * from banks where type like 'Депозитный банк'";
        String GET_ALL_CREDIT_BANKS = "select * from banks where type like 'Кредитный банк'";
        String GET_BANK_BY_ID = "select * from banks where id = ?";
        String GET_BANK_BY_NAME = "select * from banks where name = ?";
        String GET_BANKS_PRIVATE_KEY = "select * from banks_private_key where bank_id = ?";
        String GET_BANKS_KEY_SHARED_WITH_INTERMEDIARY = "select * from banks_shared_key_with_intermediary where bank_id = ?";
        String GET_BANKS_PUBLIC_KEY_FOR_RSA = "select * from banks_public_key_for_rsa where bank_id = ?";
        String GET_BANKS_PRIVATE_KEY_FOR_RSA = "select * from banks_private_key_for_rsa where bank_id = ?";
        String SET_SHARED_KEY_SITH_CUSTOMER = "insert into banks_shared_key_with_customer (\"bank_id\", \"customer_id\", \"shared_key\") values (?, ?, ?)";
        String SET_ENCRYPT_SHARED_KEY_WITH_CUSTOMER = "insert into banks_encrypt_shared_key_with_customer (\"bank_id\", \"customer_id\", \"encrypt_shared_key\") values (?, ?, ?)";
        String GET_SHARED_KEY_WITH_CUSTOMER = "select * from banks_shared_key_with_customer where bank_id = ? and customer_id = ?";

        //Intermediary
        String GET_INTEMEDIARY_PRIVATE_KEY = "select * from intermediary_private_key";
        String GET_INTERMEDIARY_KEY_SHARED_WITH_BANK = "select * from entermediary_banks_shared_key where bank_id = ?";
        String GET_INTERMEDIARY_KEY_SHARED_WITH_LOCATION = "select * from entermediary_locations_shared_key where location_id = ?";

        //Location
        String GET_LOCATION_KEY_SHARED_WITH_NTERMEDIARY = "select * from locations_shared_key_with_intermediary where location_id = ?";
    }
}
