package com.example.alviss.qtureminder.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alviss.qtureminder.R;
import com.example.alviss.qtureminder.template.ListTask;

import java.util.List;

/**
 * Created by Alviss on 23/03/2017.
 */

public class ListAdapter extends ArrayAdapter<ListTask> {

    public ListAdapter(Context context, int resource, List<ListTask> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.activity_line_task, null);
        }
        ListTask p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView txt_title = (TextView) view.findViewById(R.id.txt_title_task);
            txt_title.setText(p.title_task);
            TextView txt_date = (TextView) view.findViewById(R.id.txt_date_task);
            txt_date.setText(p.date_task);
            TextView txt_time = (TextView) view.findViewById(R.id.txt_time_task);
            txt_time.setText(p.time_task);
        }
        return view;
    }

}
