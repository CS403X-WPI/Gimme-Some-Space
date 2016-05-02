package com.ubi.alonso.givemesomespace;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wefika.horizontalpicker.HorizontalPicker;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>,HorizontalPicker.OnItemSelected{

    private GoogleMap mMap;
    private LatLng latLng;
    Marker currLocationMarker;
    public GoogleApiClient mApiClient;
    public static final String TAG = MapsActivity.class.getSimpleName();
    ArrayList<Geofence> geofences = new ArrayList<Geofence>();
    private Geofence libfence;


    public String CurrentBuildingName = "Study Space";
    private PendingIntent mPIntent;
    private Intent mIntent;
    private int rating = -1;
    private TextView tv;
    private Firebase myFirebaseRef;
    private DBHandler dbhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://blazing-heat-9371.firebaseio.com/");
        dbhandler = new DBHandler(myFirebaseRef,null);

        Resources res = getResources();




        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Firebase.setAndroidContext(this);




        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Show rationale and request permission.
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    mMap.setMyLocationEnabled(true);
                }
            }
        });



        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("MESSAGE","Recieved Intent from Child Activity");
                CurrentBuildingName = intent.getStringExtra("CurrentBuildingName");
                showDialog();
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(receiver, new IntentFilter("myBroadcastIntent"));


        mApiClient.connect();
        Button button;

        button=(Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, studySpaceListActivity.class);
                startActivity(intent);
            }
        });




    }





    @Override
    public void onItemSelected(int index)    {
        rating = 1+ index;
        Toast.makeText(this, "Rating: " + rating, Toast.LENGTH_SHORT).show();


        switch(index){
            case 0:
                tv.setText("Practically Empty");
                break;

            case 1:
                tv.setText("Very Few People");
                break;
            case 2:
                tv.setText("Moderate Amount of People");
                break;

            case 3:
                tv.setText("Large Amount of People");
                break;
            case 4:
                tv.setText("Completely Full");
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = null;
        //Intent intent = new Intent(this, ActivityRecognizedService.class);
        //PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 100, pendingIntent);
        if ((this.getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mApiClient);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mApiClient);
        }
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);

            ArrayList<RegisteredBuilding> buildings = RegisteredBuilding.getallBuildings();
            for (RegisteredBuilding building: buildings) {

                Log.d("MESSAGE","CONSTRUCTING BUILDING MARKER FOR "+building.name);
                markerOptions = new MarkerOptions();
                markerOptions.position(building.location);
                markerOptions.title(building.name);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(markerOptions);
            };

        }
        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        ArrayList<RegisteredBuilding> buildings = RegisteredBuilding.getallBuildings();
        for (int i = 0; i < buildings.toArray().length; i++)
        {

            geofences.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(buildings.get(i).name)

                    .setCircularRegion(
                            buildings.get(i).location.latitude,
                            buildings.get(i).location.longitude,
                            60)
                    .setExpirationDuration(3600000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setLoiteringDelay(6000)
                    .build());



        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(buildings.get(i).location)
                .fillColor(Color.argb(64, 0, 255, 0))
                .strokeColor(Color.GREEN)
                .strokeWidth(1)
                .radius(40);
        mMap.addCircle(circleOptions);

        LocationServices.GeofencingApi.addGeofences(
                mApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);

    }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);


        }



        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(geofences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mPIntent != null) {
            return mPIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    public void showDialog(){

        final Dialog d = new Dialog(MapsActivity.this);
        d.setContentView(R.layout.dialog);
        d.setTitle ("How full is the "+CurrentBuildingName+"?");
        TextView titletv = (TextView) d.findViewById(R.id.title_dialog);
        titletv.setText("How full is the "+CurrentBuildingName+"\non a scale of 1 to 5?");
        Button firstbutton = (Button) d.findViewById(R.id.button1);
        firstbutton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
        tv = (TextView) d.findViewById(R.id.descr);
        Button secondbutton = (Button) d.findViewById(R.id.button2);
        secondbutton.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);

        final HorizontalPicker np = (HorizontalPicker) d.findViewById(R.id.numberPicker1);
        np.setOnItemSelectedListener(this);
        firstbutton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick (View v){
                long rate;
                long timestamp;
                BuildingData data;
                timestamp = System.currentTimeMillis();
                rate = np.getSelectedItem();
                data = new BuildingData(timestamp,CurrentBuildingName,rate);

                //pushing new rating
                //to the server here
                dbhandler.sendData(data);
                d.dismiss();

            }
        });

        secondbutton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick (View v){

                d.dismiss();
            }
        });

        d.show();
    }


    /**
     * Method that handles creating a listView for the available goefences
     * @param v, given button
     */
    public void findStudySpace (View v) {

    }

 private BroadcastReceiver rcvr = new BroadcastReceiver() {
     @Override
     public void onReceive(Context context, Intent intent) {
         Log.d("MESSAGE","Got Building Name");
         Bundle bundle = intent.getExtras();
         if (bundle != null)
         {
             CurrentBuildingName = bundle.getString("CurrentBuildingName");
         }
     }
 };

}
