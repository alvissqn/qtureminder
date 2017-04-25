package com.example.alviss.qtureminder.lib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;


import android.widget.Toast;

import java.util.Calendar;


/**
 * Created by Alviss on 27/03/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
   // int id;
    @Override
    public void onReceive(Context context, Intent intent) {

//            String title = intent.getStringExtra("title");
//            String content = intent.getStringExtra("content");
//            id = intent.getIntExtra("idtask", 0);

            Intent ringtone = new Intent(context, RingtoneService.class);
//            ringtone.putExtra("title", title);
//            ringtone.putExtra("content", content);
//            ringtone.putExtra("idtask", id);
            context.startService(ringtone);
        Calendar c = Calendar.getInstance();

        String time = String.valueOf(c.get(Calendar.HOUR));
        String minute = String.valueOf(c.get(Calendar.MINUTE));
        String second = String.valueOf(c.get(Calendar.SECOND));
        Toast.makeText(context, "Việc Cần Làm Lúc "+time+":"+minute+":"+second, Toast.LENGTH_LONG).show();

    }


}
