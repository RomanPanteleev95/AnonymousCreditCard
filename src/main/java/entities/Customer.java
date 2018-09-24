package entities;

public class Customer {
    private int id;
    private String billId;

    public Customer(int id, String billId) {
        this.id = id;
        this.billId = billId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }
}
