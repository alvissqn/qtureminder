package com.example.alviss.qtureminder.lib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Alviss on 27/03/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    int id;
    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        id = intent.getIntExtra("idtask", 0);

        Intent ringtone = new Intent(context, RingtoneService.class);
            ringtone.putExtra("title", title);
            ringtone.putExtra("content", content);
            ringtone.putExtra("idtask", id);
            context.startService(ringtone);
        Toast.makeText(context,title +" "+content, Toast.LENGTH_SHORT).show();
    }

    public  void setAlarm(Context context,String ngayhen,String giohen,int id){

        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long interval = 3 * 1000;

        int ngay,thang,nam;

        String date[] = ngayhen.split("-");
        ngay = Integer.parseInt(date[0]);
        thang = Integer.parseInt(date[1])-1;
        nam = Integer.parseInt(date[2]);
        int gio,phut;
        String hour[] = giohen.split(":");
        gio = Integer.parseInt(hour[0]);
        phut = Integer.parseInt(hour[1]);

        /* Set the alarm to start*/

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, gio);
        calendar.set(Calendar.MINUTE, phut);
        calendar.set(Calendar.SECOND,00);
        calendar.set(Calendar.YEAR,nam);
        calendar.set(Calendar.MONTH,thang);
        calendar.set(Calendar.DATE,ngay);

        /* Repeating on every 3 minutes interval */
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval,pendingIntent);
    }
    public void cancel(Context context,int id) {
        // If the alarm has been set, cancel it.
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        if (alarmMgr!= null) {
            alarmMgr.cancel(pendingIntent);
        }

    }
}
