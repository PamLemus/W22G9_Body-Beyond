package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bodybeyond.R;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        TextView jaspalEmail = findViewById(R.id.txtmailJaspal);
        TextView jasmineEmail = findViewById(R.id.txtmailJasmine);
        TextView pamelaEmail = findViewById(R.id.txtmailPamela);
        ImageButton back = findViewById(R.id.imageButtoncontactusback);
        Linkify.addLinks(jaspalEmail, Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(jasmineEmail, Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(pamelaEmail, Linkify.EMAIL_ADDRESSES);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactActivity.this, HomeActivity.class));
            }
        });
    }
}