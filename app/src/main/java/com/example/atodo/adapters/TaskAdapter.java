package com.example.atodo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.atodo.EditActivity;
import com.example.atodo.MainActivityVM;
import com.example.atodo.R;
import com.example.atodo.database.entities.Task;
import com.example.atodo.helpers.DateHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends BaseAdapter {

    private List<Task> mTaskList;
    private Context mContext;
    private MainActivityVM mMainActivityVM;

    public TaskAdapter(Context context, @NonNull MainActivityVM mainActivityVM, List<Task> taskList) {
        this.mMainActivityVM = mainActivityVM;
        if (taskList == null) {
            taskList = new ArrayList<Task>();
        }
        mTaskList = taskList;
        this.mContext = context;
    }

    public void setmTaskList(List<Task> tList) {
        mTaskList = tList;
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

        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row, parent, false);
        }

        Task task = mTaskList.get(position);
        ListRowListener rowListener = new ListRowListener(mContext, mMainActivityVM, task);

        CheckBox checkBox = convertView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(rowListener);
        convertView.findViewById(R.id.editButton).setOnClickListener(rowListener);

        ((TextView)convertView.findViewById(R.id.textName)).setText(task.name);
        String dateText = DateHelper.getDateString(task.created_date, mContext);
        ((TextView)convertView.findViewById(R.id.textDate)).setText(dateText);
        checkBox.setChecked(task.finished);

        setViewBackground(convertView, position);

        return convertView;
    }


    /**
     * Changes color of even rows
     * @param convertView view to change background
     * @param position position in list
     */
    private void setViewBackground(View convertView, int position) {
        int color = ContextCompat.getColor(mContext, R.color.background);

        if(position % 2 == 1){
            color = ContextCompat.getColor(mContext, R.color.lightListBackground);

        }
        convertView.setBackgroundColor(color);
    }
}
