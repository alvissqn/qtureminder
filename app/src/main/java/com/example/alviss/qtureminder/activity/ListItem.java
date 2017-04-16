package com.example.alviss.qtureminder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.alviss.qtureminder.R;
import com.example.alviss.qtureminder.lib.JSONParser;
import com.example.alviss.qtureminder.lib.ListAdapter;
import com.example.alviss.qtureminder.lib.Service_Url;
import com.example.alviss.qtureminder.template.ListTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListItem extends AppCompatActivity {
    private ListView lv;
    private ProgressDialog pDialog;
    Button btnCreateViec;
    int uid;
    JSONParser jsonParser = new JSONParser();
    public String linklist = Service_Url.listtask;
    ArrayList<ListTask> mang_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);
        lv = (ListView) findViewById(R.id.lvDsviec);
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

    }
    private class Laylistitem extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListItem.this);
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
                for(int i=0; i < task.length(); i++){
                    JSONObject c = task.getJSONObject(i);
                    String title_task = c.getString("titletask");
                    String date_task = c.getString("date_task");
                    String time_task = c.getString("time_task");
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
                    ListAdapter adapter = new ListAdapter(ListItem.this, R.layout.activity_line_task,mang_task);
                    lv.setAdapter(adapter);
                }
            });
        }
    }
}
