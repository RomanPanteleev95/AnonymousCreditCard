package constants;

public interface Constant {
    interface SqlQuery{
        String SELECT_ALL_BANKS = "select * from banks";
        String SELECT_BANK_BY_NAME = "select * from banks where name = ?";
        String SELCT_USER_PASSWORD_BY_NAME = "select * from customers where name = ?";
        String INSERT_NEW_CUSTOMER = "insert into customers  (\"name\", \"customer_password\") values (?, ?)";
        String REGISTRATION_CUSTOMER_IN_BANK = "update customers set credit_bank_id = ?, account_id_in_credit_bank = ?, " +
                "shared_key_with_credit_bank = ?, deposit_bank_id = ?, account_id_in_deposit_bank = ?, shared_key_with_deposit_bank = ?, current_money = ? where name = ?";
        String GET_ALL_DEPOSIT_BANKS = "select * from banks where type like 'Deposit'";
        String GET_ALL_CREDIT_BANKS = "select * from banks where type like 'Credit'";

    }
}
