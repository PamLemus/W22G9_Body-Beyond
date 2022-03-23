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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bodybeyond.R;
import com.example.bodybeyond.adapters.ViewPagerDietExerciseAdapter;
import com.example.bodybeyond.fragments.DietFragment;
import com.example.bodybeyond.fragments.ExerciseFragment;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayoutDietExercise;
    ViewPager viewPagerDietExercise;
    TextView BMIResult;
    TextView BMIDescrp;
    ImageView BMIImgResult;

    private static final DecimalFormat df = new DecimalFormat("0.0");


    String gender;
    double height = 147;
    double weight = 45.8;
    double BMIcalculation;
    int age = 49;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actBar = getSupportActionBar();
        actBar.setDisplayShowHomeEnabled(true);
        actBar.setDisplayUseLogoEnabled(true);
        actBar.setLogo(R.drawable.hamburger);
        actBar.setTitle("  Welcome, Jasmine"); //Read the name from DB
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFCF4"));
        actBar.setBackgroundDrawable(colorDrawable);

        //Changing Text Color of the Action Bar
        int black = Color.BLACK;
        setActionbarTextColor(actBar, black);

        //Button Calculate Again Click Listener
        Button calculate = findViewById(R.id.btnCalculateAgain);
        calculate.setOnClickListener((View view) -> {
            startActivity(new Intent(HomeActivity.this, CalculateBMIActivity.class));

        });

       //Porgress Bar configuration
        ProgressBar progressBarSteps = findViewById(R.id.progressBarSteps);
        progressBarSteps.setMax(100);  // target steps
        progressBarSteps.setProgress(80);  //steps walked

        //Configuration for TabLayout
        tabLayoutDietExercise = findViewById(R.id.tabLayout);
        viewPagerDietExercise = findViewById(R.id.viewPager);

        tabLayoutDietExercise.setupWithViewPager(viewPagerDietExercise);
        ViewPagerDietExerciseAdapter vpDEAdapter = new ViewPagerDietExerciseAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpDEAdapter.addFragmentsDE(new DietFragment(), "Diet");
        vpDEAdapter.addFragmentsDE(new ExerciseFragment(), "Exercise");
        viewPagerDietExercise.setAdapter(vpDEAdapter);

        //Display BMI result, number
        BMIResult = findViewById(R.id.txtBMIResult);
        double BMIvalue = calculateBMI(height,weight);
        BMIResult.setText(df.format(BMIvalue));

        //Call Method to establish BMI Description and corresponding image
        BMIDescriptionLogic(age, BMIvalue);


    }

    //Method to change text color from Action Bar
    private void setActionbarTextColor(ActionBar actBar, int color) {
        String title = actBar.getTitle().toString();
        Spannable spannablerTitle = new SpannableString(title);
        spannablerTitle.setSpan(new ForegroundColorSpan(color), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actBar.setTitle(spannablerTitle);
    }
    //Method to calculate BMI Number
    public double calculateBMI (double height, double weight) {
        double cmHeight = (height/100);
        double powerHeight = Math.pow(cmHeight,2);
        BMIcalculation = (weight/powerHeight);
        return BMIcalculation;
    }

    //Method to display BMI Description
    public void BMIDescriptionLogic (int age, double BMIvalue) {
        String BMIDescription = "";
        String suggestedAction = "";
        BMIImgResult = findViewById(R.id.imgBMIresult);

        // Results for Adults
        if (age >= 21) {
            if (BMIvalue < 18.5) {
                BMIDescription = "Underweight = less than 18.5";
                suggestedAction = "GAIN Weight";
                BMIImgResult.setImageResource(R.drawable.under_weight);
            } if (BMIvalue >= 18.5) {
                BMIDescription = "in Normal Weight = from 18.5 to 24.9";
                suggestedAction = "MAINTAIN Weight";
                BMIImgResult.setImageResource(R.drawable.normal);
            } if (BMIvalue >= 25) {
                BMIDescription = "Overweight = from 25 to 29.9";
                suggestedAction = "LOSE Weight";
                BMIImgResult.setImageResource(R.drawable.over_weight);
            }  if (BMIvalue >= 30) {
                BMIDescription = "Obese = greater than or equal to 30";
                suggestedAction = "LOSE Weight";
                BMIImgResult.setImageResource(R.drawable.obese);
            } }

        else if (age <21) {
            if (age >= 2) {   // Results for Children 1st Range
            if (BMIvalue <= 13.5) {
                BMIDescription = "Underweight = Less than or equal to 13.5";
                suggestedAction = "GAIN Weight";
                BMIImgResult.setImageResource(R.drawable.under_weight);
            } if (BMIvalue >= 13.6) {
                BMIDescription = "in Normal Weight = From 13.6 to 17.5";
                suggestedAction = "MAINTAIN Weight";
                BMIImgResult.setImageResource(R.drawable.normal);
            } if (BMIvalue >= 17.6) {
                BMIDescription = "Overweight = From 17.6 to 18.5";
                suggestedAction = "LOSE Weight";
                BMIImgResult.setImageResource(R.drawable.over_weight);
            }  if (BMIvalue > 18.5 ) {
                BMIDescription = "Obese = More than 18.5";
                suggestedAction = "LOSE Weight";
                BMIImgResult.setImageResource(R.drawable.obese);
            }
            }

        if (age >= 8) {  // Results for Children 2nd Range
            if (BMIvalue <= 14.5) {
                BMIDescription = "Underweight = Less than or equal to 14.5";
                suggestedAction = "GAIN Weight";
                BMIImgResult.setImageResource(R.drawable.under_weight);
            } if (BMIvalue >= 14.6) {
                BMIDescription = "in Normal Weight = From 14.6 to 20.5";
                suggestedAction = "MAINTAIN Weight";
                BMIImgResult.setImageResource(R.drawable.normal);
            } if (BMIvalue >= 20.6) {
                BMIDescription = "Overweight = From 20.6 to 24.5";
                suggestedAction = "LOSE Weight";
                BMIImgResult.setImageResource(R.drawable.over_weight);
            }  if (BMIvalue > 24.5 ) {
                BMIDescription = "Obese = More than 24.5";
                suggestedAction = "LOSE Weight";
                BMIImgResult.setImageResource(R.drawable.obese);
            }
        }

        if (age >= 15) {  // Results for Children 3rd Range
            if (BMIvalue <= 17.5) {
                BMIDescription = "Underweight = Less than or equal to 17.5";
                suggestedAction = "GAIN Weight";
                BMIImgResult.setImageResource(R.drawable.under_weight);
            } if (BMIvalue >= 17.6) {
                BMIDescription = "in Normal Weight = From 17.6 to 25.5";
                suggestedAction = "MAINTAIN Weight";
                BMIImgResult.setImageResource(R.drawable.normal);
            } if (BMIvalue >= 25.6) {
                BMIDescription = "Overweight = From 25.6 to 29.5";
                suggestedAction = "LOSE Weight";
                BMIImgResult.setImageResource(R.drawable.over_weight);
            }  if (BMIvalue > 29.5 ) {
                BMIDescription = "Obese = More than 29.5";
                suggestedAction = "LOSE Weight";
                BMIImgResult.setImageResource(R.drawable.obese);
            }
        }
        }

        BMIDescrp = findViewById(R.id.txtBMIResultDescr);
        BMIDescrp.setText("You are " + BMIDescription + "\n You need to " + suggestedAction);
    }


}