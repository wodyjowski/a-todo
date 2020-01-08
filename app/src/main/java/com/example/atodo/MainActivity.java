package com.example.atodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.example.atodo.database.AppDatabase;
import com.example.atodo.database.entities.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase appDb = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "testDb").build();

        appDb.taskDao().createTask(new Task(){{
            name = "siemka";
        }});


    }
}
