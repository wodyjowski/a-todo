package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.atodo.database.entities.Task;
import com.example.atodo.helpers.DateHelper;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    // Objects
    private EditActivityVM mEditActivityVM;
    private LiveData<Task> liveTask;
    private Task task;

    // Controls
    private EditText editTextTaskName;
    private EditText editTextTaskContent;
    private TextView textViewCreatedDate;
    private Button buttonDelete;
    private Spinner spinnerPriority;

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
        buttonDelete = findViewById(R.id.buttonDelete);
        spinnerPriority = findViewById(R.id.spinnerPriority);

        // Events
        buttonDelete.setOnClickListener(this);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(this));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        liveTask.observe(this, task -> {
            if(task == null)
                return;
            this.task = task;

            spinnerPriority.setSelection(task.priority);
            editTextTaskName.setText(task.name);
            editTextTaskContent.setText(task.content);
            // String resource with parameter
            textViewCreatedDate.setText(getString(R.string.created, DateHelper.getDateTimeString(task.created_date, this)));
        });
    }

    @Override
    public void onStop() {
        Task updatedTask = liveTask.getValue();

        if(updatedTask != null) {
            updatedTask.name = editTextTaskName.getText().toString().trim();
            updatedTask.content = editTextTaskContent.getText().toString().trim();
            updatedTask.priority = spinnerPriority.getSelectedItemPosition();
            mEditActivityVM.updateTask(updatedTask);
        }

        super.onStop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDelete:
                deleteTask();
                break;
        }
    }

    private void deleteTask() {
        mEditActivityVM.deleteTask(task);
        finish();
    }
}
