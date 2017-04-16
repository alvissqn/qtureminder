package com.example.alviss.qtureminder.activity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.alviss.qtureminder.R;
import com.example.alviss.qtureminder.lib.RingtoneService;
import com.example.alviss.qtureminder.lib.SchedulingService;

public class Rington extends AppCompatActivity {
    Button Start,Stop;
    Ringtone ringTone;
    SchedulingService sv = new SchedulingService();
    Uri uriAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rington);
        Start = (Button) findViewById(R.id.btnStart);
        Stop = (Button) findViewById(R.id.btnStop);
        uriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(Rington.this,RingtoneService.class);
                startService(inten);
               // sv.runRingtone(Rington.this);
//                ringTone = RingtoneManager
//                        .getRingtone(getApplicationContext(), uriAlarm);
//                Toast.makeText(Rington.this,
//                        ringTone.getTitle(Rington.this),
//                        Toast.LENGTH_LONG).show();
//                ringTone.play();
            }
        });
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // sv.stopRingtone();
                Intent inten = new Intent(Rington.this,RingtoneService.class);
                stopService(inten);
            }
        });
    }

}
