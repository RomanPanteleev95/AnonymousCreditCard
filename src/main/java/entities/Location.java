package entities;

public class Location {
    private String locationName;
    private String sharedKeyWithIntermediary;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getSharedKeyWithIntermediary() {
        return sharedKeyWithIntermediary;
    }

    public void setSharedKeyWithIntermediary(String sharedKeyWithIntermediary) {
        this.sharedKeyWithIntermediary = sharedKeyWithIntermediary;
    }
}
