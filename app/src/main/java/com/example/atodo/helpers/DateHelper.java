package com.example.atodo.helpers;

import android.content.Context;

import com.example.atodo.database.entities.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    private DateHelper() {}

    public static String getDateString(Date date, Context context) {
        // Time format 12/24 setting comes from system
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return date == null ? "" : timeFormat.format(date) + " " + dateFormat.format(date);
    }
}
