package com.example.atodo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.core.app.NotificationCompat;

import com.example.atodo.database.AppDatabase;
import com.example.atodo.database.TaskRepository;
import com.example.atodo.database.entities.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderService extends Service {

    private final String CHANNEL_NAME = "Task reminders";

    private TaskRepository mTaskRepository;
    private Task nextTask;

    public ReminderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTaskRepository = new TaskRepository(AppDatabase.getDatabase(this));

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(1);

        notificationChanelConfig();


        // This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Log.d("Service", "Service is working");

                checkAndNotify();
            }
        }, 0, 1, TimeUnit.SECONDS);

        return super.onStartCommand(intent, flags, startId);
    }

    private void checkAndNotify() {
        loadNextTask();
        Log.d("Service", "Reminder checked");

        Date currentDate = Calendar.getInstance().getTime();

        if(nextTask != null && (currentDate.equals(nextTask.reminder_date) || currentDate.after(nextTask.reminder_date))) {
            reminderNotification();
            nextTask.remind = false;
            mTaskRepository.update(nextTask);



            checkAndNotify();
        }
    }

    private void notificationChanelConfig() {

        // Create notification channel for API > 29
        NotificationChannel serviceChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            serviceChannel = new NotificationChannel(
                    CHANNEL_NAME,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void loadNextTask() {
        nextTask = mTaskRepository.getNextTaskToRemind();
    }

    private void reminderNotification() {
        Notification newMessageNotification = new NotificationCompat.Builder(this, CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_info_24dp)
                .setContentTitle(nextTask.name)
                .setContentText(nextTask.content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(nextTask.uid, newMessageNotification);
        Log.d("Service", "Notification " + nextTask.uid);
    }
}

