package com.YaroslavGorbach.delusionalgenerator.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.YaroslavGorbach.delusionalgenerator.feature.notifycation.MyNotificationManager;
import com.YaroslavGorbach.delusionalgenerator.feature.notifycation.MyNotificationManagerImp;

public class AlarmReceiver extends BroadcastReceiver {
    public static String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    @Override
    public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);
            MyNotificationManager myNotificationManager = new MyNotificationManagerImp();
            myNotificationManager.sendNotification(notificationManager, context, intent.getStringExtra(EXTRA_MESSAGE));
    }
}
