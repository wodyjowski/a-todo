package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.atodo.adapters.TaskAdapter;
import com.example.atodo.adapters.TaskListObserver;
import com.example.atodo.database.entities.Task;
import com.rustamg.filedialogs.FileDialog;
import com.rustamg.filedialogs.OpenFileDialog;

import java.io.IOException;
import java.io.OutputStreamWriter;
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

//        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//        requestPermissions(permissions, 1);
//
//
//        FileDialog dialog = new OpenFileDialog();
//        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_MaterialComponents_DayNight_DarkActionBar);
//        dialog.show(getSupportFragmentManager(), OpenFileDialog.class.getName());
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
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
