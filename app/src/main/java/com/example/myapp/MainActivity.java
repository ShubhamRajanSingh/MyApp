package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    Switch switch1;
    int icon[] = {R.drawable.to_do,R.drawable.ic_tasks,R.drawable.ic_reminder};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.home_grid_view);
        switch1=findViewById(R.id.switch1);

        final Intent intent = new Intent(MainActivity.this, Notifications.class);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, 0);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                        Log.d("noti", "Notification Check");

                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);


                } else {
                    Log.d("noti","Notification end");

                    alarmManager.cancel(pendingIntent);


                }
            }
        });



        final MainAdapter adapter = new MainAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(position==0) {
                    Intent i = new Intent(MainActivity.this, to_do.class);
                    startActivity(i);
                }
                else if(position==1){
                    Intent i = new Intent(MainActivity.this, tasksActivity.class);
                    startActivity(i);
                }
                else if(position==2){
                    Intent i = new Intent(MainActivity.this, ReminderActivity.class);
                    startActivity(i);
                }


            }
        });


    }
    private class MainAdapter extends BaseAdapter{

        public MainAdapter(MainActivity mainActivity) {

        }

        @Override
        public int getCount() {


            return 3;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.home_grid_content, null);

            ImageView imageView = view.findViewById(R.id.logo_image);
            imageView.setImageResource(icon[position]);
            return view;
        }
    }
}
