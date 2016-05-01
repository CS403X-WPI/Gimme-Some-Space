package com.ubi.alonso.givemesomespace;

import android.util.Log;

/**
 * Created by ben on 4/30/16.
 */
public class StudySpace {
    public String name;
    public Integer rating;

    public StudySpace(String name, int rating)
    {
        Log.d("MESSAGE","Created Study space "+name+" With Rating: "+rating);
        this.name = name;
        this.rating = rating;
    }
}
