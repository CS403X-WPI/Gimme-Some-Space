package com.ubi.alonso.givemesomespace;

import android.text.format.Time;
import android.util.Log;

import java.util.Date;

/**
 * Created by ben on 4/30/16.
 */
public class StudySpace {
    public String name;
    public Integer rating;
    public Date lastPing;

    public StudySpace(String name, int rating,Date lastping)
    {
        Log.d("MESSAGE","Created Study space "+name+" With Rating: "+rating);
        this.name = name;
        this.rating = rating;
        this.lastPing = lastping;
    }
}
