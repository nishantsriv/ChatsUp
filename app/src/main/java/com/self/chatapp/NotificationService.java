package com.self.chatapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.self.chatapp.R.id.username;

/**
 * Created by Nishant on 02-05-2017.
 */

public class NotificationService extends Service {
    private DatabaseReference datarefrence, databaseReferenceuser;
    String name;
    private ArrayList<Userclass> arrayList, validarraylist;
    private ArrayList<Userclass> arrayListget, arrayListlocal;
    private BroadcastReceiver reciever;

    @Override
    public void onCreate() {
        super.onCreate();
        arrayListget = new ArrayList<>();
        arrayListlocal = new ArrayList<>();
        name = SharedPrefManager.getInstance(this).getDetail("name", "names");
        /*SharedPreferences pref = getSharedPreferences("name", MODE_PRIVATE);
        name = pref.getString("names", "numm");*/
        arrayList = new ArrayList<>();
        validarraylist = new ArrayList<>();
        datarefrence = FirebaseDatabase.getInstance().getReference("messages");
        databaseReferenceuser = FirebaseDatabase.getInstance().getReference("userdetail");
        this.reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo info = manager.getActiveNetworkInfo();
                    if (info != null && info.isConnectedOrConnecting()) {
                        noti();
                    } else {
                        Toast.makeText(context, "D", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(reciever, intentFilter);
    }

    private void noti() {
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
                    split[0] = split[0].replace(".", "");
                    split2[0] = split2[0].replace(".", "");
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


                            Log.d("TEST", arrayListget.size() + "");

                            int pre = SharedPrefManager.getInstance(NotificationService.this).getinteger("size", name + username);
                            int remaining = arrayListget.size() - pre;
                            if (remaining == 1) {
                                NotificationClass.createnotification_single(NotificationService.this, split[0], arrayListget.get(pre).getMessage(), validarraylist.get(finalI).getUsename(), name);
                            } else if (remaining > 1) {
                                String[] s = new String[arrayListget.size() - pre];
                                for (int i = pre; i < (arrayListget.size() - pre); i++) {
                                    s[i] = arrayListget.get(i).getMessage();
                                }
                                NotificationClass.createnotification_multiple(NotificationService.this, split[0], s, validarraylist.get(finalI).getUsename(), name);
                            }
                            SharedPrefManager.getInstance(NotificationService.this).saveinteger("size", name + username, arrayListget.size());

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


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciever);
    }
}