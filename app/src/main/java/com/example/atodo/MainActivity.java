package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.atodo.adapters.TaskAdapter;
import com.example.atodo.database.entities.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    // Objects
    private MainActivityVM mMainActivityVM;
    private TaskAdapter listAdapter;

    // Controls
    private EditText mEditText;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainActivityVM = ViewModelProviders.of(this).get(MainActivityVM.class);
        InitInterface();
    }

    private void InitInterface() {

        // Init component objects
        mEditText = findViewById(R.id.editText);
        mListView = findViewById(R.id.listView);

        // Text entered event
        mEditText.setOnEditorActionListener(this);

        // Display text if list is empty
        mListView.setEmptyView(findViewById(R.id.emptyText));

        mMainActivityVM.getAllTasks().observe(this, tasks -> {
            listAdapter = new TaskAdapter(getApplication(), mMainActivityVM, tasks);
            mListView.setAdapter(listAdapter);
        });

        mListView.setAdapter(listAdapter);
    }

    private void CreateNewTask() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    }

    private void CreateTask() {
        String inputName = mEditText.getText().toString();
        mMainActivityVM.insert(new Task(){{
                name = inputName;
                created_date = new Date();
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String inputName = mEditText.getText().toString();
        mMainActivityVM.createTask(inputName);
        mEditText.setText("");

        return false;
    }
}
