package com.self.chatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nishant on 11-04-2017.
 */

public class ListViewAdapter2 extends ArrayAdapter<Userclass> {
    private Context context;
    private List<Userclass> detailsList;

    public ListViewAdapter2(Context context, List<Userclass> userDetailsList) {
        super(context, R.layout.model, userDetailsList);
        this.context = context;
        this.detailsList = userDetailsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.model, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.username);
        Userclass userDetails = detailsList.get(position);
        String[] split = userDetails.getUsename().split("\\@");
        tv.setText(split[0]+ ":" + userDetails.getMessage());
        return view;
    }
}
