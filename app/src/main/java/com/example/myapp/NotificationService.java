package com.example.myapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Database database = new Database(this);
        SQLiteDatabase db = database.getReadableDatabase();
        int n = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM TASKS where DONE=? ",
                new String[]{"0"});

        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (cursor.moveToNext()){
            n++;
        }
        Log.d("noti",""+n);


        if(n>1){
            Log.d("noti","Notification Check 1");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "MyChannel";
                String description = "MyChannel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("MyChannel", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            Uri uri = Uri.parse("android.resource://"
                    + this.getPackageName() + "/" + R.raw.alertsound);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyChannel")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle("Tasks")
                    .setContentText(""+n+" Task Baki hai")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(uri);

            NotificationManagerCompat manager=NotificationManagerCompat.from(this);
            manager.notify(999,builder.build());

        }

        return START_STICKY;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
