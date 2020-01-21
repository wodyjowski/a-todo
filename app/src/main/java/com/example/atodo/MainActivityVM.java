package com.example.atodo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.atodo.database.AppDatabase;
import com.example.atodo.database.TaskRepository;
import com.example.atodo.database.entities.Task;
import com.example.atodo.helpers.DateHelper;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

// ViewModel
public class MainActivityVM extends AndroidViewModel {
    private TaskRepository mTaskRepository;
    private LiveData<List<Task>> currentData;


    public boolean showFinishedTasks = false;
    public int orderType = 0;

    public MainActivityVM(@NonNull Application application) {
        super(application);
        mTaskRepository = new TaskRepository(AppDatabase.getDatabase(application));
    }

    public void insert(Task task) { mTaskRepository.insert(task);}

    public void createTask(String inputName) {
        if(isStringEmpty(inputName)) return;
            this.insert(new Task(){{
                name = inputName;
                created_date = new Date();
            }
        });
    }

    private boolean isStringEmpty(String input) {
        return input.trim().isEmpty();
    }

    public void updateTaskFinished(Task task, boolean finished) {
        if(task.finished == finished) return;

        task.finished = finished;
        mTaskRepository.update(task);
    }

    public void saveTaskList(List<Task> taskList, Context context) {

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.d("Directory", dir.getAbsolutePath());
        try {
            dir.mkdirs();
            File saveFile = new File(dir, "task_list " + DateHelper.getCustomDateTimeString(new Date()) + ".txt");
            FileWriter fileWriter = new FileWriter(saveFile);
            fileWriter.append(new Gson().toJson(taskList));
            fileWriter.close();
            Toast.makeText(context, "File saved", Toast.LENGTH_SHORT).show();

        } catch (Exception ex) {
            Toast.makeText(context, "Couldn't create file", Toast.LENGTH_SHORT).show();
            Log.e("Error", ex.getMessage());
        }
    }

    public void removeAllObservers(LifecycleOwner owner) {
        if(currentData == null)
            return;

        currentData.removeObservers(owner);
    }

    public LiveData<List<Task>> getTasks(LifecycleOwner owner) {
        removeAllObservers(owner);

        LiveData<List<Task>> resultData = null;

        switch (orderType) {
            case 0:
                resultData = mTaskRepository.getAllTasks(!showFinishedTasks);
                break;
            case 1:
                resultData = mTaskRepository.getAllTaskLisByPriority(!showFinishedTasks);
                break;
        }

        currentData = resultData;

        return resultData;
    }
}
