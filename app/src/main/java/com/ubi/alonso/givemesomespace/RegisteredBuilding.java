package com.ubi.alonso.givemesomespace;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ben on 5/1/16.
 */
public class RegisteredBuilding {
    public LatLng location;
    public String name;
    public int rating;
    public int hitcount;
    public Date lasthit;
    public RegisteredBuilding(LatLng loc, String name)
    {
        this.location=loc;
        this.name = name;
    }

    public Integer computeRating()
    {
        if (hitcount > 0)
            return (rating / hitcount);
        else
            return rating;
    }


    static ArrayList<RegisteredBuilding> getallBuildings()
    {
        ArrayList<RegisteredBuilding> ar = new ArrayList<>();
        ar.add(0, new RegisteredBuilding(new LatLng(42.27468,-71.80839),"Campus Center"));
        ar.add(1, new RegisteredBuilding(new LatLng(42.274232,-71.806297),"Library"));
        ar.add(1,new RegisteredBuilding( new LatLng(42.273932,-71.807649),"Stratton Hall"));


        return ar;
    }


}


