package com.softwarefactory.teamdelta.serendipity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;

    Notification myNotification;

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
            Toast.makeText(context, "Entering specified GPS proximity area", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d(getClass().getSimpleName(), "exiting");
            Toast.makeText(context, "Exiting specified GPS proximity area", Toast.LENGTH_SHORT).show();
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLights(Color.WHITE, 1500, 1500);
        builder.setContentTitle("Proximity Alert!");
        builder.setContentText("You are near a saved recording!");
        // builder.build() might be required here but requires min API level change from 15 -> 16
        myNotification = builder.getNotification();
        notificationManager.notify(NOTIFICATION_ID, myNotification);
    }

}

