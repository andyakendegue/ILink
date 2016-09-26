package com.ilink.proximity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.google.android.gms.drive.DriveStatusCodes;
import com.ilink.HelpActivity;

public class ProximityIntentReceiver extends BroadcastReceiver {
    public static final String EVENT_ID_INTENT_EXTRA = "EventIDIntentExtraKey";
    private static final int NOTIFICATION_ID = 1000;
    private NotificationManager mNotificationManager;
    private int notificationID;
    private int totalMessages;

    public ProximityIntentReceiver() {
        this.notificationID = NOTIFICATION_ID;
        this.totalMessages = 0;
    }

    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        Notification notification = createNotification();
        if (Boolean.valueOf(intent.getBooleanExtra("entering", false)).booleanValue()) {
            Log.d(getClass().getSimpleName(), "entering");
            Builder nBuilder = new Builder(context);
            nBuilder.setContentTitle("Proximity Alert!");
            nBuilder.setContentText("You are entering your point of interest.");
            nBuilder.setTicker("Entering");
            nBuilder.setAutoCancel(true);
            nBuilder.setSmallIcon(C1558R.mipmap.ic_launcher);
            int i = this.totalMessages + 1;
            this.totalMessages = i;
            nBuilder.setNumber(i);
            Intent newIntent = new Intent(context, HelpActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(HelpActivity.class);
            stackBuilder.addNextIntent(newIntent);
            nBuilder.setContentIntent(stackBuilder.getPendingIntent(0, 134217728));
            this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
            this.mNotificationManager.notify(this.notificationID, nBuilder.build());
            return;
        }
        Log.d(getClass().getSimpleName(), "exiting");
        nBuilder = new Builder(context);
        nBuilder.setContentTitle("Proximity Alert!");
        nBuilder.setContentText("You are exiting your point of interest.");
        nBuilder.setTicker("Exiting");
        nBuilder.setAutoCancel(true);
        nBuilder.setSmallIcon(C1558R.mipmap.ic_launcher);
        i = this.totalMessages + 1;
        this.totalMessages = i;
        nBuilder.setNumber(i);
        newIntent = new Intent(context, HelpActivity.class);
        stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HelpActivity.class);
        stackBuilder.addNextIntent(newIntent);
        nBuilder.setContentIntent(stackBuilder.getPendingIntent(0, 134217728));
        this.mNotificationManager = (NotificationManager) context.getSystemService("notification");
        this.mNotificationManager.notify(this.notificationID, nBuilder.build());
    }

    private Notification createNotification() {
        Notification notification = new Notification();
        notification.icon = C1558R.mipmap.ic_launcher;
        notification.when = System.currentTimeMillis();
        notification.flags |= 16;
        notification.flags |= 1;
        notification.defaults |= 2;
        notification.defaults |= 4;
        notification.ledARGB = -1;
        notification.ledOnMS = DriveStatusCodes.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        notification.ledOffMS = DriveStatusCodes.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        return notification;
    }
}
