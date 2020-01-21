package com.example.atodo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.atodo.database.AppDatabase;
import com.example.atodo.database.ImageRepository;
import com.example.atodo.database.TaskRepository;
import com.example.atodo.database.entities.Image;
import com.example.atodo.database.entities.Task;

import java.util.List;

public class EditActivityVM extends AndroidViewModel {
    private final TaskRepository mTaskRepository;
    private final ImageRepository mImageRepository;
    private Application application;

    public EditActivityVM(@NonNull Application application) {
        super(application);
        this.application = application;

        mTaskRepository = new TaskRepository(AppDatabase.getDatabase(application));
        mImageRepository = new ImageRepository(AppDatabase.getDatabase(application));
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

    public void createImage(byte[] data, int uid) {
        Image img = new Image();
        img.data = data;
        img.task_uid = uid;

        mImageRepository.insert(img);
    }

    public LiveData<List<Image>> getAllImages(int task_uid) {
        return mImageRepository.getAllImages(task_uid);
    }

    public void deleteImage(Image img) {
        mImageRepository.delete(img);
    }
}
