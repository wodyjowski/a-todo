package com.example.atodo.adapters;

import android.widget.CompoundButton;

import com.example.atodo.MainActivityVM;
import com.example.atodo.database.entities.Task;

public class ListRowListener implements CompoundButton.OnCheckedChangeListener {

    private MainActivityVM mMainActivityVM;
    private Task mTask;
    private int mPosition;

    ListRowListener(MainActivityVM mMainActivityVM, Task task, int position) {
        this.mMainActivityVM = mMainActivityVM;
        mTask = task;
        this.mPosition = position;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mMainActivityVM.updateTaskFinished(mTask, isChecked);
    }
}
