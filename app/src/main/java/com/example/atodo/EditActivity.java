package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.atodo.database.entities.Task;
import com.example.atodo.helpers.DateHelper;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

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

        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editTextDate:
                EditDateDialog();
                break;
        }
    }

    private void EditDateDialog() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }

        };


        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
