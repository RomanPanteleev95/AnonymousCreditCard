package entities.blocks;

import java.util.Objects;

public class InnerBlock {
    private String information;

    public InnerBlock(String information) {
        this.information = information;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InnerBlock that = (InnerBlock) o;
        return Objects.equals(information, that.information);
    }

    @Override
    public int hashCode() {
        return Objects.hash(information);
    }
}
