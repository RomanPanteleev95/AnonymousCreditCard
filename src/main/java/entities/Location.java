package entities;

public class Location {
    private int locationId;
    private String locationName;
    private String sharedKeyWithIntermediary;
    private int depositBankId;
    private String accountIdInDepositBank;
    private String secretKeySharedWithDepositBank;

    public Location(int locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getSharedKeyWithIntermediary() {
        return sharedKeyWithIntermediary;
    }

    public void setSharedKeyWithIntermediary(String sharedKeyWithIntermediary) {
        this.sharedKeyWithIntermediary = sharedKeyWithIntermediary;
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
}
