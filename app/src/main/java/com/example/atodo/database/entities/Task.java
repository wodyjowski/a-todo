package com.example.atodo.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.atodo.converters.DateConverter;

import java.util.Date;

@Entity
@TypeConverters({DateConverter.class})
public class Task {
    @PrimaryKey
    public int uid;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public Date created_date;
}
