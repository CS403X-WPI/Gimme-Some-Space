package com.ubi.alonso.givemesomespace;


import android.util.Log;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.firebase.client.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jameschow on 4/29/16.
 */
public class DBHandler {
    private static int ccAvg;
    private static int libAvg;
    private Firebase fb;
    private studySpaceListActivity activity;

    public DBHandler(Firebase firebase, studySpaceListActivity ssla) {
        this.fb = firebase;
        this.activity = ssla;
    }

    Integer finalAVG = 0;

    public void sendData(BuildingData data) {
        try {

            Firebase newref = fb.child("inputs").push();

            Firebase newData = newref.child("Building Name");
            Firebase childrenTime = newref.child("Time");
            Firebase childrenRate = newref.child("Rating");
            newData.setValue(data.getBuildingName());
            childrenTime.setValue(data.getTimeStamp());
            childrenRate.setValue(data.getRating());

        } catch (Exception e) {
            Log.d("MESSAGE", e.toString());
            e.printStackTrace();
        }

        Log.d("MESSAGE", "DATA SUCCESSFULLY SAVED");

    }

    public StudySpace retrieveData() {
        final List<BuildingData> dataList = new ArrayList<BuildingData>();
        this.fb.child("inputs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int counter = 0;
                int limit;
                if (Integer.parseInt(Long.toString(snapshot.getChildrenCount())) < 10) {
                    limit = Integer.parseInt(Long.toString(snapshot.getChildrenCount()));
                } else {
                    limit = 10;
                }
                Log.d("MESSAGE", "There are " + snapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    try {
//                        BuildingData post = (BuildingData) postSnapshot.getValue(BuildingData.class);
//                        dataList.add(post);
                        if (counter < limit - 1) {
                            String name = (String) postSnapshot.child("Building Name").getValue();
                            long rating = (long) postSnapshot.child("Rating").getValue();
                            long time = Long.parseLong(((String) postSnapshot.child("Time").getValue()));
                            Log.d("MESSAGE", "The name is :" + name + " with rating " + rating + " at time " + time + " counter " + counter);
                            dataList.add(new BuildingData(time, name, rating));
                            counter++;

                        } else if (counter == limit - 1) {
                            String name = (String) postSnapshot.child("Building Name").getValue();
                            long rating = (long) postSnapshot.child("Rating").getValue();
                            long time = Long.parseLong(((String) postSnapshot.child("Time").getValue()));
                            Log.d("MESSAGE", "The name is :" + name + " with rating " + rating + " at time " + time + " counter " + counter);
                            dataList.add(new BuildingData(time, name, rating));
                            Log.d("MESSAGE", "LIST IS " + dataList.toString());

                            //multiple methods for the different buildings
                            activity.setData(computeAverageRate(dataList));

                            break;
                        } else {
                            String name = (String) postSnapshot.child("Building Name").getValue();
                            Log.d("MESSAGE", "LIST IS " + dataList.toString());

                            activity.setData(computeAverageRate(dataList));
                            break;
                        }
                    } catch (Exception e) {
                        Log.d("MESSAGE", e.toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        Log.d("MESSAGE", "LIBAVG AT OUTPUT: " + finalAVG);

        Calendar c = Calendar.getInstance();

        return new StudySpace("Library", finalAVG, c.getTime());
    }


    public ArrayList<RegisteredBuilding> computeAverageRate(List<BuildingData> dataList) {

        ArrayList<RegisteredBuilding> allBuildings = RegisteredBuilding.getallBuildings();


        for (BuildingData data : dataList) {
            for (RegisteredBuilding building : allBuildings) {
                Log.d("MESSAGE","Rating for "+ data.getBuildingName()+" @"+data.getRating());
                Log.d("MESSAGE","Comapring "+ data.getBuildingName()+" with "+data.getBuildingName());
                if (building.name.equals(data.getBuildingName())) {
                    Log.d("MESSAGE","Comapring Successful"+data.getBuildingName());
                    building.rating += data.getRating();
                    building.hitcount = building.hitcount + 1;

                    building.lasthit = new Date(Long.parseLong(data.getTimeStamp()) * 1000);
                }
            }

        }
        return allBuildings;
    }
}


