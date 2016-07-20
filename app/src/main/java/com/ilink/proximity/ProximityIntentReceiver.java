package com.ilink.proximity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ilink.HelpActivity;
import com.ilink.R;

public class ProximityIntentReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1000;

    // private static final string MYCLASS = getClass().getSimpleName();

    // New notif
    public static final String EVENT_ID_INTENT_EXTRA = "EventIDIntentExtraKey";
    private int notificationID = 1000;
    private int totalMessages = 0;
    private NotificationManager mNotificationManager;

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//				null, 0);

       // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                //intent, 0);

        Notification notification = createNotification();

        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {

            Log.d(getClass().getSimpleName(), "entering");
            //Toast.makeText(context, "entering", Toast.LENGTH_LONG).show();

            /*notification.setLatestEventInfo(context, "Proximity Alert!",
                    "You are entering your point of interest.", pendingIntent);

            notificationManager.notify(NOTIFICATION_ID, notification);
            */

            // new notif

            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
                    context);
            nBuilder.setContentTitle("Proximity Alert!");
            nBuilder.setContentText("You are entering your point of interest.");
            nBuilder.setTicker("Entering");
            nBuilder.setAutoCancel(true);
            nBuilder.setSmallIcon(R.mipmap.ic_launcher);
            nBuilder.setNumber(++totalMessages);

            Intent newIntent = new Intent(context, HelpActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            stackBuilder.addParentStack(HelpActivity.class);

            stackBuilder.addNextIntent(newIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
            PendingIntent.FLAG_UPDATE_CURRENT);
            nBuilder.setContentIntent(pendingIntent);

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationID, nBuilder.build());

        } else {

            Log.d(getClass().getSimpleName(), "exiting");
            //Toast.makeText(context,"exiting", Toast.LENGTH_LONG).show();
            /*notification.setLatestEventInfo(context, "Proximity Alert!",
                    "You are exiting your point of interest.", pendingIntent);
            notificationManager.notify(NOTIFICATION_ID, notification);
            */

            // new notif

            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
                    context);
            nBuilder.setContentTitle("Proximity Alert!");
            nBuilder.setContentText("You are exiting your point of interest.");
            nBuilder.setTicker("Exiting");
            nBuilder.setAutoCancel(true);
            nBuilder.setSmallIcon(R.mipmap.ic_launcher);
            nBuilder.setNumber(++totalMessages);

            Intent newIntent = new Intent(context, HelpActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(HelpActivity.class);

            stackBuilder.addNextIntent(newIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            nBuilder.setContentIntent(pendingIntent);

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationID, nBuilder.build());

        }




    }

    private Notification createNotification() {
        Notification notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
        notification.when = System.currentTimeMillis();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;
        return notification;



    }





}
