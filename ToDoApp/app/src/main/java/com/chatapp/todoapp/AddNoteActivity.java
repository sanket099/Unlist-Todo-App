package com.chatapp.todoapp;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {
    //psfs yo!
    public static final String EXTRA_TITLE =
            "title";
    public static final String EXTRA_DESCRIPTION =
            "desc";
    public static final String EXTRA_PRIORITY =
            "pri";
    public static final String EXTRA_ID =
            "id";
    public static final String EXTRA_DATE =
            "date";
    public static final String EXTRA_TIME =
            "time";

    public static final String EXTRA_DONE = "done";



    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private TextView tv_date;
    private TextView tv_time;
    RadioButton radioButton;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ImageView iv_title;
    private ImageView iv_desc;
    Boolean done ;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        CreateNotificationChannel();

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        tv_date = findViewById(R.id.tv_date);
        //radioButton = findViewById(R.id.rb_done);
        tv_time = findViewById(R.id.tv_time);
        iv_desc = findViewById(R.id.iv_voice_desc);
        iv_title = findViewById(R.id.iv_voice_title);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                final DatePickerDialog datePickerDialog = new DatePickerDialog(AddNoteActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {




                                if(String.valueOf(monthOfYear).length() == 1){
                                    tv_date.setText(dayOfMonth + "-" + "0"+(monthOfYear + 1) + "-" + year);
                                }
                                else{
                                    tv_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }



                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


            }
        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddNoteActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {


                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tv_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.show();

            }
        });

        iv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,3);

                }
                else{
                    Toast.makeText(AddNoteActivity.this,"Device Doesnt Support",Toast.LENGTH_SHORT).show();

                }

            }
        });

        iv_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if (intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,4);

                }
                else{
                    Toast.makeText(AddNoteActivity.this,"Device Doesnt Support",Toast.LENGTH_SHORT).show();

                }

            }
        });

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
            tv_date.setText(intent.getStringExtra(EXTRA_DATE));
            tv_time.setText(intent.getStringExtra(EXTRA_TIME));
            done = intent.getBooleanExtra(EXTRA_DONE,false);



        } else {
            setTitle("Add Note");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int priority = numberPickerPriority.getValue();
        String date = tv_date.getText().toString();
        String time = tv_time.getText().toString();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();

        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_DATE,date);
        data.putExtra(EXTRA_TIME , time);
        if(done == null){
            data.putExtra(EXTRA_DONE, false);
        }
        else{
            data.putExtra(EXTRA_DONE, done);
        }



        //editing
        int id =getIntent().getIntExtra(EXTRA_ID,-1);
        if(id != -1){
            data.putExtra(EXTRA_ID,id);

        }
        System.out.println(data + "  " +id);
        setResult(RESULT_OK, data);


        if(date.equals("Date") || time.equals("Time") )
        {
            Toast.makeText(this, "Set Date and Time to get Reminders ", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(this,Broadcast.class);
            intent.putExtra("title", title);
            intent.putExtra("desc",description);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            String str_date= date + " " + time;

            Date localTime = null;
            try {
                localTime = new SimpleDateFormat("dd-MM-yyy HH:mm",Locale.getDefault()).parse(str_date);
                System.out.println("TimeStamp is " +localTime.getTime());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                System.out.println("TimeStamp is " + e.getMessage());
            }


            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    localTime.getTime(),
                    pendingIntent);

            Toast.makeText(this, "Reminder Set", Toast.LENGTH_SHORT).show();

        }




        finish();//close
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNotificationChannel(){
        NotificationChannel channel = new NotificationChannel("channelid","channelname", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("channeldesc");
        channel.enableLights(true);

        channel.enableVibration(true);


        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 3:
                if(resultCode == RESULT_OK && data!= null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextTitle.setText(result.get(0));
                    break;

                }
            case 4:
                if(resultCode == RESULT_OK && data!= null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editTextDescription.setText(result.get(0));
                    break;

                }


        }
    }
}