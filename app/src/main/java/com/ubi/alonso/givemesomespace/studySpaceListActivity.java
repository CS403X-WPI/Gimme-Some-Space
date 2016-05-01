package com.ubi.alonso.givemesomespace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class studySpaceListActivity extends AppCompatActivity {
    private Firebase myFirebaseRef;
    private StudySpaceAdapter adapter;

    String[] arr = {"a","b","c"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_space_list);

        myFirebaseRef = new Firebase("https://blazing-heat-9371.firebaseio.com/");
        DBHandler dbh = new DBHandler(myFirebaseRef,this);
        dbh.retrieveData();





    }

    public void setData(int data) {
        ArrayList<StudySpace> ssArr = new ArrayList<StudySpace>();
        ssArr.add(new StudySpace("Library", data));
        adapter = new StudySpaceAdapter(this,ssArr);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


    }
}
