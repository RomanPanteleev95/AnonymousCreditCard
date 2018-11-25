package entities.blocks;

import java.util.Objects;

public class DoubleBlock{
    private String bankName;
    private InnerBlock innerBlock;

    public DoubleBlock(String bankName, InnerBlock innerBlock) {
        this.bankName = bankName;
        this.innerBlock = innerBlock;
    }

    public String getBankName() {
        return bankName;
    }

    public InnerBlock getInnerBlock() {
        return innerBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleBlock that = (DoubleBlock) o;
        return Objects.equals(bankName, that.bankName) &&
                Objects.equals(innerBlock, that.innerBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankName, innerBlock);
    }
}
