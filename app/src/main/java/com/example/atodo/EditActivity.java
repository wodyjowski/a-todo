package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.atodo.database.entities.Task;
import com.example.atodo.helpers.DateHelper;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity  {

    // Objects
    private EditActivityVM mEditActivityVM;
    private LiveData<Task> liveTask;
    final Calendar myCalendar = Calendar.getInstance();

    // Controls
    private EditText editTextTaskName;
    private EditText editTextTaskContent;
    private TextView textViewCreatedDate;
    private EditText editTextDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        int taskId = getIntent().getIntExtra("TaskId", 0);

        mEditActivityVM = ViewModelProviders.of(this).get(EditActivityVM.class);
        liveTask = mEditActivityVM.GetTask(taskId);

        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskContent = findViewById(R.id.editTextTaskContent);
        textViewCreatedDate = findViewById(R.id.textViewCreatedDate);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(this));

        liveTask.observe(this, task -> {
            editTextTaskName.setText(task.name);
            editTextTaskContent.setText(task.content);
            // String resource with parameter
            textViewCreatedDate.setText(getString(R.string.created, DateHelper.getDateString(task.created_date, this)));
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
