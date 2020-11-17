package com.example.myapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notifications extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Database database = new Database(context);
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



        if(n>1){


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "MyChannel";
                String description = "MyChannel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("MyChannel", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            Uri uri = Uri.parse("android.resource://"
                    + context.getPackageName() + "/" + R.raw.alertsound);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "MyChannel")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setContentTitle("Tasks")
                    .setContentText(""+n+" Task Baki hai")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(uri);

            NotificationManagerCompat manager=NotificationManagerCompat.from(context);
            manager.notify(999,builder.build());

        }


        Intent intent1 = new Intent(context, Notifications.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent1, 0);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+3600000, pendingIntent);
    }
}
