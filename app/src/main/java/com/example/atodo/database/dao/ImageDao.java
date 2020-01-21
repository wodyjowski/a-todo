package com.example.atodo.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.atodo.database.entities.Image;
import com.example.atodo.database.entities.Task;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert
    void insert(Image image);

    @Delete
    void delete(Image image);

    @Query("SELECT * FROM Images WHERE task_uid = :uid")
    LiveData<List<Image>> loadAll(int uid);
}
