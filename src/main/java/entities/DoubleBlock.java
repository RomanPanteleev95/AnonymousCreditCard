package entities;

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
}
