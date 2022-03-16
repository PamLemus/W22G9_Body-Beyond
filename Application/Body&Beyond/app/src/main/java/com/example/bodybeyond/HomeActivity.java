package com.example.bodybeyond;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;

import com.example.bodybeyond.activities.CalculateBMIActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actBar = getSupportActionBar();
        actBar.setDisplayShowHomeEnabled(true);
        actBar.setDisplayUseLogoEnabled(true);
        actBar.setLogo(R.drawable.whitebackgr_image);
        actBar.setTitle("Welcome, Jasmine"); //Read the name from DB
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFCF4"));
        actBar.setBackgroundDrawable(colorDrawable);

        //Changing Text Color of the Action Bar
        int black = Color.BLACK;
        setActionbarTextColor(actBar, black);

    }

    //Method to change text color from Action Bar
    private void setActionbarTextColor(ActionBar actBar, int color) {

        String title = actBar.getTitle().toString();
        Spannable spannablerTitle = new SpannableString(title);
        spannablerTitle.setSpan(new ForegroundColorSpan(color), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actBar.setTitle(spannablerTitle);

        Button calculate = findViewById(R.id.btnCalculateAgain);
        calculate.setOnClickListener((View view) -> {
              startActivity(new Intent(HomeActivity.this, CalculateBMIActivity.class));

        });

    }
}