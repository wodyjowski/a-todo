package com.example.atodo.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.atodo.database.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM Tasks WHERE finished = 0 OR :finished ORDER BY finished, created_date DESC")
    LiveData<List<Task>> getAllTaskList(boolean finished);

    @Query("SELECT * FROM Tasks WHERE finished = 0 OR :finished ORDER BY finished, priority DESC, created_date DESC")
    LiveData<List<Task>> getAllTaskLisByPriority(boolean finished);

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM Tasks")
    void deleteAll();

    @Update
    void update(Task task);

    @Query("SELECT * FROM tasks WHERE uid = :uid")
    LiveData<Task> loadSingle(int uid);

    @Query("SELECT * FROM tasks WHERE remind ORDER BY reminder_date LIMIT 1")
    Task loadNextToRemind();
}
