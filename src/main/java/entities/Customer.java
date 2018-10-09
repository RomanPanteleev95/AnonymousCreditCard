package entities;

public class Customer {
    private String id;
    private String billIdInDepositBank;
    private String billIdInCreditCardBank;
    private String secretKeySharedWithDepositBank;
    private String secretKeySharedWithCreditCardBank;

    public Customer(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
