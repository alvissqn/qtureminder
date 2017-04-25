package com.example.alviss.qtureminder.activity;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alviss.qtureminder.R;
import com.example.alviss.qtureminder.lib.AlarmReceiver;
import com.example.alviss.qtureminder.lib.JSONParser;
import com.example.alviss.qtureminder.lib.MyUtility;
import com.example.alviss.qtureminder.lib.Service_Url;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateTask extends AppCompatActivity {
    EditText edtCongviec, edtNoidung;
    TextView txtdate, txttime;
    Button btnDate, btnTime,btnCongviec;
    private ProgressDialog pDialog;

    public String linkcreatetask = Service_Url.createtask;
    public String mes_result="";
    public String congviec,noidung,ngay,time;
    public int taskid;
    JSONParser jsonParser = new JSONParser();

    MyUtility alarm = new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        edtCongviec = (EditText) findViewById(R.id.edtcongviec);
        edtNoidung = (EditText) findViewById(R.id.edtnoidung);
        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime = (TextView) findViewById(R.id.txttime);
        btnDate = (Button) findViewById(R.id.btndate);
        btnTime = (Button) findViewById(R.id.btntime);
        String time = MyUtility.getCurrentTime();
        txttime.setText(time);
        btnCongviec = (Button) findViewById(R.id.btncongviec);

        btnCongviec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddTask().execute();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdatePickerDialog();
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }
    /**
     * Hàm hiển thị DatePickerDialog
     */
    public void showdatePickerDialog(){
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int newmonth = month + 1;
                txtdate.setText(dayOfMonth + "-" + newmonth + "-" + year);
            }
        };
        int ngay,thang,nam;
        String date[] = txtdate.getText().toString().split("-");
        ngay = Integer.parseInt(date[0]);
        thang = Integer.parseInt(date[1])-1;
        nam = Integer.parseInt(date[2]);
        DatePickerDialog dateDialog = new DatePickerDialog(this, callback, nam, thang, ngay);
        dateDialog.setTitle("Chọn Ngày");
        dateDialog.show();
    }
    /**
     * Hàm hiển thị TimePickerDialog
     */
    public void showTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener callback=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {

                String h = ( hourOfDay >9 ) ? Integer.toString(hourOfDay) : "0" + hourOfDay;
                String m = ( minute >9 ) ? Integer.toString(minute) : "0" + minute;
                String time = h + ":" + m;

                txttime.setText(time);
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong TimePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s=txttime.getText()+"";

        String strArr[]=s.split(":");
        int gio=Integer.parseInt(strArr[0]);
        int phut=Integer.parseInt(strArr[1]);
        TimePickerDialog time=new TimePickerDialog(
                CreateTask.this,
                callback, gio, phut, true);
        time.setTitle("Chọn thời gian");
        time.show();
    }
    private class AddTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(CreateTask.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<NameValuePair> taskarr = new ArrayList<NameValuePair>();
            congviec = edtCongviec.getText().toString();
            noidung = edtNoidung.getText().toString();
            ngay = txtdate.getText().toString();
            time = txttime.getText().toString();

            String uid = String.valueOf(MainActivity.userid);
            taskarr.add(new BasicNameValuePair("congviec",congviec));
            taskarr.add(new BasicNameValuePair("noidung",noidung));
            taskarr.add(new BasicNameValuePair("ngay",ngay));
            taskarr.add(new BasicNameValuePair("thoigian", time));
            taskarr.add(new BasicNameValuePair("uid",uid));
            taskarr.add(new BasicNameValuePair("device","mobile"));
            JSONObject jsoncretask = jsonParser.makeHttpRequest(linkcreatetask,"POST", taskarr);

            int success = 0;
            try {
                success = jsoncretask.getInt("success");
                if(success == 1){
                    mes_result = jsoncretask.getString("message");
                    taskid = jsoncretask.getInt("idtask");
                    alarm.setAlarm(CreateTask.this,ngay,time,taskid);
                    finish();
                }
                else {
                    mes_result = jsoncretask.getString("message");
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            Intent i = new Intent(CreateTask.this, TaskActivity.class);
            startActivity(i);
            finish();
        }
    }
}

