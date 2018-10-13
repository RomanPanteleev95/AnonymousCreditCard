package entities.blocks;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerBlock that = (InnerBlock) o;
        return Objects.equals(encryptInformation, that.encryptInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encryptInformation);
    }
}
