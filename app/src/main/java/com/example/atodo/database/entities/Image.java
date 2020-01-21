package com.example.atodo.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.atodo.converters.DateConverter;

@Entity(tableName = "Images")
@TypeConverters({DateConverter.class})
public class Image {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo
    public int task_uid;

    @ColumnInfo
    public byte[] data;
}
