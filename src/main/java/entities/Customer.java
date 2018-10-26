package entities;

public class Customer {
    private int id;
    private String name;
    private String customerPassword;
    private int creditBankId;
    private String accountIdInCreditCardBank;
    private String secretKeySharedWithCreditCardBank;
    private int depositBankId;
    private String accountIdInDepositBank;
    private String secretKeySharedWithDepositBank;
    private String email;

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public String getAccountIdInCreditCardBank() {
        return accountIdInCreditCardBank;
    }

    public void setAccountIdInCreditCardBank(String accountIdInCreditCardBank) {
        this.accountIdInCreditCardBank = accountIdInCreditCardBank;
    }

    public String getSecretKeySharedWithCreditCardBank() {
        return secretKeySharedWithCreditCardBank;
    }

    public void setSecretKeySharedWithCreditCardBank(String secretKeySharedWithCreditCardBank) {
        this.secretKeySharedWithCreditCardBank = secretKeySharedWithCreditCardBank;
    }

    public int getDepositBankId() {
        return depositBankId;
    }

    public void setDepositBankId(int depositBankId) {
        this.depositBankId = depositBankId;
    }

    public String getAccountIdInDepositBank() {
        return accountIdInDepositBank;
    }

    public void setAccountIdInDepositBank(String accountIdInDepositBank) {
        this.accountIdInDepositBank = accountIdInDepositBank;
    }

    public String getSecretKeySharedWithDepositBank() {
        return secretKeySharedWithDepositBank;
    }

    public void setSecretKeySharedWithDepositBank(String secretKeySharedWithDepositBank) {
        this.secretKeySharedWithDepositBank = secretKeySharedWithDepositBank;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
