package entities.banks;

import entities.blocks.InnerBlock;

import java.util.HashMap;
import java.util.Map;

public abstract class Bank {
    protected String id;
    protected String privateKey;
    protected String sharedKeyWithIntermediary;
    protected Map<String, String> customersBill = new HashMap<>();
    protected Map<String, String> sharedCustomerKey = new HashMap<>();

    public String encryptInnerBlock(InnerBlock innerBlock){
        return null;
    }

    public Bank(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
