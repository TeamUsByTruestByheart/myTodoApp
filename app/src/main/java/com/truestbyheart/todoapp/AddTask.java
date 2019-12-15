package com.truestbyheart.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.truestbyheart.todoapp.Models.TaskModel;
import com.truestbyheart.todoapp.SQLite.DBHelper;

import java.util.Calendar;

public class AddTask extends AppCompatActivity {
    private static final String TAG = "AddTask";
    private DBHelper db;
    private static AddTask addTask;
    private PendingIntent pendingIntent;

    public static AddTask instance(){
        return addTask;
    }

    @Override
    protected void onStart() {
        super.onStart();
        addTask = this;
    }

    AlarmManager alarmManager;
    EditText taskDescription, dateOfTask, timeOfTask;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    TaskModel todoTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
         db = new DBHelper(this);
         todoTask = new TaskModel();
         alarmManager =(AlarmManager) getSystemService(ALARM_SERVICE);

        taskDescription = findViewById(R.id.taskDescription);
        dateOfTask = findViewById(R.id.dateForTask);
        timeOfTask = findViewById(R.id.timeForTask);

         Intent intent = getIntent();

         /**
          * @description using hasExtra(String name) function we can be able to check if an intent was passed with data or not
          *
          * */
         if(intent.hasExtra("TASK_ID")){
             int id = intent.getExtras().getInt("TASK_ID");
             todoTask = db.getTask(id);
             taskDescription.setText(todoTask.task);
             dateOfTask.setText(todoTask.date);
             timeOfTask.setText(todoTask.time);
         }



        dateOfTask.setInputType(InputType.TYPE_NULL);
        dateOfTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(AddTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateOfTask.setText(dayOfMonth+"/"+month+"/"+year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        timeOfTask.setInputType(InputType.TYPE_NULL);
        timeOfTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                final Intent alarmIntent = new Intent(AddTask.this,AlarmReceiver.class);

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);


                timePickerDialog = new TimePickerDialog(AddTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                     timeOfTask.setText(hourOfDay+":"+minute);
                     long time;
                     pendingIntent = PendingIntent.getBroadcast(AddTask.this,0,alarmIntent,0);

                     alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                             System.currentTimeMillis() + (5 * 1000),
                             10000,pendingIntent);
                    }
                }, hour, minute,true);
                timePickerDialog.show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.saveTask:
                if(getIntent().hasExtra("TASK_ID")){
                    updateTask();
                }
                else{
                    saveTask();
                }
                return true;
            default:
              return super.onOptionsItemSelected(item);
        }
    }

    public void saveTask() {
       todoTask.task = taskDescription.getText().toString();
       todoTask.date = dateOfTask.getText().toString();
       todoTask.time = timeOfTask.getText().toString();
       todoTask.isDone = false;
       boolean isCreated = db.InsertTask(todoTask);


        if(isCreated){
            MainActivity.notifyAdapter();
            startActivity(new Intent(AddTask.this, MainActivity.class));
        } else {
            Toast.makeText(this, "Failed to create the task", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateTask(){
        todoTask.task = taskDescription.getText().toString();
        todoTask.date = dateOfTask.getText().toString();
        todoTask.time = timeOfTask.getText().toString();
        boolean isUpdate = db.updateTask(todoTask,this);

        if(isUpdate){
            MainActivity.notifyAdapter();
            startActivity(new Intent(AddTask.this, MainActivity.class));
        }
    }

}