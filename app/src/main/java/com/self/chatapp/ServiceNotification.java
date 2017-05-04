package com.self.chatapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.self.chatapp.R.id.action0;
import static com.self.chatapp.R.id.listView;
import static com.self.chatapp.R.id.username;

/**
 * Created by Nishant on 02-05-2017.
 */

public class ServiceNotification extends Service {
    private DatabaseReference datarefrence, databaseReferenceuser;
    String name;
    StringBuilder stringBuilder;
    private ArrayList<Userclass> arrayList, validarraylist;
    private ArrayList<Userclass> arrayListget, arrayListlocal;

    @Override
    public void onCreate() {
        super.onCreate();
        arrayListget = new ArrayList<>();
        arrayListlocal = new ArrayList<>();
        SharedPreferences pref = getSharedPreferences("name", MODE_PRIVATE);
        name = pref.getString("names", "numm");
        arrayList = new ArrayList<>();
        validarraylist = new ArrayList<>();
        datarefrence = FirebaseDatabase.getInstance().getReference("messages");
        databaseReferenceuser = FirebaseDatabase.getInstance().getReference("userdetail");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        databaseReferenceuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                validarraylist.clear();
                for (DataSnapshot ad : dataSnapshot.getChildren()) {
                    Userclass userclass = ad.getValue(Userclass.class);
                    arrayList.add(userclass);
                    Log.d("TEST", "IN");
                }

                for (int i = 0; i < arrayList.size(); i++) {
                    if (!arrayList.get(i).getUsename().equals(name)) {
                        validarraylist.add(arrayList.get(i));
                    }
                }

                for (int i = 0; i < validarraylist.size(); i++) {
                    final String[] split = validarraylist.get(i).getUsename().split("\\@");
                    final String[] split2 = name.split("\\@");

                    final int finalI = i;
                    datarefrence.child(split[0] + "_" + split2[0]).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            arrayListget.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Userclass userclass = dataSnapshot1.getValue(Userclass.class);
                                if (!userclass.getUsename().equals(name))
                                    arrayListget.add(userclass);
                            }
                            SharedPreferences sf = getSharedPreferences("size", MODE_PRIVATE);
                            Log.d("TEST", arrayListget.size() + "");
                            int pre = sf.getInt(name + username, 0);
                            int remaining = arrayListget.size() - pre;
                            if (remaining == 1) {
                                createnotification(split[0], arrayListget.get(pre).getMessage(),validarraylist.get(finalI).getUsename());
                            } else if (remaining > 1) {
                                String[] s = new String[arrayListget.size() - pre];
                                for (int i = pre; i < arrayListget.size(); i++) {
                                    s[i] = arrayListget.get(i).getMessage();
                                }
                                createnotification2(split[0], s, validarraylist.get(finalI).getUsename());
                            }
                            SharedPreferences.Editor editor = sf.edit();
                            editor.putInt(name + username, arrayListget.size()).commit();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void createnotification(String usename, String message,String s) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(usename)
                        .setContentText(message)
                        .setOngoing(false);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("username", name);
        intent.putExtra("chat_with", s);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(1, mBuilder.build());


    }

    private void createnotification2(String usename, String[] message, String s) {
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(usename)
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
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("username", name);
        intent.putExtra("chat_with", s);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(1, mBuilder.build());


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
