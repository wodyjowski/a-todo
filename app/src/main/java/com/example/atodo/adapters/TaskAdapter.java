package com.example.atodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.atodo.R;
import com.example.atodo.database.entities.Task;

import java.util.ArrayList;
import java.util.List;

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
        Task tsk = mTaskList.get(position);
        ((TextView)convertView.findViewById(R.id.textName)).setText(tsk.name);
        ((TextView)convertView.findViewById(R.id.textDate)).setText(tsk.getCreatedDate());

        return convertView;
    }
}
