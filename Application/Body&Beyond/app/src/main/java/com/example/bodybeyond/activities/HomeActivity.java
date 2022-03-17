package com.example.bodybeyond.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.bodybeyond.R;
import com.example.bodybeyond.adapters.ViewPagerDietExerciseAdapter;
import com.example.bodybeyond.fragments.DietFragment;
import com.example.bodybeyond.fragments.ExerciseFragment;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayoutDietExercise;
    ViewPager viewPagerDietExercise;

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

        ProgressBar progressBarSteps = findViewById(R.id.progressBarSteps);
        progressBarSteps.setMax(100);  // target steps
        progressBarSteps.setProgress(80);  //steps walked

        tabLayoutDietExercise = findViewById(R.id.tabLayoutDietExercise);
        viewPagerDietExercise = findViewById(R.id.viePagerDietExercise);

        tabLayoutDietExercise.setupWithViewPager(viewPagerDietExercise);
        ViewPagerDietExerciseAdapter vpDEAdapter = new ViewPagerDietExerciseAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpDEAdapter.addFragmentsDE(new DietFragment(), "Diet");
        vpDEAdapter.addFragmentsDE(new ExerciseFragment(), "Exercise");
        viewPagerDietExercise.setAdapter(vpDEAdapter);


    }
}