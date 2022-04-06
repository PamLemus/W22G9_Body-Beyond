package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bodybeyond.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLogIn = findViewById(R.id.btnLogIn);
        TextView txtCreateAccount = findViewById(R.id.txtViewCreateAccount);
        btnLogIn.setOnClickListener((View btnView) -> {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
        txtCreateAccount.setOnClickListener((View txtView) -> {
            startActivity(new Intent(MainActivity.this, SignUp.class));
        });
    }
}