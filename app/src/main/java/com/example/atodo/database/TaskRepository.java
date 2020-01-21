package com.example.atodo.database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.atodo.database.dao.TaskDao;
import com.example.atodo.database.entities.Task;

import java.util.List;

public class TaskRepository {
    private TaskDao mTaskDao;

    public TaskRepository(AppDatabase appDatabase) {
        mTaskDao = appDatabase.taskDao();
    }

    public LiveData<List<Task>> getAllTasks(boolean finished){
        return mTaskDao.getAllTaskList(finished);
    }

    public LiveData<List<Task>> getAllTaskLisByPriority(boolean finished) {
        return mTaskDao.getAllTaskLisByPriority(finished);
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
