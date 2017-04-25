package com.example.alviss.qtureminder.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.alviss.qtureminder.template.ListTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Update_TaskActivity extends AppCompatActivity {
    EditText edtCongviec, edtNoidung;
    TextView txtdate, txttime;
    Button btnDate, btnTime,btnCongviec;
    private ProgressDialog pDialog;
    private Context context = this;
    public String linkupdate = Service_Url.updatetask;
    public String linksingletask = Service_Url.singletask;
    public String mes_result="";
   // public String congviec,noidung,ngay,time;
    public int taskid;
    JSONParser jsonParser = new JSONParser();
    //AlarmReceiver alarm = new AlarmReceiver();
    MyUtility alarm = new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__task);
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
                new UpdateTask().execute();
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
        new GetTask().execute();
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

        System.out.println("Ngay :" + ngay + " thang :"+thang + " nam :"+nam);

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
        System.out.println("Thoi gian lay duoc la : " +s);
        String strArr[]=s.split(":");
        int gio=Integer.parseInt(strArr[0]);
        int phut=Integer.parseInt(strArr[1]);
        TimePickerDialog time=new TimePickerDialog(
                Update_TaskActivity.this,
                callback, gio, phut, true);
        time.setTitle("Chọn thời gian");
        time.show();
    }
    private class GetTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Update_TaskActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> taskone = new ArrayList<NameValuePair>();
            Service_Url svu = new Service_Url();
            Bundle bd = getIntent().getExtras();
            if(bd != null) {
                String idtask = String.valueOf(bd.getInt("id"));
                taskone.add(new BasicNameValuePair("taskid",idtask));
                taskid = bd.getInt("id");
            }
            try {
                JSONObject jsonObj = jsonParser.makeHttpRequest(linksingletask,"GET", taskone);
                Log.d("Create Response", jsonObj.toString());
                JSONArray taskresult = jsonObj.getJSONArray("answers");
                for(int i=0; i < taskresult.length(); i++){
                    JSONObject c = taskresult.getJSONObject(i);
                    String title_task = c.getString("titletask");
                    String content_task = c.getString("content_task");
                    String date_task = svu.Format_Date(c.getString("date_task"));
                    String time_task = svu.Format_time(c.getString("time_task"));
                    edtCongviec.setText(title_task);
                    edtNoidung.setText(content_task);
                    txtdate.setText(date_task);
                    txttime.setText(time_task);
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
        }
    }
    private class UpdateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Update_TaskActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String congviec,noidung,ngay,time;
            List<NameValuePair> taskarr = new ArrayList<NameValuePair>();
            congviec = edtCongviec.getText().toString();
            noidung = edtNoidung.getText().toString();
            ngay = txtdate.getText().toString();
            time = txttime.getText().toString();

            taskarr.add(new BasicNameValuePair("act","update"));
            taskarr.add(new BasicNameValuePair("congviec",congviec));
            taskarr.add(new BasicNameValuePair("noidung",noidung));
            taskarr.add(new BasicNameValuePair("ngay",ngay));
            taskarr.add(new BasicNameValuePair("thoigian", time));
            taskarr.add(new BasicNameValuePair("idtask", String.valueOf(taskid)));
            JSONObject jsoncretask = jsonParser.makeHttpRequest(linkupdate,"POST", taskarr);
            Log.d("Create Response", jsoncretask.toString());
            int success = 0;
            try {
                success = jsoncretask.getInt("success");
                if(success == 1){
                    mes_result = jsoncretask.getString("message");
                    taskid = jsoncretask.getInt("idtask");
                    alarm.setAlarm(Update_TaskActivity.this,ngay,time,taskid);
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

            Toast.makeText(context, mes_result, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Update_TaskActivity.this, TaskActivity.class);
            startActivity(i);
        }
    }
}
