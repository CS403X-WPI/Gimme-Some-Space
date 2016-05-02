package com.ubi.alonso.givemesomespace;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by ben on 5/1/16.
 */
public class RegisteredBuilding {
    public LatLng location;
    public String name;
    public RegisteredBuilding(LatLng loc, String name)
    {
        this.location=loc;
        this.name = name;
    }

    static ArrayList<RegisteredBuilding> getallBuildings()
    {
        ArrayList<RegisteredBuilding> ar = new ArrayList<>();
        ar.add(0, new RegisteredBuilding(new LatLng(42.27468,-71.80839),"Campus Center"));
        ar.add(1, new RegisteredBuilding(new LatLng(42.274232,-71.806297),"Library"));


        return ar;
    }


}


