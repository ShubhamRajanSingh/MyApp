package com.example.myapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class to_do extends AppCompatActivity implements dialogcustom.dialogCustomListener {

    GridView gridView;
    CheckBox checkBox;
    TextView textTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        gridView = findViewById(R.id.task_grid_view);
        final to_do.MainAdapter adapter = new to_do.MainAdapter();
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                checkBox=view.findViewById(R.id.check_box);
//                textTask=view.findViewById(R.id.task_text);
//                textTask.setBackgroundResource(R.drawable.strick);
//                checkBox.setChecked(true);
                String s=adapter.tasks.get(position);
                Database database = new Database(to_do.this);
                SQLiteDatabase db = database.getReadableDatabase();
                ContentValues values=new ContentValues();
                values.put("DONE",1);

                db.update("TASKS",values,"TASK=?",new String[]{s});
                finish();
                startActivity(getIntent());
//                db.delete("TASKS","TASK=?",new String[]{s});
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();

            }
        });
        FloatingActionButton fab2 = findViewById(R.id.done);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDone();
            }
        });
    }


    private void popup() {
        dialogcustom d = new dialogcustom();
        d.show(getSupportFragmentManager(), null);
    }

    private void showDone() {
        Intent i = new Intent(this, doneActivity.class);
        startActivity(i);

    }

    @Override
    public void applyText(String task, Boolean done) {


        Database database = new Database(to_do.this);
        SQLiteDatabase db = database.getReadableDatabase();
        ContentValues values=new ContentValues();
        values.put("TASK",task);
        values.put("DONE",done);
        db.insert("TASKS",null,values);
        finish();
        startActivity(getIntent());

    }


    private class MainAdapter extends BaseAdapter {

        ArrayList<String> tasks=new ArrayList<>();



        @Override
        public int getCount() {
            Database database = new Database(to_do.this);
            SQLiteDatabase db = database.getReadableDatabase();
            int n = 0;

            Cursor cursor = db.rawQuery("SELECT * FROM TASKS where DONE=? ",
                    new String[]{"0"});


            if (cursor != null) {

                cursor.moveToFirst();
            }
            while (cursor.moveToNext()){
                tasks.add(cursor.getString(1));
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
            View view = getLayoutInflater().inflate(R.layout.check_box_to_do, null);
            checkBox=view.findViewById(R.id.check_box);
            textTask=view.findViewById(R.id.task_text);
            textTask.setText(tasks.get(position));

            return view;
        }
    }

}
