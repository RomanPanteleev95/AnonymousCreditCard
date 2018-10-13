package entities;

public class Location {
    private String locationId;
    private String sharedKeyWithIntermediary;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSharedKeyWithIntermediary() {
        return sharedKeyWithIntermediary;
    }

    public void setSharedKeyWithIntermediary(String sharedKeyWithIntermediary) {
        this.sharedKeyWithIntermediary = sharedKeyWithIntermediary;
    }
}
