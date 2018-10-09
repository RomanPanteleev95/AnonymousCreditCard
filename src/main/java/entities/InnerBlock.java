package entities;

import java.io.Serializable;

public class InnerBlock {
    private String encryptInformation;

    public InnerBlock(String encryptInformation) {
        this.encryptInformation = encryptInformation;
    }

    public String getEncryptInformation() {
        return encryptInformation;
    }

    public void setEncryptInformation(String encryptInformation) {
        this.encryptInformation = encryptInformation;
    }
}
