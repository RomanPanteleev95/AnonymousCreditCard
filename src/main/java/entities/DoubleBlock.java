package entities;

import java.util.Objects;

public class DoubleBlock{
    private String bankId;
    private InnerBlock innerBlock;

    public DoubleBlock(String bankId, InnerBlock innerBlock) {
        this.bankId = bankId;
        this.innerBlock = innerBlock;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public InnerBlock getInnerBlock() {
        return innerBlock;
    }

    public void setInnerBlock(InnerBlock innerBlock) {
        this.innerBlock = innerBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleBlock that = (DoubleBlock) o;
        return Objects.equals(bankId, that.bankId) &&
                Objects.equals(innerBlock, that.innerBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankId, innerBlock);
    }
}
