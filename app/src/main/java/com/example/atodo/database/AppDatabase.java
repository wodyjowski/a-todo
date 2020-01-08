package com.example.atodo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.atodo.converters.DateConverter;
import com.example.atodo.database.dao.TaskDao;
import com.example.atodo.database.entities.Task;

@Database(entities = {Task.class},version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
