package com.example.alviss.qtureminder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alviss.qtureminder.R;
import com.example.alviss.qtureminder.lib.JSONParser;
import com.example.alviss.qtureminder.lib.Service_Url;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText etUser, etPass;
    private Button btnLogin;
    private TextView txtRegister;
    public static int userid;
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    private static String link = Service_Url.login;
    int flag=0;
    JSONParser jsonParser = new JSONParser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login Screen!!!");
        etUser = (EditText) findViewById(R.id.txtUser);
        etPass = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtvRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LayUser().execute();
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });

    }
    // Triggers when LOGIN Button clicked

    private class LayUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Sig in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
                    List<NameValuePair> listarr = new ArrayList<NameValuePair>();
                    String user = etUser.getText().toString();
                    String pass = etPass.getText().toString();
                    listarr.add(new BasicNameValuePair("username",user));
                    listarr.add(new BasicNameValuePair("password",pass));
                    JSONObject json = jsonParser.makeHttpRequest(link,"POST", listarr);
                    Log.d("Create Response", json.toString());
                    try {
                        int success = json.getInt("success");
                        userid = json.getInt("userid");
                        if (success == 1)
                        {
                            flag=0;
                            Intent i = new Intent(getApplicationContext(),TaskActivity.class);
                            //i.putExtra("iduser",userid);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            // failed to Sign in
                            flag=1;
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(flag==1)
                Toast.makeText(MainActivity.this,"Please Enter Correct informations", Toast.LENGTH_LONG).show();
        }
    }

//    private class Login extends AsyncTask<String, String, String>
//    {
//        @Override
//        protected String doInBackground(String... params) {
//            HttpHandler sh = new HttpHandler();
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(link);
//
//            Log.e(TAG, "Response from url: " + jsonStr);
//            if(jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    if(jsonObj.getInt("success") == 1) {
//                        startActivity(new Intent(MainActivity.this,ListItem.class));
//                    } else {
//                        Toast.makeText(MainActivity.this, "Login Không Thành Công", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            Toast.makeText(MainActivity.this, "Đã Load Xong", Toast.LENGTH_LONG).show();
//        }
//    }


}
