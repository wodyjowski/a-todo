package com.example.atodo.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public Long DateToLong(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public Date LongToDate(Long input){
        return input == null ? null : new Date(input);
    }
}
