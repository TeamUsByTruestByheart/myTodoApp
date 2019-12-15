package com.truestbyheart.todoapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    public AlarmService(){
        super("AlarmService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        sendNotification("wake up!");
    }

    private void sendNotification(String msg) {
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0,
                new Intent(this, AddTask.class), 0);

        NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("TodoApp").setSmallIcon(R.drawable.ic_launcher_foreground)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
    }
}
