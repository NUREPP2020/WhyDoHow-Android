package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends Activity {

    TextView textView3;

    EditText txtName, txtEmail, txtPassword, txtConfirmation;

    SessionManager session;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textView3 = (TextView) findViewById(R.id.textView3);
        button = (Button) findViewById(R.id.button);

        txtName = (EditText) findViewById(R.id.editTextTextPersonName);
        txtEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        txtPassword = (EditText) findViewById(R.id.editTextTextPassword2);
        txtConfirmation = (EditText) findViewById(R.id.editTextTextPassword);

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username, email, password from EditText
                String name = txtName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmation = txtConfirmation.getText().toString().trim();

                boolean validName = Pattern.matches("[A-Za-zА-Яа-яЁёІіЇїЄє ]+", name);
                boolean validEmail = Pattern.matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", email);
                boolean validPass = Pattern.matches("(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*]).{8,16}$", password);
                boolean validConf = password.equals(confirmation);

                // Check if username, email, password valid
                if(validName && validEmail && validPass && validConf){
                    // For testing puspose username, password is checked with sample data
                    // username = test
                    // password = test
                    // Creating user login session
                    // For testing i am storing name, email as follow
                    // Use user real data
                    session = new SessionManager(getApplicationContext());
                    session.createLoginSession(name, email);

                    // Starting LoggedActivity
                    Intent i = new Intent(getApplicationContext(), LoggedActivity.class);
                    startActivity(i);
                    finish();
                    //checkLogin(v);

                }else{
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    Toast.makeText(getApplicationContext(),"Введите данные в нужном формате", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}