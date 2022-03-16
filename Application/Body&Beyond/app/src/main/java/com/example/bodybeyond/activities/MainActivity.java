package com.example.bodybeyond.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bodybeyond.HomeActivity;
import com.example.bodybeyond.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogIn = findViewById(R.id.btnLogIn);
        TextView txtCreateAccount = findViewById(R.id.txtViewCreateAccount);

        btnLogIn.setOnClickListener((View btnView) -> {
        startActivity(new Intent(MainActivity.this, Login.class));
        });

        txtCreateAccount.setOnClickListener((View txtView) -> {
            startActivity(new Intent(MainActivity.this, SignUp.class));
        });

        Button home = findViewById(R.id.btnHome);
        home.setOnClickListener((View view) -> {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));

        });

    }

}