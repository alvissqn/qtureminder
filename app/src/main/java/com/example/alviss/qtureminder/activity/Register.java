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

public class Register extends AppCompatActivity {
    EditText etuser, etpass;
    Button btnreg;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    int flag=0;
    //public String linkreg = "http://192.168.1.221/json/register.php";
    public String linkreg = Service_Url.register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etuser = (EditText)findViewById(R.id.etUsername);
        etpass = (EditText)findViewById(R.id.etPassword);
        btnreg = (Button)findViewById(R.id.btnReg);
        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dangkinick().execute();
            }
        });
    }
    class Dangkinick extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Register ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> listarr = new ArrayList<NameValuePair>();
            String user = etuser.getText().toString();
            String pass = etpass.getText().toString();
            listarr.add(new BasicNameValuePair("username",user));
            listarr.add(new BasicNameValuePair("password",pass));
            JSONObject json = jsonParser.makeHttpRequest(linkreg,"POST", listarr);
            Log.d("Create Response", json.toString());
            try {
                int success = json.getInt("success");
                if (success == 1)
                {
                    flag=0;
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);

//                        i.putExtra("mobile_number",number);
//                        i.putExtra("password",pwd);

                    finish();
                }
                else
                {
                    // failed to Sign in
                    flag=1;
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
            if(flag==1)
                Toast.makeText(Register.this, "Please Enter Correct informations", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Register.this, "Register Success. Thanks You!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
