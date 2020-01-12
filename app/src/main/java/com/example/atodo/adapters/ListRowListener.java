package com.example.atodo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;

import com.example.atodo.EditActivity;
import com.example.atodo.MainActivityVM;
import com.example.atodo.database.entities.Task;

public class ListRowListener implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Context context;
    private MainActivityVM mMainActivityVM;
    private Task mTask;

    ListRowListener(Context context, MainActivityVM mMainActivityVM, Task task) {
        this.context = context;
        this.mMainActivityVM = mMainActivityVM;
        mTask = task;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mMainActivityVM.updateTaskFinished(mTask, isChecked);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("TaskId", mTask.uid);

        context.startActivity(intent);
    }
}
