package constants;

public interface Constant {
    interface SqlQuery{
        String SELECT_ALL_BANKS = "select * from banks";
        String SELCT_USER_PASSWORD_BY_NAME = "select customer_password from customer where name = ?";

    }
}
