package com.self.chatapp;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nisha on 09-05-2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Userclass> userclasses;

    public ChatAdapter(Context context, ArrayList<Userclass> userclasses) {
        this.context = context;
        this.userclasses = userclasses;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.model_chat, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String[] split=userclasses.get(position).getUsename().split("\\@");
        holder.username.setText(split[0]);
        holder.message.setText(userclasses.get(position).getMessage());
        String name = SharedPrefManager.getInstance(context).getDetail("name", "names");
        if (userclasses.get(position).getUsename().equals(name)) {
            holder.chat_layout.setGravity(Gravity.END);
            holder.chatchildlayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.edittxt));
        }else holder.chatchildlayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.edittxt2));
    }

    @Override
    public int getItemCount() {
        return userclasses.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView username, message;
        private LinearLayout chat_layout,chatchildlayout;

        public Holder(View itemView) {
            super(itemView);
            chatchildlayout= (LinearLayout) itemView.findViewById(R.id.chatchildlayout);
            chat_layout = (LinearLayout) itemView.findViewById(R.id.chatlayout);
            username = (TextView) itemView.findViewById(R.id.tv1);
            message = (TextView) itemView.findViewById(R.id.tv2);
        }
    }
}
