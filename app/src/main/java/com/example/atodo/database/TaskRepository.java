package com.example.atodo.database;

import androidx.lifecycle.LiveData;

import com.example.atodo.database.dao.TaskDao;
import com.example.atodo.database.entities.Task;

import java.util.List;

public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;

    public TaskRepository(AppDatabase appDatabase) {
        mTaskDao = appDatabase.taskDao();
        mAllTasks = mTaskDao.getTaskList();
    }

    public LiveData<List<Task>> getAllTasks(){
        return mAllTasks;
    }

    public void insert(final Task task){
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        });
    }

    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.update(task);
        });
    }
}
