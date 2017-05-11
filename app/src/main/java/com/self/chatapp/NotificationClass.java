package com.self.chatapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by nisha on 10-05-2017.
 */

public class NotificationClass {
    public static void createnotification_single(Context context, String usename, String message, String s, String name) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(usename)
                        .setAutoCancel(true)
                        .setContentText(message)
                        .setOngoing(false);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);


        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username", name);
        intent.putExtra("chat_with", s);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        android.app.NotificationManager mNotifyMgr =
                (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        int random = (int) (Math.random() * 5000 + 1);

        // Builds the notification and issues it.
        mNotifyMgr.notify(random, mBuilder.build());

    }

    public static void createnotification_multiple(Context context, String usename, String[] message, String s, String name) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(usename)
                        .setAutoCancel(true)
                        .setContentText(message.length + " message recieved")
                        .setOngoing(false);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(message.length + " messages");

        for (int i = 0; i < message.length; i++) {
            inboxStyle.addLine(message[i]);
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username", name);
        intent.putExtra("chat_with", s);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        android.app.NotificationManager mNotifyMgr =
                (android.app.NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        int random = (int) (Math.random() * 5000 + 1);
        // Builds the notification and issues it.
        mNotifyMgr.notify(random, mBuilder.build());


    }
}
