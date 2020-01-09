package com.truestbyheart.todoapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class App extends Application {
    public static final String TODO_NOTIFICATION_CHANNEL = "todoNotificationChannel";
    private static final int NOTIFICATION_ID = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel todoChannel = new NotificationChannel(
                    TODO_NOTIFICATION_CHANNEL,
                    "todo notification channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            todoChannel.setDescription("This notification will remind the user when the time todo a task has reached.");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(todoChannel);
        }
    }

    private void deliverNotification(Context context) {
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TODO_NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_todo_icon)
                .setContentTitle("Stand Up Alert")
                .setContentText("You should stand up and walk around now!")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }
}
