package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.allyants.notifyme.NotifyMe;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    TextView setDate,setTime;
    EditText message;
    Button setdateButton,setTimeButton;
    Calendar now =Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        setDate=findViewById(R.id.reminder_date);
        setTime=findViewById(R.id.reminder_time);
        message=findViewById(R.id.reminder_edittext);
        setdateButton=findViewById(R.id.set_date_button);
        setTimeButton=findViewById(R.id.set_time_button);

        dpd=DatePickerDialog.newInstance(SetReminderActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
                );
        tpd=TimePickerDialog.newInstance(SetReminderActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),false
        );
        setdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(),"Datepickerdialog");


            }
        });
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tpd.show(getFragmentManager(),"Timepickerdialog");
            }
        });


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);

        Date date = now.getTime();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String date1 = format1.format(date);

        Date inActiveDate = null;

        try {

            inActiveDate = format1.parse(date1);

        } catch (ParseException e1) {


            e1.printStackTrace();

        }

        String formateDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inActiveDate);
        String formateDate = new SimpleDateFormat("yyyy-MM-dd ").format(inActiveDate);
        String formateTime = new SimpleDateFormat(" HH:mm:ss").format(inActiveDate);

        Database database = new Database(SetReminderActivity.this);
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("MESSAGE",message.getText().toString());
        values.put("datetimes",formateDateTime);
        values.put("date",formateDate);
        values.put("time",formateTime);
        db.insert("REMINDER",null,values);
        setDate.setText(formateDate);
        setTime.setText(formateTime);
        NotifyMe notifyMe = new NotifyMe.Builder(SetReminderActivity.this)
                .title("Reminder")
                .content(message.getText().toString())
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .addAction(new Intent(), "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(new Intent(), "Done")
                .large_icon(R.drawable.ic_notifications)
                .build();


//        db.rawQuery("SELECT * FROM REMINDER ORDER BY datetime(datetimes) DESC LIMIT 1 ",
//                new String[]{});




//        Log.d("date",formateDate+" "+formateTime+" "+formateDateTime);





    }
}
