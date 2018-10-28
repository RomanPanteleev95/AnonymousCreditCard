package entities.banks;

import entities.blocks.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    protected int id;
    protected String name;
    protected String type;
    protected String privateKey;
    protected String sharedKeyWithIntermediary;
    protected Map<String, String> sharedCustomerKey = new HashMap<>();
    protected Map<Integer, DoubleBlock> alliesDoubleBlocks = new HashMap<>();

    public Map<Integer, DoubleBlock> getAlliesDoubleBlocks() {
        return alliesDoubleBlocks;
    }

    public void setAlliesDoubleBlocks(Map<Integer, DoubleBlock> alliesDoubleBlocks) {
        this.alliesDoubleBlocks = alliesDoubleBlocks;
    }

    public Bank(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Bank(){

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Map<String, String> getSharedCustomerKey() {
        return sharedCustomerKey;
    }

    public void setSharedCustomerKey(Map<String, String> sharedCustomerKey) {
        this.sharedCustomerKey = sharedCustomerKey;
    }
}
