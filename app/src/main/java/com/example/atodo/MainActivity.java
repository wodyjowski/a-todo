package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.atodo.database.entities.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Objects
    private MainActivityVM mMainActivityVM;

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
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.listView);
        mEditText = findViewById(R.id.editText);
        mListView = findViewById(R.id.listView);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplication(), R.layout.list_layout ); // , mMainActivityVM.getAllTasks().getValue().stream().map((li) -> li.name).collect(Collectors.toList()));

        List<Task> tList =  mMainActivityVM.getAllTasks().getValue();
        if(tList != null) {
            for(Task t : tList) {
                listAdapter.add(t.name);
            }
        }

        mListView.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        CreateTask();
    }

    private void CreateTask() {
        String inputName = mEditText.getText().toString();
        mMainActivityVM.insert(new Task(){{
                name = inputName;
            }
        });
    }
}
