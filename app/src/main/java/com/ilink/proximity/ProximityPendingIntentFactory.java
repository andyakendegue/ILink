package com.ilink.proximity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ProximityPendingIntentFactory {
    public static final String PROX_ALERT_INTENT = "com.nkala.ProximityAlert";
    private static final int REQUEST_CODE = 0;

    public static PendingIntent createPendingIntent(Context context) {
        return PendingIntent.getBroadcast(context, 0, new Intent(PROX_ALERT_INTENT), 134217728);
    }
}
