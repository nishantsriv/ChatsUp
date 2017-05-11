package com.self.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by nisha on 11-05-2017.
 */

public class UserlistAdapter extends RecyclerView.Adapter<UserlistAdapter.Holder> {
    private Context context;
    private ArrayList<Userclass> userclassArrayList;
    private LayoutInflater layoutInflater;

    public UserlistAdapter(Context context, ArrayList<Userclass> validarraylist) {
        this.context = context;
        userclassArrayList = validarraylist;
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.model_userlist, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final String myname = SharedPrefManager.getInstance(context).getDetail("name", "names");
        holder.textView_username.setText(userclassArrayList.get(position).getUsename());

        holder.layoutview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("username", myname);
                i.putExtra("chat_with", userclassArrayList.get(position).getUsename());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userclassArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView textView_username;
        private LinearLayout layoutview;

        public Holder(View itemView) {
            super(itemView);
            textView_username = (TextView) itemView.findViewById(R.id.username);
            layoutview = (LinearLayout) itemView.findViewById(R.id.layoutview);
        }
    }
}
