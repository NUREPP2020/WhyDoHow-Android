package com.example.test;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Headers;
import okhttp3.ResponseBody;

public class LoginActivity extends Activity {

    final String TAG = this.getClass().getSimpleName();
    int flag;

    // Email, password edittext
    EditText txtEmail, txtPassword;

    String name;

    // login button
    Button btnLogin;

    // Session Manager Class
    SessionManager session;

    TextView textView3;

    TextView textView2;

    String url = "http://i668320w.beget.tech/API/check_pass_login.php?";

    String getEmail(){
        return txtEmail.toString();
    }

    String getName(){
        return name;
    }

    void setName(String name){
        this.name = name;
    }

    void setFail(){
        flag = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     //   StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
     //       @Override
     //       public void onResponse(String response) {
     //           Log.d(TAG, response);
     //       }
     //   }, new Response.ErrorListener() {
     //       @Override
     //      public void onErrorResponse(VolleyError error) {
     //          Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
     //       }
     //   });
        //RequestQueue queue = Volley.newRequestQueue(this);
        //queue.add(stringRequest);

    //    MySingleton.getInstance(this).addToRequestQueue(stringRequest);

        // Email, Password input text
        txtEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        txtPassword = (EditText) findViewById(R.id.editTextTextPassword);


        // Login button
        btnLogin = (Button) findViewById(R.id.button);


        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get username, password from EditText
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                boolean validEmail = Pattern.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", email);
                boolean validPass = Pattern.matches("(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*]).{8,16}$", password);
                // Check if username, password is filled
                if(email.trim().length() > 0 && password.trim().length() > 0){
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test
                    // Creating user login session
                    // For testing i am storing name, email as follow
                    // Use user real data
                    session = new SessionManager(getApplicationContext());
                    session.createLoginSession("test", email);

                    // Starting LoggedActivity
                    Intent i = new Intent(getApplicationContext(), LoggedActivity.class);
                    startActivity(i);
                    finish();
                    //checkLogin(v);

                }else{
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    Toast.makeText(LoginActivity.this,"Неправильный логин/пароль", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    public void checkLogin(View arg0) {

        // Get text from email and password field
        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();

        // Initialize  AsyncLogin() class with email and password
        new LoginAsync().execute(email,password);

    }

    public class LoginAsync  extends AsyncTask<String, String, String>  {

        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your php file resides
                url = new URL("https://whydohow.000webhostapp.com/Androide/test.php?");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }

            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();


                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessfulshsfhsf");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            } finally {
                conn.disconnect();
            }
        }


        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();

            if(result.equalsIgnoreCase("true"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                //session = new SessionManager(getApplicationContext());
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                // Creating user login session
                // For testing i am storing name, email as follow
                // Use user real data
                //session.createLoginSession("sf", getEmail());

                // Starting LoggedActivity
                // Intent i = new Intent(getApplicationContext(), LoggedActivity.class);
                //startActivity(i);
                //finish();

                //Intent intent = new Intent(MainActivity.this,SuccessActivity.class);
                //startActivity(intent);
                //MainActivity.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                setFail();

                // If username and password does not match display a error message
                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                setFail();
                //"OOPs! Something went wrong. Connection Problem."
                Toast.makeText(LoginActivity.this, "No connection", Toast.LENGTH_LONG).show();

            }
        }

    }

    public void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}