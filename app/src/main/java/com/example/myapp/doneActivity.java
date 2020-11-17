package com.example.myapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class doneActivity extends AppCompatActivity {


    GridView gridView;
    CheckBox checkBox;
    TextView textTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        gridView = findViewById(R.id.task_done_grid_view);
        final doneActivity.MainAdapter adapter = new doneActivity.MainAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                String s = adapter.tasks.get(position);
                Database database = new Database(doneActivity.this);
                SQLiteDatabase db = database.getReadableDatabase();
                db.delete("TASKS", "TASK=?", new String[]{s});
                finish();
                startActivity(getIntent());

                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Help", "Clicked");
                checkBox = view.findViewById(R.id.check_box);
                textTask = view.findViewById(R.id.task_text);
                textTask.setBackgroundResource(R.color.white);
                checkBox.setChecked(false);
                String s = adapter.tasks.get(position);
                Database database = new Database(doneActivity.this);
                SQLiteDatabase db = database.getReadableDatabase();
                ContentValues values = new ContentValues();
                values.put("DONE", 0);

                db.update("TASKS", values, "TASK=?", new String[]{s});

                finish();
                startActivity(getIntent());

//
            }
        });
    }

    private class MainAdapter extends BaseAdapter {

        ArrayList<String> tasks = new ArrayList<>();


        @Override
        public int getCount() {
            Database database = new Database(doneActivity.this);
            SQLiteDatabase db = database.getReadableDatabase();
            int n = -1;

            Cursor cursor = db.rawQuery("SELECT * FROM TASKS where DONE=? ",
                    new String[]{"1"});


            if (cursor != null) {

                cursor.moveToFirst();
            }
            do {
                tasks.add(cursor.getString(1));
                n++;

            } while (cursor.moveToNext());


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
            View view = getLayoutInflater().inflate(R.layout.check_box_to_do, null);
            checkBox = view.findViewById(R.id.check_box);
            checkBox.setChecked(true);
            textTask = view.findViewById(R.id.task_text);
            textTask.setText(tasks.get(position));
            textTask.setBackgroundResource(R.drawable.strick);


            return view;
        }
    }
}
