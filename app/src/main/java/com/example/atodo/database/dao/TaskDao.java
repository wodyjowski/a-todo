package com.example.atodo.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.atodo.database.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Tasks ORDER BY created_date")
    List<Task> getTaskList();

    @Insert
    void createTask(Task task);

    @Delete
    void DeleteTask(Task task);

    @Query("DELETE FROM Tasks")
    void DeleteAll();

}
