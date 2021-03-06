package com.example.atodo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.atodo.adapters.TaskAdapter;
import com.example.atodo.adapters.TaskListObserver;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener, View.OnFocusChangeListener, OnLongClickListener, AdapterView.OnItemSelectedListener {

    // Constants
    private final int REQUEST_SAVE = 1;

    // Objects
    private MainActivityVM mMainActivityVM;
    private TaskAdapter listAdapter;
    private TaskListObserver listObserver;

    // Controls
    private EditText mEditText;
    private ListView mListView;
    private TextView mTextViewShowFinished;
    private TextView textViewTitle;
    private Spinner spinnerFilter;

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
        textViewTitle = findViewById(R.id.textViewTitle);
        spinnerFilter = findViewById(R.id.spinnerFilter);

        // Text entered event
        mEditText.setOnEditorActionListener(this);
        mEditText.setOnFocusChangeListener(this);

        // Click listeners
        mTextViewShowFinished.setOnClickListener(this);
        textViewTitle.setOnLongClickListener(this);

        // Display text if list is empty
        mListView.setEmptyView(findViewById(R.id.emptyText));

        //  Spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filter_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);
        spinnerFilter.setOnItemSelectedListener(this);

        listAdapter = new TaskAdapter(getApplication(), mMainActivityVM, null);
        mListView.setAdapter(listAdapter);

        listObserver = new TaskListObserver(listAdapter);

        ChangeFinishedVisibility();

        mListView.setAdapter(listAdapter);

        startService(new Intent(this, ReminderService.class));
        Log.i("Service start", "Service has started");
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
        switch (v.getId()) {
            case R.id.textViewShowFin:
                ChangeFinishedVisibility();
                break;
        }
    }

    private void ChangeFinishedVisibility() {
        // Change list data source on show/hide finished tasks
        if (mMainActivityVM.showFinishedTasks) {
            mMainActivityVM.showFinishedTasks = false;
            mTextViewShowFinished.setText(R.string.test_hide_finished);
        } else {
            mMainActivityVM.showFinishedTasks = true;
            mTextViewShowFinished.setText(R.string.test_show_finished);
        }

        mMainActivityVM.getTasks(this).observe(this, listObserver);

        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mEditText.setHint("");
            mEditText.setCursorVisible(true);
        } else {
            mEditText.setHint(getString(R.string.new_reminder));
            mEditText.setCursorVisible(false);
        }
    }

    @Override
    public boolean onLongClick(View v) {

        Log.d("Click", "Long click");

        int res = this.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (res != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, REQUEST_SAVE);
        } else {
            saveFile();
        }

        return true;
    }

    private void saveFile() {
        mMainActivityVM.getTasks(this).observe(this, taskList -> {
            mMainActivityVM.saveTaskList(taskList, getApplicationContext());
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_SAVE:
                saveFile();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        mMainActivityVM.orderType = position;
        mMainActivityVM.getTasks(this).observe(this, listObserver);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
