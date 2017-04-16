package com.example.alviss.qtureminder.lib;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.alviss.qtureminder.R;
import com.example.alviss.qtureminder.activity.MainActivity;
import com.example.alviss.qtureminder.activity.TaskActivity;

/**
 * Created by Alviss on 02/04/2017.
 */

public class RingtoneService extends Service {
    //private MediaPlayer media_song;
    private NotificationManager mNotificationManager;
    Ringtone r;
    private String tieude,noidung;
    private int idtask;
    Uri alert;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("title") != null) {
            tieude = intent.getStringExtra("title");//get data here sended from BroadcastReceiver
            noidung = intent.getStringExtra("content");
            idtask =intent.getIntExtra("idtask",0);
            sendNotification(noidung,tieude,idtask);
        }
       // media_song = MediaPlayer.create(this, R.raw.cucku);
       // media_song.start();
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        runRingtone();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, "On destroy called", Toast.LENGTH_SHORT).show();
        stopRingtone();
    }

    private void sendNotification(String content,String title,int idnotifi) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, idnotifi,
                new Intent(this, TaskActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.todo)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(content))
                        .setAutoCancel(true)
                        .setContentText(content);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(idnotifi, mBuilder.build());
    }
    public void runRingtone() {
        r = RingtoneManager.getRingtone(getBaseContext(),alert);
        r.play();
    }
    public void stopRingtone(){
        r.stop();
    }

}
