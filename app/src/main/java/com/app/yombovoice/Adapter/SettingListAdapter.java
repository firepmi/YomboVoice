package com.app.yombovoice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.yombovoice.R;

import java.util.ArrayList;

public class SettingListAdapter extends BaseAdapter {
    ArrayList<String> title_list = new ArrayList<String>();
    Context activityContext;
    public SettingListAdapter(Context context){
        activityContext = context;
        title_list.add("Name");
        title_list.add("Mobile Number");
        title_list.add("Default message");
        title_list.add("Password");
        title_list.add("Notification");
        title_list.add("Blocked");
        title_list.add("Logout");
    }
    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        LayoutInflater mInflater = (LayoutInflater)
                activityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.setting_item, null);
        TextView txtTitle = (TextView)view.findViewById(R.id.txt_title);
        txtTitle.setText(title_list.get(position));
        return view;
    }

}
