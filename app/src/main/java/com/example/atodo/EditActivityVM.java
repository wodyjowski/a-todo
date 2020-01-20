package com.example.atodo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.atodo.database.AppDatabase;
import com.example.atodo.database.TaskRepository;
import com.example.atodo.database.entities.Task;

public class EditActivityVM extends AndroidViewModel {
    private final TaskRepository mTaskRepository;
    private Application application;

    public EditActivityVM(@NonNull Application application) {
        super(application);
        this.application = application;

        mTaskRepository = new TaskRepository(AppDatabase.getDatabase(application));
    }

    public LiveData<Task> GetTask(int uid) {
        return mTaskRepository.getTask(uid);
    }


    public void updateTask(Task updatedTask) {
        mTaskRepository.update(updatedTask);
    }

    public void deleteTask(Task task) {
        mTaskRepository.delete(task);

    }
}
