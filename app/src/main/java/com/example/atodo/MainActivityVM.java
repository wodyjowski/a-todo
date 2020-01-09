package com.example.atodo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.atodo.database.AppDatabase;
import com.example.atodo.database.TaskRepository;
import com.example.atodo.database.entities.Task;

import java.util.List;

// ViewModel
public class MainActivityVM extends AndroidViewModel {
    private TaskRepository mTaskRepository;

    private LiveData<List<Task>> mAllTasks;

    public MainActivityVM(@NonNull Application application) {
        super(application);
        mTaskRepository = new TaskRepository(AppDatabase.getDatabase(application));
        mAllTasks = mTaskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getmAllTasks() { return mAllTasks; }

    public void insert(Task task) { mTaskRepository.insert(task);}
}
