package com.self.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserlistActivity extends AppCompatActivity {
    private DatabaseReference databaseReferenceuserdetail;
    private RecyclerView  recyclerView;
    private ArrayList<Userclass> arrayList, validarraylist;
    private String name, id;
    private Toolbar toolbar;
    private TextView textView_toolbar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        textView_toolbar = (TextView) findViewById(R.id.text_toolbar);
        textView_toolbar.setText("UserlistActivity");
        arrayList = new ArrayList<>();
        validarraylist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_userlist);
        databaseReferenceuserdetail = FirebaseDatabase.getInstance().getReference("userdetail");
        name = getIntent().getStringExtra("username");
        String[] split = name.split("\\@");
        Toast.makeText(this, "Welcome: " + split[0], Toast.LENGTH_SHORT).show();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
    }

    private void adduser() {
        id = databaseReferenceuserdetail.push().getKey();
        databaseReferenceuserdetail.child(id).setValue(new Userclass(id, name, ""));
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReferenceuserdetail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                validarraylist.clear();
                for (DataSnapshot ad : dataSnapshot.getChildren()) {
                    Userclass userclass = ad.getValue(Userclass.class);
                    arrayList.add(userclass);
                }
                boolean chk = false;
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getUsename().equals(name)) {
                        chk = true;
                    }
                }
                if (chk) {

                } else {
                    adduser();
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    if (!arrayList.get(i).getUsename().equals(name)) {
                        validarraylist.add(arrayList.get(i));
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(UserlistActivity.this));
                recyclerView.setAdapter(new UserlistAdapter(UserlistActivity.this,validarraylist));
                progressDialog.dismiss();
                SharedPrefManager.getInstance(UserlistActivity.this).saveDetail("name", "names", name);

                startService(new Intent(UserlistActivity.this, NotificationService.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(UserlistActivity.this, ChatActivity.class);
                i.putExtra("username", name);
                i.putExtra("chat_with", validarraylist.get(position).getUsename());
                startActivity(i);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signOut();
            startActivity(new Intent(UserlistActivity.this, LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

