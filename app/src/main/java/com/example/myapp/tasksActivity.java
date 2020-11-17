package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class tasksActivity extends AppCompatActivity implements dialogcustom.dialogCustomListener {

    GridView gridView;
    TextView textTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);


        gridView = findViewById(R.id.job_grid_view);
        final tasksActivity.MainAdapter adapter = new tasksActivity.MainAdapter();
        gridView.setAdapter(adapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                String s = adapter.tasks.get(position);
                Database database = new Database(tasksActivity.this);
                SQLiteDatabase db = database.getReadableDatabase();
                db.delete("JOBS", "DOMAIN=?", new String[]{s});
                finish();
                startActivity(getIntent());

                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                textTask=view.findViewById(R.id.job_text);


                String s=adapter.tasks.get(position);

                Intent i=new Intent(tasksActivity.this,subTasksActivity.class);
                i.putExtra("Task",s);
                startActivity(i);

            }
        });

        FloatingActionButton fab = findViewById(R.id.add_fab_job);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();

            }
        });

    }

    private void popup() {
        dialogcustom d = new dialogcustom();
        d.show(getSupportFragmentManager(), null);
    }



    @Override
    public void applyText(String task, Boolean done) {

        Database database = new Database(tasksActivity.this);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("DOMAIN",task);
        values.put("DONE",done);
        db.insert("JOBS",null,values);
        finish();
        startActivity(getIntent());

    }

    private class MainAdapter extends BaseAdapter {

        ArrayList<String> tasks=new ArrayList<>();



        @Override
        public int getCount() {
            Database database = new Database(tasksActivity.this);
            SQLiteDatabase db = database.getReadableDatabase();
            int n = 0;

            Cursor cursor = db.rawQuery("SELECT DISTINCT DOMAIN FROM JOBS ",
                    new String[]{});



            if (cursor != null) {

                cursor.moveToFirst();
            }
            while (cursor.moveToNext()){
                tasks.add(cursor.getString(0));
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

            textTask=view.findViewById(R.id.job_text);
            textTask.setText(tasks.get(position));


            return view;
        }
    }
}
