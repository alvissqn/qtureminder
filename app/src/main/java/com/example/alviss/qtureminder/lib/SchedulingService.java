package com.example.alviss.qtureminder.lib;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.alviss.qtureminder.R;
import com.example.alviss.qtureminder.activity.MainActivity;

/**
 * Created by Alviss on 27/03/2017.
 */

public class SchedulingService extends IntentService {
    private NotificationManager mNotificationManager;
    private String tieude,noidung;
    private int idtask;
    Ringtone r;
    Uri alert;

    public SchedulingService(){
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (intent.getStringExtra("title") != null) {
            tieude = intent.getStringExtra("title");//get data here sended from BroadcastReceiver
            noidung = intent.getStringExtra("content");
            idtask =intent.getIntExtra("idtask",0);
        }
        sendNotification(tieude,noidung,idtask);
        runRingtone();
    }

    private void sendNotification(String content, String title, int taskid) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.todo)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(content))
                        .setAutoCancel(true)
//                      .addAction(R.drawable.alarm,"STOP",pendingIntent)
                        .setContentText(content);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(taskid, mBuilder.build());
    }
    public void runRingtone() {
        r = RingtoneManager.getRingtone(getBaseContext(),alert);
//        if (r == null) {
//
//            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            r = RingtoneManager.getRingtone(context, alert);
//
//            if (r == null) {
//                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                r = RingtoneManager.getRingtone(context, alert);
//            }
//        }
//        if (r != null)
            r.play();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRingtone();
    }

    public void stopRingtone(){
        if(r != null) {
            r.stop();
            r = null;
        }
    }

}
