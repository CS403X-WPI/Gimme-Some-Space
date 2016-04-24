package com.example.jameschow.webdbapp;

/**
 * Created by jameschow on 4/20/16.
 */
public class BuildingData {
    private String timeStamp;
    private String buildingName;
    private int rating;

    BuildingData (String time, String name, int rate) {
        this.buildingName = name;
        this.rating = rate;
        this.timeStamp = time;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
