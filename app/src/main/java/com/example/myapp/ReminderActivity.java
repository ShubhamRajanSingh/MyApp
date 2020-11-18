package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity {

    GridView gridView;
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        gridView=findViewById(R.id.reminder_grid_view);



        final ReminderActivity.MainAdapter adapter = new ReminderActivity.MainAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                Database database = new Database(ReminderActivity.this);
                SQLiteDatabase db = database.getWritableDatabase();
                String s = adapter.message.get(position);
                db.delete("REMINDER", "MESSAGE=?", new String[]{s});
                finish();
                startActivity(getIntent());

                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i=new Intent(ReminderActivity.this,ReminderSeeActivity.class);
            i.putExtra("Message",adapter.message.get(position));
            startActivity(i);


//
            }
        });
        floatingActionButton=findViewById(R.id.reminder_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReminderActivity.this, SetReminderActivity.class);
                startActivity(i);
            }
        });
    }
    private class MainAdapter extends BaseAdapter {

        ArrayList<String> message = new ArrayList<>();


        @Override
        public int getCount() {

            Database database = new Database(ReminderActivity.this);
            SQLiteDatabase db = database.getReadableDatabase();
            int n = 0;

            Cursor cursor = db.rawQuery("SELECT * FROM REMINDER  ",
                    new String[]{});

            if(cursor!=null){

                cursor.moveToFirst();
                message.add(cursor.getString(1));
                n++;

            }
            while(cursor.moveToNext()){
                message.add(cursor.getString(1));
                n++;

            }



            return n;
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
            View view = getLayoutInflater().inflate(R.layout.task, null);
            TextView textView=view.findViewById(R.id.job_text);
            textView.setText(message.get(position));

            return view;
        }
    }

}
