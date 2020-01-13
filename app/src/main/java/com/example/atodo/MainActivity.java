package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.atodo.adapters.TaskAdapter;
import com.example.atodo.adapters.TaskListObserver;
import com.example.atodo.database.entities.Task;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener, View.OnFocusChangeListener {
    // Objects
    private MainActivityVM mMainActivityVM;
    private TaskAdapter listAdapter;
    private TaskListObserver listObserver;

    // Controls
    private EditText mEditText;
    private ListView mListView;
    private TextView mTextViewShowFinished;

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
        mTextViewShowFinished = findViewById(R.id.textViewShowFin);

        // Text entered event
        mEditText.setOnEditorActionListener(this);
        mEditText.setOnFocusChangeListener(this);
        mTextViewShowFinished.setOnClickListener(this);

        // Display text if list is empty
        mListView.setEmptyView(findViewById(R.id.emptyText));

        listAdapter = new TaskAdapter(getApplication(), mMainActivityVM, null);
        mListView.setAdapter(listAdapter);

        listObserver = new TaskListObserver(listAdapter);

        ChangeFinishedVisibility();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewShowFin:
                ChangeFinishedVisibility();
                break;
        }
    }

    private void ChangeFinishedVisibility() {

        // Change list data source on show/hide finished tasks
        if(mMainActivityVM.isShowFinishedTasks()){
            mMainActivityVM.getmUnfinishedTasks().removeObservers(this);
            mMainActivityVM.setShowFinishedTasks(false);
            mMainActivityVM.getAllTasks().observe(this, listObserver);
            mTextViewShowFinished.setText(R.string.test_hide_finished);
        } else {
            mMainActivityVM.getAllTasks().removeObservers(this);
            mMainActivityVM.setShowFinishedTasks(true);
            mMainActivityVM.getmUnfinishedTasks().observe(this, listObserver);
            mTextViewShowFinished.setText(R.string.test_show_finished);
        }

        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            mEditText.setHint("");
            mEditText.setCursorVisible(true);
        }
        else{
            mEditText.setHint(getString(R.string.new_reminder));
            mEditText.setCursorVisible(false);
        }
    }
}
