package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.EditText;

import com.example.atodo.database.entities.Task;

public class EditActivity extends AppCompatActivity {

    // Objects
    private EditActivityVM mEditActivityVM;
    private LiveData<Task> liveTask;

    // Controls
    private EditText editTextTaskName;
    private EditText editTextTaskContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        int taskId = getIntent().getIntExtra("TaskId", 0);

        mEditActivityVM = ViewModelProviders.of(this).get(EditActivityVM.class);
        liveTask = mEditActivityVM.GetTask(taskId);

        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskContent = findViewById(R.id.editTextTaskContent);

        liveTask.observe(this, task -> {
            editTextTaskName.setText(task.name);
            editTextTaskContent.setText(task.content);
        });
    }

    @Override
    public void onStop() {
        Task updatedTask = liveTask.getValue();
        updatedTask.name = editTextTaskName.getText().toString();
        updatedTask.content = editTextTaskContent.getText().toString();
        mEditActivityVM.updateTask(updatedTask);

        super.onStop();
    }
}
