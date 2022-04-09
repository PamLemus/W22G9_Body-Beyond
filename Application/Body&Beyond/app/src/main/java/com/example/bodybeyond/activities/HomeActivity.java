package com.example.bodybeyond.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.example.bodybeyond.R;
import com.example.bodybeyond.adapters.ViewPagerDietExerciseAdapter;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.fragments.DietFragment;
import com.example.bodybeyond.fragments.ExerciseFragment;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity implements SensorEventListener {

    TabLayout tabLayoutDietExercise;
    ViewPager viewPagerDietExercise;
    TextView BMIResult;
    TextView BMIDescrp;
    ImageView BMIImgResult;
    TextView txtSuggestedCaloriesIntake;
    TextView txtSuggSteps;
    TextView txtCurrentSteps;
    TextView txtBurnedCalSteps;
    TextView stepProgressPercentage;

    private static final DecimalFormat df = new DecimalFormat("0.0");
    private static final DecimalFormat rf = new DecimalFormat("0,000");
    private static final DecimalFormat pf = new DecimalFormat("0%");

    String gender;  //Value limited to "M"(male) or "F"(female)
    double height;
    double weight;
    double BMIcalculation;
    int age;
    String BMIDescription;
    String suggestedAction;
    String activity;  //Value limited to "Light" or "Moderate" or "Active"
    double suggCalIntakeFinal;
    double targetStepsPerDay;
    int currentSteps;

    //Jasmine - Navigation drawer
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView email;
    String useremail;
    String username;
    User userObj;
    String range;


    //Jaspal's Step Counter Implementation
    SensorManager sensorManager;
    boolean running = false;
    int steps;
    SharedPreferences prefs = null;
    SharedPreferences.Editor editor;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    ProgressBar progressBarSteps;
    private int progressStatus;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        prefs = getApplicationContext().getSharedPreferences("LOCAL_STORAGE", MODE_PRIVATE);
        editor = prefs.edit();

         SharedPreferences  sharedPreferences = getSharedPreferences("USER_EMAIL", MODE_PRIVATE);
         useremail = sharedPreferences.getString("EMAIL","null");

        //Jasmine - navigation drawer method call
        NavDrawer();
        SetUpToolbar();

        //Changing Text Color of the Action Bar
        int black = Color.BLACK;

        //Button Calculate Again Click Listener
        Button calculate = findViewById(R.id.btnCalculateAgain);
        calculate.setOnClickListener((View view) -> {
            getSharedPreferences("SIGNUP_PREF", MODE_PRIVATE ).edit().clear().apply();
            startActivity(new Intent(HomeActivity.this, CalculateBMIActivity.class));
        });

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

        //Call Method to calculate suggested Calories Intake
        BMRCalculation(age, height, weight, gender, activity);


        //step counter progress bar implementation.
        currentSteps = progressStatus;
        progressBarSteps = findViewById(R.id.progressBarSteps);
        progressStatus = prefs.getInt("steps", 0);
        currentSteps = progressStatus;

        CustomScheduler();

        //Call Method to calculate and display target steps per day
        txtCurrentSteps = findViewById(R.id.txtCurrentSteps);
        targetSteps (activity, currentSteps);
        progressBarSteps.setMax((int)targetStepsPerDay);  // target steps
        progressBarSteps.setProgress(progressStatus);  //steps walked

        //Definition of Ranges according with Suggested Calories Intake
        if (suggCalIntakeFinal < 1500) {
            range = "Range 1";
        } else if (suggCalIntakeFinal <= 2350) {
            range = "Range 2";
        } else if (suggCalIntakeFinal > 2350) {
            range = "Range 3";
        }
        UserCalRangePref(range);

        //Call the method to get the activity
        UserActivity(activity);
        GetNotification("Welcome "+ username + " to Body And Beyond");
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

    //Method to calculate Basal Metabolic Rate(BMR) base on Mifflin-St Jeor Equation
    public void BMRCalculation (int age, double height, double weight, String gender, String activity) {
        double maleBMRFormula = ((10*weight)+(6.25*height)-(5*age)+5);
        double femaleBMRFormula = ((10*weight)+(6.25*height)-(5*age)-161);
        double activityFactor = 0.0;
        double suggCalIntakeMainNum;
        int calFactorGainLose = 500; //Standard value to gain or lose 0.5kg per week

        switch (activity) {
            case "Light":
                activityFactor = 1.375;
                break;
            case "Moderate":
                activityFactor = 1.465;
                break;
            case "Active":
                activityFactor = 1.55;
                break;
        }

        //Basis of calculation is for Maintain Weight
        if (gender.equals("F")) {
            suggCalIntakeMainNum = (activityFactor*femaleBMRFormula);
        } else {
            suggCalIntakeMainNum = (activityFactor*maleBMRFormula);
        }

        //Final Suggested Calories Intake based on BMI results
        if(suggestedAction.startsWith("MAI")) {
                suggCalIntakeFinal = suggCalIntakeMainNum;
        } else if (suggestedAction.startsWith("LOS")) {
                suggCalIntakeFinal = (suggCalIntakeMainNum-calFactorGainLose);
        } else if (suggestedAction.startsWith("GAI")) {
            suggCalIntakeFinal = (suggCalIntakeMainNum + calFactorGainLose);
        }

        txtSuggestedCaloriesIntake = findViewById(R.id.txtSuggCalNum);
        txtSuggestedCaloriesIntake.setText(rf.format(suggCalIntakeFinal));
    }

    //Method to calculate Target Steps based on activity registered
    public void targetSteps (String activity, int currentSteps) {
        double weightLostWeekly = 0.0;
        int burnedCalLose1KG = 7700;
        double burnedCalPerStep = 0.04;
        double burnedCalPerWeek;
        double targetStepsPerWeek;
        double burnedCalPerDay;
        double burnedCalperCurrentSteps;
        double progressStepsBurnedCal;

        switch (activity) {
            case "Light":
                weightLostWeekly = 0.2;
                break;
            case "Moderate":
                weightLostWeekly = 0.3;
                break;
            case "Active":
                weightLostWeekly = 0.5;
                break;
        }
        burnedCalPerWeek = (burnedCalLose1KG*weightLostWeekly) /1;
        targetStepsPerWeek = (1*burnedCalPerWeek)/burnedCalPerStep;
        targetStepsPerDay = targetStepsPerWeek/7;
        burnedCalPerDay = burnedCalPerWeek/7;
        burnedCalperCurrentSteps = currentSteps * burnedCalPerStep;
        progressStepsBurnedCal = burnedCalperCurrentSteps / burnedCalPerDay;

        txtCurrentSteps.setText(String.valueOf(currentSteps));

        txtSuggSteps = findViewById(R.id.txtSuggStepsNum);
        txtSuggSteps.setText(rf.format(targetStepsPerDay));

        txtBurnedCalSteps = findViewById(R.id.txtBurnedCalSteps);
        txtBurnedCalSteps.setText(df.format(burnedCalperCurrentSteps));

        stepProgressPercentage = findViewById(R.id.txtStepsProgressPercentage);
        stepProgressPercentage.setText(pf.format(progressStepsBurnedCal));
    }

    //Jasmine- Navigation drawer
    private void NavDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);

        navigationView.setNavigationItemSelectedListener((@NonNull MenuItem menuItem) -> {

                switch (menuItem.getItemId())
                {
                    case  R.id.navProfile:
                        Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        break;
                    case  R.id.navContactUs:
                           Intent contactIntent = new Intent(HomeActivity.this, ContactActivity.class);
                           startActivity(contactIntent);
                          break;
                    case  R.id.navAboutUs:
                          Intent aboutIntent = new Intent(HomeActivity.this, AboutUsActivity.class);
                           startActivity(aboutIntent);
                        break;
                    case  R.id.navLogout:
                        getSharedPreferences("USER_EMAIL", Context.MODE_PRIVATE).edit().clear().apply();
                        getSharedPreferences("SIGNUP_PREF", Context.MODE_PRIVATE).edit().clear().apply();
                        getSharedPreferences("LOCAL_STORAGE", Context.MODE_PRIVATE).edit().clear().apply();
                        finish();
                        startActivity(new Intent(this, LoginActivity.class));
                    break;
                }

                return false;
            }

        );
    }
    //Jasmine- toolbar for navigation drawer
    public void SetUpToolbar() {
        userObj= GetUser(useremail);
        if (userObj == null) {
              Toast.makeText(this, "Record does not exists.", Toast.LENGTH_SHORT).show();
        } else {
            username = userObj.getUserName();
            gender = (userObj.getUserGender() == null ? "F" : userObj.getUserGender());
            height = userObj.getUserHeight();
            weight = userObj.getUserWeight();
            age = userObj.getUserAge();
            activity = userObj.getActivityType();
            drawerLayout = findViewById(R.id.drawerLayout);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Welcome, " + username);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.app_name_dummy,R.string.app_name_dummy);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
        }


    }

    private User GetUser(String email) {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        try {
            User user = userDao.getUserInfo(email);
            if (user != null) {
                return user;
            }
        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CustomScheduler() {
        // Next run at midnight (UTC) - Replace with local time zone, if needed
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime nextRun = now.withHour(0).withMinute(0).withSecond(0);
        // If midnight is in the past, add one day
        if (now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
            //nextRun = nextRun.plusSeconds(5);
        }
        // Get duration between now and midnight
        final Duration initialDelay = Duration.between(now, nextRun);

        // Schedule a task to run at midnight and then every day
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> resetStepCount(),
                initialDelay.toMillis(),
                Duration.ofDays(1).toMillis(),
              //  Duration.ofSeconds(5).toMillis(),
                TimeUnit.MILLISECONDS);
    }

    private void resetStepCount() {
        //   reset every 24 hours.
        GetNotification("Your Steps: " + steps);
        editor.clear();
        steps = 0;
        progressStatus = steps;
        editor.putInt("steps", steps);
        editor.commit();
        txtCurrentSteps.setText(String.valueOf(steps));
        progressBarSteps.setProgress(progressStatus);
    }

    private void GetNotification(String str) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("B&B", "Body And Beyond", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "B&B")
                .setSmallIcon(R.mipmap.ic_launcher_bb_round)
                .setContentTitle("Notification")
                .setContentText(str);
        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, notification);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            steps = prefs.getInt("steps", 0);
            progressStatus = steps;
            progressBarSteps.setProgress(progressStatus);
            txtCurrentSteps.setText(String.valueOf(steps));
            targetSteps (activity, currentSteps);
        } else {
            Toast.makeText(this, "Sensor not found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        editor.putInt("steps", steps);
        editor.commit();
        progressStatus = steps;
        targetSteps (activity, currentSteps);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running) {
            if (sensorEvent.values[0] > 0) {
                steps++;
                progressStatus = steps;
                targetSteps (activity, currentSteps);
            }
            txtCurrentSteps.setText(String.valueOf(steps));
            progressBarSteps.setProgress(progressStatus);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    private void UserCalRangePref(String range) {
        SharedPreferences sharedPreferences = getSharedPreferences("CATEGORY_DIET", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("DIET_RANGE", range);
        edit.commit();
    }

    private void UserActivity(String activity) {
        SharedPreferences sharedPreferences = getSharedPreferences("CATEGORY_EXERCISE", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("EXERCISE_ACTIVITY", activity);
        edit.commit();
    }
}