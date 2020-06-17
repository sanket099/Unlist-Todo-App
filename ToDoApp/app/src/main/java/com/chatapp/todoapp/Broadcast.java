package com.chatapp.todoapp;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, DisplayTasksActivity.class);
        PendingIntent intent1 = PendingIntent.getActivity(context,1,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"channelid")
                .setSmallIcon(R.drawable.ic_baseline_alarm_on_24)
                .setContentText(intent.getStringExtra("desc"))
                .setContentTitle("Reminder For : "+intent.getStringExtra("title"))
                .setSound(alarmSound)
                .setContentIntent(intent1)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);



        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200,builder.build());

    }
}
