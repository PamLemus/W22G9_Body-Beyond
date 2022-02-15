package com.example.bodybeyond;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    final String TAG = "LOGIN_ACTIVITY";
    EditText emailId;
    EditText password;
    Button btnLogIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailId = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPwd);
        btnLogIn = findViewById(R.id.btnLogIn);

        btnLogIn.setOnClickListener((View view) -> {

            String email = emailId.getText().toString();
            String pwd = password.getText().toString();

            if(email.isEmpty())
            {
                Toast.makeText(this, "Please enter valid Email address.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Empty email id field.");
            }
            else if(pwd.isEmpty())
            {
                Toast.makeText(this, "Please enter valid Password.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Empty password field.");
            }
            else{
                Log.d(TAG, "User Name: " + email + "  " + "Pwd: " + pwd);
            }
        });
    }
}