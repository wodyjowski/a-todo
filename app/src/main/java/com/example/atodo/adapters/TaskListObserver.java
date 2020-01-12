package com.example.atodo.adapters;

import androidx.lifecycle.Observer;

import com.example.atodo.database.entities.Task;

import java.util.List;

public class TaskListObserver implements Observer<List<Task>> {

    private TaskAdapter mTaskAdapter;

    public TaskListObserver(TaskAdapter taskAdapter) {
        this.mTaskAdapter = taskAdapter;
    }

    @Override
    public void onChanged(List<Task> taskList) {
        mTaskAdapter.setmTaskList(taskList);
        mTaskAdapter.notifyDataSetChanged();
    }
}
