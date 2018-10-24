package entities;

public class Customer {
    private String name;
    private String billIdInDepositBank;
    private String billIdInCreditCardBank;
    private String secretKeySharedWithDepositBank;
    private String secretKeySharedWithCreditCardBank;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getBillIdInDepositBank() {
        return billIdInDepositBank;
    }

    public void setBillIdInDepositBank(String billIdInDepositBank) {
        this.billIdInDepositBank = billIdInDepositBank;
    }

    public String getBillIdInCreditCardBank() {
        return billIdInCreditCardBank;
    }

    public void setBillIdInCreditCardBank(String billIdInCreditCardBank) {
        this.billIdInCreditCardBank = billIdInCreditCardBank;
    }

    public String getSecretKeySharedWithDepositBank() {
        return secretKeySharedWithDepositBank;
    }

    public void setSecretKeySharedWithDepositBank(String secretKeySharedWithDepositBank) {
        this.secretKeySharedWithDepositBank = secretKeySharedWithDepositBank;
    }

    public String getSecretKeySharedWithCreditCardBank() {
        return secretKeySharedWithCreditCardBank;
    }

    public void setSecretKeySharedWithCreditCardBank(String secretKeySharedWithCreditCardBank) {
        this.secretKeySharedWithCreditCardBank = secretKeySharedWithCreditCardBank;
    }
}
