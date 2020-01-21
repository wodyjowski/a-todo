package com.example.atodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.atodo.adapters.ImageAdapter;
import com.example.atodo.database.entities.Image;
import com.example.atodo.database.entities.Task;
import com.example.atodo.helpers.DateHelper;
import com.example.atodo.helpers.ExpandableHeightGridView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.core.graphics.TypefaceCompatUtil.getTempFile;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    // Objects
    private EditActivityVM mEditActivityVM;
    private LiveData<Task> liveTask;
    private Task task;
    private Bitmap bitmap;

    // Controls
    private EditText editTextTaskName;
    private EditText editTextTaskContent;
    private TextView textViewCreatedDate;
    private Button buttonDelete;
    private Spinner spinnerPriority;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private CheckBox checkBoxRemind;
    private Button buttonAdd;
    private ExpandableHeightGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        int taskId = getIntent().getIntExtra("TaskId", 0);

        mEditActivityVM = ViewModelProviders.of(this).get(EditActivityVM.class);
        liveTask = mEditActivityVM.GetTask(taskId);

        editTextTaskName = findViewById(R.id.editTextTaskName);
        editTextTaskContent = findViewById(R.id.editTextTaskContent);
        textViewCreatedDate = findViewById(R.id.textViewCreatedDate);
        buttonDelete = findViewById(R.id.buttonDelete);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        checkBoxRemind = findViewById(R.id.checkBoxRemind);
        buttonAdd = findViewById(R.id.buttonAdd);
        gridView = findViewById(R.id.gridView);

        // Events
        buttonDelete.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);

        TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(this));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        ImageAdapter imgAdapter = new ImageAdapter(this, mEditActivityVM, null);
        gridView.setAdapter(imgAdapter);
        gridView.setExpanded(true);

        mEditActivityVM.getAllImages(taskId).observe(this, images -> {
            imgAdapter.setTaskList(images);
        });


        liveTask.observe(this, task -> {
            if(task == null)
                return;
            this.task = task;

            spinnerPriority.setSelection(task.priority);
            editTextTaskName.setText(task.name);
            editTextTaskContent.setText(task.content);
            checkBoxRemind.setChecked(task.remind);


            if(task.reminder_date != null)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(task.reminder_date);

                datePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                timePicker.setHour(cal.get(Calendar.HOUR));
                timePicker.setMinute(cal.get(Calendar.MINUTE));
            }

            checkBoxRemind.setChecked(task.remind);

            // String resource with parameter
            textViewCreatedDate.setText(getString(R.string.created, DateHelper.getDateTimeString(task.created_date, this)));
        });
    }

    public Intent getPickImageIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempFile(context));
        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    "Pick image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    private List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    @Override
    public void onStop() {
        Task updatedTask = liveTask.getValue();

        if(updatedTask != null) {
            updatedTask.name = editTextTaskName.getText().toString().trim();
            updatedTask.content = editTextTaskContent.getText().toString().trim();
            updatedTask.priority = spinnerPriority.getSelectedItemPosition();
            updatedTask.remind = checkBoxRemind.isChecked();

            if(bitmap != null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                updatedTask.image = stream.toByteArray();
            } else {
                updatedTask.image = null;
            }

            // Save reminder date and time
            Calendar cal = Calendar.getInstance();
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getHour(), timePicker.getMinute(), 0);

            updatedTask.reminder_date = cal.getTime();
            mEditActivityVM.updateTask(updatedTask);
        }

        super.onStop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDelete:
                deleteTask();
                break;
            case R.id.buttonAdd:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
                startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Object bitmapData = data.getExtras().get("data");
            bitmap = (Bitmap)bitmapData;

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            mEditActivityVM.createImage(stream.toByteArray(), task.uid);
        }
    }

    private void deleteTask() {
        mEditActivityVM.deleteTask(task);
        finish();
    }
}
