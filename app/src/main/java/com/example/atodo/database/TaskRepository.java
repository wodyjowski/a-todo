package com.example.atodo.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.atodo.database.dao.TaskDao;
import com.example.atodo.database.entities.Task;

import java.util.List;

public class TaskRepository {
    private TaskDao mTaskDao;
    private LiveData<List<Task>> mAllTasks;
    private LiveData<List<Task>> mUnfinishedTasks;

    public TaskRepository(AppDatabase appDatabase) {
        mTaskDao = appDatabase.taskDao();
        mAllTasks = mTaskDao.getAllTaskList();
        mUnfinishedTasks = mTaskDao.getUnfinishedTaskList();
    }

    public LiveData<List<Task>> getAllTasks(){
        return mAllTasks;
    }

    public LiveData<List<Task>> getUnfinishedTasks() {
        return mUnfinishedTasks;
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

    public void imidiateUpdate(Task task) {
            mTaskDao.update(task);
    }

    public LiveData<Task> getTask(int uid) {
        return mTaskDao.loadSingle(uid);
    }

    public Task getNextTaskToRemind() {
        return mTaskDao.loadNextToRemind();
    }


    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.delete(task);
        });
    }
}
