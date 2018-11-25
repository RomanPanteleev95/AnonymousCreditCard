package entities;

import utils.DataBaseUtils;

import java.sql.SQLException;

public class Location {
    private int locationId;
    private String locationName;
    private int depositBankId;
    private String accountIdInDepositBank;

    public Location(int locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getSharedKeyWithIntermediary() throws SQLException, ClassNotFoundException {
        return DataBaseUtils.getLocationKeySharedWithIntermediary(this.locationId);
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
}
