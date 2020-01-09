package com.truestbyheart.todoapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class AlarmReceive extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // AddTask inst = AddTask.instance();

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if(alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());

         startWakefulService(context, (intent.setComponent(comp)));
         setResultCode(Activity.RESULT_OK);
    }
}
