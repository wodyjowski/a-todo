package com.example.atodo.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.atodo.converters.DateConverter;

import java.util.Date;

@Entity(tableName = "Tasks")
@TypeConverters({DateConverter.class})
public class Task {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String content;

    @ColumnInfo
    public int priority;

    @ColumnInfo
    public Date created_date;

    @ColumnInfo
    public Date reminder_date;

    @ColumnInfo
    public boolean remind;

    @ColumnInfo
    public boolean finished;

    @ColumnInfo
    public byte[] image;
}
