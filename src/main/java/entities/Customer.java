package entities;

public class Customer {
    private int id;
    private String name;
    private String customerPassword;
    private int creditBankId;
    private String accountIdInCreditCardBank;
    private int depositBankId;
    private String accountIdInDepositBank;
    private String email;

    public Customer(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public int getCreditBankId() {
        return creditBankId;
    }

    public void setCreditBankId(int creditBankId) {
        this.creditBankId = creditBankId;
    }

    public void setAccountIdInCreditCardBank(String accountIdInCreditCardBank) {
        this.accountIdInCreditCardBank = accountIdInCreditCardBank;
    }

    public int getDepositBankId() {
        return depositBankId;
    }

    public void setDepositBankId(int depositBankId) {
        this.depositBankId = depositBankId;
    }

    public void setAccountIdInDepositBank(String accountIdInDepositBank) {
        this.accountIdInDepositBank = accountIdInDepositBank;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
