package com.example.alviss.qtureminder.lib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Alviss on 25/03/2017.
 */

public final class MyUtility {
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    public static String getCurrentTime(){
        String hour24;
        //Lấy đối tượng Calendar ra, mặc định ngày hiện tại
        Calendar now = Calendar.getInstance();

        //Muốn xuất Giờ:Phút theo kiểu 24h
        String strDateFormat24 = "HH:mm";
        SimpleDateFormat sdf = null;

        //Tạo đối tượng SimpleDateFormat với định dạng 24
        sdf = new SimpleDateFormat(strDateFormat24);
        hour24 = sdf.format(now.getTime());
        return hour24;
    }
    public static String getCurrentDate(){
        String date;
        //Lấy đối tượng Calendar ra, mặc định ngày hiện tại
        Calendar now = Calendar.getInstance();
        //Lấy ngày theo kiểu ngày/tháng/năm
        String strDateFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        date = sdf.format(now.getTime());
        return date;
    }
    public static String getCurrentDateTime(){
        String datetime;
        //Lấy đối tượng Calendar ra, mặc định ngày hiện tại
        Calendar now = Calendar.getInstance();
        //Lấy ngày theo kiểu ngày/tháng/năm
        String strDateFormat = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        datetime = sdf.format(now.getTime());
        return datetime;
    }


    public  void setAlarm(Context context, String ngayhen, String giohen, int id){

        alarmMgr = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        long interval = 1000 * 60 * 1;

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

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

        calendar.set(Calendar.HOUR_OF_DAY, gio);
        calendar.set(Calendar.MINUTE, phut);
        calendar.set(Calendar.SECOND,00);
        calendar.set(Calendar.YEAR,nam);
        calendar.set(Calendar.MONTH,thang);
        calendar.set(Calendar.DATE,ngay);

        /* Repeating on every 1 minutes interval */
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval,pendingIntent);

//        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
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
