package com.ubi.alonso.givemesomespace;

/**
 * Created by jameschow on 4/20/16.
 */
public class BuildingData {
    private String timeStamp;
    private String buildingName;
    private long rating;

    BuildingData (long time, String name, long rate) {
        this.buildingName = name;
        this.rating = rate+1;
        this.timeStamp = Long.toString(time);
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
