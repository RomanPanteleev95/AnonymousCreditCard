package entities.banks;

import java.util.HashMap;
import java.util.Map;

public abstract class Bank {
    protected String name;
    protected String privateKey;
    protected String sharedKeyWithIntermediary;
    protected Map<String, String> customersBill = new HashMap<>();
    protected Map<String, String> sharedCustomerKey = new HashMap<>();

    public Bank(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSharedKeyWithIntermediary() {
        return sharedKeyWithIntermediary;
    }

    public void setSharedKeyWithIntermediary(String sharedKeyWithIntermediary) {
        this.sharedKeyWithIntermediary = sharedKeyWithIntermediary;
    }

}
