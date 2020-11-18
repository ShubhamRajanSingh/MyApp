package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReminderSeeActivity extends AppCompatActivity {
    TextView message,date,time;
    Button returnprev;
    String m=null,d=null,t=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_see);

        message=findViewById(R.id.reminder_text_see_message);
        date=findViewById(R.id.reminder_date_see);
        time=findViewById(R.id.reminder_time_see);
        returnprev=findViewById(R.id.returnprev);

        Intent i=getIntent();
        Bundle extras=i.getExtras();


        if(!extras.isEmpty()){
            Database database = new Database(ReminderSeeActivity.this);
            SQLiteDatabase db = database.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM REMINDER where MESSAGE=? ",
                    new String[]{extras.get("Message").toString()});


            if (cursor != null) {

                cursor.moveToFirst();
               do {
                  m=cursor.getString(1);
                  d=cursor.getString(3);
                  t=cursor.getString(4);

                }while(cursor.moveToNext());
            }


        }

        message.setText(m);
        date.setText(d);
        time.setText(t);
        returnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ReminderSeeActivity.this,ReminderActivity.class);
                startActivity(i);
            }
        });
    }
}
