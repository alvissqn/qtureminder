package com.example.alviss.qtureminder.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.alviss.qtureminder.R;
import com.example.alviss.qtureminder.lib.AlarmReceiver;
import com.example.alviss.qtureminder.lib.JSONParser;
import com.example.alviss.qtureminder.lib.ListAdapter;
import com.example.alviss.qtureminder.lib.MyUtility;
import com.example.alviss.qtureminder.lib.RingtoneService;
import com.example.alviss.qtureminder.lib.SchedulingService;
import com.example.alviss.qtureminder.lib.Service_Url;
import com.example.alviss.qtureminder.template.ListTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alviss on 06/04/2017.
 */

public class TaskActivity extends AppCompatActivity {
    private SwipeMenuListView lv;
    private List<ApplicationInfo> mListTask;
    private ProgressDialog pDialog;
    private int getidtask;
    Button btnCreateViec;
    private int[] taskid;
    private String[] datetask, hourtask;
    int uid;
    JSONParser jsonParser = new JSONParser();
    public String mes_result="";
    public String linklist = Service_Url.listtask;
    ArrayList<ListTask> mang_task;
    Context context = this;
    MyUtility alarm = new MyUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);
        mListTask = getPackageManager().getInstalledApplications(0);
        lv = (SwipeMenuListView) findViewById(R.id.lvDsviec);
        btnCreateViec = (Button) findViewById(R.id.btnCreateViec);
        btnCreateViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateTask.class);
                startActivity(i);
            }
        });
        mang_task = new ArrayList<ListTask>();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Laylistitem().execute(linklist);
            }
        });

        /** Tao swipe **/
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                //openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // set item icon
                openItem.setIcon(R.drawable.ic_edit);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        lv.setMenuCreator(creator);

        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                //ApplicationInfo item = mListTask.get(position);
                switch (index) {
                    case 0:
                        // Edit
                        int id = taskid[position];
                        Intent update = new Intent(TaskActivity.this,Update_TaskActivity.class);
                        update.putExtra("id",id);
                        startActivity(update);
                        break;
                    case 1:
                        // delete
                        new XoaTask().execute(String.valueOf(taskid[position]));
                        Intent intent = new Intent(TaskActivity.this, RingtoneService.class);
                        stopService(intent);
                        break;
                }
                return false;
            }
        });

        /** Kết thúc tạo swipe **/


    }
    private class Laylistitem extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TaskActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> listtask = new ArrayList<NameValuePair>();
            uid = MainActivity.userid;

            listtask.add(new BasicNameValuePair("listitem",String.valueOf(uid)));
            try {

                JSONObject jsonObj = jsonParser.makeHttpRequest(params[0],"GET", listtask);
                Log.d("Create Response", jsonObj.toString());
                JSONArray task = jsonObj.getJSONArray("answers");
                taskid = new int[task.length()];
                hourtask = new String[task.length()];
                datetask = new String[task.length()];
                for(int i=0; i < task.length(); i++){
                    JSONObject c = task.getJSONObject(i);
                    String title_task = c.getString("titletask");
                    String date_task = c.getString("date_task");
                    String time_task = c.getString("time_task");
                   // String thietbi = c.getString("device");
                    taskid[i] = c.getInt("taskid");
                    datetask[i] = c.getString("date_task");
                    hourtask[i] = c.getString("time_task");
//                    if(thietbi.equals("website")) {
//                        alarm.setAlarm(TaskActivity.this,date_task,time_task,taskid[i]);
//                    }

                    mang_task.add(new ListTask(title_task,date_task,time_task));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new ListAdapter(TaskActivity.this, R.layout.activity_line_task, mang_task);
                    lv.setAdapter(adapter);
                }
            });
//            for (int j = 0; j < taskid.length; j++){
//                alarm.setAlarm(TaskActivity.this,datetask[j],hourtask[j],taskid[j]);
//            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private class XoaTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TaskActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> listtask = new ArrayList<NameValuePair>();
            listtask.add(new BasicNameValuePair("taskid",params[0]));
            getidtask = Integer.valueOf(params[0]);
            try {
                JSONObject jsonObj = jsonParser.makeHttpRequest(Service_Url.deletetask,"GET", listtask);
                Log.d("Create Response", jsonObj.toString());
                alarm.cancel(context,getidtask);
                mes_result = jsonObj.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            Toast.makeText(TaskActivity.this, mes_result, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(TaskActivity.this,TaskActivity.class);
            startActivity(i);
            finish();
        }
    }

}
