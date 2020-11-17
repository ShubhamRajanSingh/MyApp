package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class subTasksActivity extends AppCompatActivity implements dialogcustom.dialogCustomListener{

    Toolbar toolbar;
    GridView gridView;
    CheckBox checksubBox;
    TextView textsub;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_tasks);


        toolbar=findViewById(R.id.subtoolbar);
        gridView=findViewById(R.id.subjob_grid_view);
        Intent i=getIntent();
        Bundle extra=i.getExtras();

        toolbar.setTitle("{ "+(CharSequence) extra.get("Task")+" ;}");


        final subTasksActivity.MainAdapter adapter = new subTasksActivity.MainAdapter();
        gridView.setAdapter(adapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                String s = adapter.tasks.get(position);
                Database database = new Database(subTasksActivity.this);
                SQLiteDatabase db = database.getReadableDatabase();
                db.delete("JOBS", "SUBDOMAIN=?", new String[]{s});
                finish();
                startActivity(getIntent());

                return false;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String s=adapter.tasks.get(position);
                Database database = new Database(subTasksActivity.this);
                SQLiteDatabase db = database.getReadableDatabase();
                ContentValues values=new ContentValues();
                values.put("DONE",1);

                db.update("JOBS",values,"SUBDOMAIN=?",new String[]{s});




            }
        });

        FloatingActionButton fab = findViewById(R.id.addsub_fab_job);
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

        Database database = new Database(subTasksActivity.this);
        SQLiteDatabase db = database.getReadableDatabase();
        Intent i=getIntent();
        Bundle extra=i.getExtras();
        ContentValues values=new ContentValues();
        values.put("DOMAIN",extra.get("Task").toString());
        values.put("SUBDOMAIN",task);
        values.put("DONE",done);
        db.insert("JOBS",null,values);
        finish();
        startActivity(getIntent());

    }

    private class MainAdapter extends BaseAdapter {

        ArrayList<String> tasks=new ArrayList<>();
        ArrayList<Boolean> done=new ArrayList<>();



        @Override
        public int getCount() {
            Database database = new Database(subTasksActivity.this);
            SQLiteDatabase db = database.getReadableDatabase();
            Intent i=getIntent();
            Bundle extra=i.getExtras();
            int n = 0;

            Cursor cursor = db.rawQuery("SELECT  SUBDOMAIN,DONE FROM JOBS where DOMAIN=?",
                    new String[]{extra.get("Task").toString()});



            if (cursor != null) {

                cursor.moveToFirst();
            }
            while (cursor.moveToNext()){
                tasks.add(cursor.getString(0));
                done.add(cursor.getInt(1) > 0);
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

            checksubBox=view.findViewById(R.id.check_box);
            textsub=(TextView)view.findViewById(R.id.task_text);

            textsub.setText(tasks.get(position));
            if(done.get(position)){
                checksubBox.setChecked(true);
            }



            return view;
        }
    }
}
