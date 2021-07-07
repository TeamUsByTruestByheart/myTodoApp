package com.truestbyheart.todoapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

import androidx.core.app.NotificationCompat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AlarmService extends IntentService {
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "todo_notification_channel";

    public AlarmService(String name) {
        super("Alarm Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       createNotificationChannel();
    }

    public void createNotificationChannel() {
        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder
                    (this, PRIMARY_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_todo_icon)
                    .setContentTitle(this.getString(R.string.notification_title))
                    .setContentText(this.getString(R.string.notification_text))
                    .setContentIntent(contentIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            // Deliver the notification
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
