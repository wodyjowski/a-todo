package com.example.atodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.atodo.R;
import com.example.atodo.database.entities.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends BaseAdapter {

    private List<Task> mTaskList;
    private Context context;

    public TaskAdapter(Context context, List<Task> taskList) {
        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }
        mTaskList = taskList;
        this.context = context;
    }

    public void clear() {
        mTaskList.clear();
    }

    public void add(Task task) {
        mTaskList.add(task);
    }

    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTaskList.get(position).uid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position > mTaskList.size()) {
            return null;
        }

        convertView = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
        Task task = mTaskList.get(position);
        ((TextView)convertView.findViewById(R.id.textName)).setText(task.name);
        ((TextView)convertView.findViewById(R.id.textDate)).setText(getCreatedDate(task, context));

        if(position % 2 == 1){
            int color = ContextCompat.getColor(context, R.color.lightListBackground);
            convertView.setBackgroundColor(color);
        }

        return convertView;
    }

    public String getCreatedDate(Task task, Context context) {
        // Time format 12/24 setting comes from system
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return task.created_date == null ? "" : timeFormat.format(task.created_date) + " " + dateFormat.format(task.created_date);
    }
}
