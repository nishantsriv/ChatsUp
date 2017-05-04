package com.self.chatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    private String chat_with, myname, id1, id2;
    private ListView listView;
    private EditText msg_txt;
    private ImageView send_btn;
    private LinearLayout layout;
    private DatabaseReference datarefrence;
    private ArrayList<Userclass> arrayList;
    private String[] split, split2;
    private Toolbar toolbar;
    private TextView textView_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        arrayList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        datarefrence = FirebaseDatabase.getInstance().getReference("messages");
        listView = (ListView) findViewById(R.id.chatlist);
        msg_txt = (EditText) findViewById(R.id.edittxt);
        send_btn = (ImageView) findViewById(R.id.send_btn);
        chat_with = getIntent().getStringExtra("chat_with");
        split = chat_with.split("\\@");
        textView_toolbar = (TextView) findViewById(R.id.text_toolbar);
        textView_toolbar.setText("Chat with " + split[0]);
        myname = getIntent().getStringExtra("username");
        split2 = myname.split("\\@");
        msg_txt.setHint("Send to: " + split[0]);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id1 = datarefrence.child(split2[0] + "_" + split[0]).push().getKey();
                datarefrence.child(split2[0] + "_" + split[0]).child(id1).setValue(new Userclass(id1, myname, msg_txt.getText().toString()));
                id2 = datarefrence.child(split[0] + "_" + split2[0]).push().getKey();
                datarefrence.child(split[0] + "_" + split2[0]).child(id2).setValue(new Userclass(id2, myname, msg_txt.getText().toString()));
                msg_txt.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        datarefrence.child(split2[0] + "_" + split[0]).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Userclass userclass = dataSnapshot.getValue(Userclass.class);

                arrayList.add(userclass);
                listView.setAdapter(new ListViewAdapter2(Chat.this, arrayList));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
