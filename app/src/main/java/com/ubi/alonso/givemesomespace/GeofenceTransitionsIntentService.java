package com.ubi.alonso.givemesomespace;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.nearby.messages.Message;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.ubi.alonso.givemesomespace.action.FOO";
    private static final String ACTION_BAZ = "com.ubi.alonso.givemesomespace.action.BAZ";
    static boolean inGeofence = false;
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.ubi.alonso.givemesomespace.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.ubi.alonso.givemesomespace.extra.PARAM2";
    private static MapsActivity mapsClass;
    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            System.out.println("Failed");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {




            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            Log.d("MESSAGE","Geofence Event Captured. You are at "+geofencingEvent.getTriggeringGeofences().get(0).getRequestId());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            inGeofence = true;
            Intent dialogIntent = new Intent("myBroadcastIntent");


            showNotification(geofencingEvent.getTriggeringGeofences().get(0).getRequestId());

                dialogIntent.putExtra("CurrentBuildingName",geofencingEvent.getTriggeringGeofences().get(0).getRequestId());

              LocalBroadcastManager.getInstance(this).sendBroadcast(dialogIntent);

//            Log.i(TAG, geofenceTransitionDetails);
        } else {
//            // Log the error.
//            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void showNotification(String name){

        //Intent notificationIntent = new Intent(getBaseContext(), NotificationReceiver.class);
        //PendingIntent pIntent = PendingIntent.getActivity(EpworthIntroActivity.this, 0, notificationIntent, 0);




        Notification mNotification = new Notification.Builder(this)
                .setContentTitle("Give Me Some Space")
                .setContentText("You entered the "+name+". How full is it?")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                //.setContentIntent(pIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotification);


    }




}
