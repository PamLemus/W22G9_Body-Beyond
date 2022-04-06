package com.example.bodybeyond.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView profileEmail;
    EditText profileName;
    EditText profileAge;
    RadioButton profileGenderM;
    RadioButton profileGenderF;
    EditText profileHeight;
    EditText profileWeight;
    Spinner spinnerProfileActivity;
    List<String> spinnerItems = new ArrayList<>(
            Arrays.asList("Light: Exercise 1-3 times/week", "Moderate: Daily exercise or intense exercise 3-5 times/week", "Active: Intense exercise 6-7 times/week")
    );

    String useremail;
    User userObj;
    String username;
    String gender;
    double height;
    double weight;
    int age;
    String activity;

    private static final DecimalFormat tf = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        SharedPreferences sharedPreferences = getSharedPreferences("USER_EMAIL", MODE_PRIVATE);
        useremail = sharedPreferences.getString("EMAIL", "null");

        UserInfo();

        profileEmail = findViewById(R.id.txtUserEmail);
        profileEmail.setText(useremail);

        ImageView backHome = findViewById(R.id.imgProfileBackBtn);
        backHome.setOnClickListener((View view) -> {
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));

        });

        profileName = findViewById(R.id.editTxtProfileName);
        profileName.setText(username);

        profileAge = findViewById(R.id.editTxtProfileAge);
        profileAge.setText(tf.format(age));

        profileGenderM = findViewById(R.id.radioBtnMale);
        profileGenderF = findViewById(R.id.radioBtnFemale);
        if (gender.equals("F")) {
            profileGenderF.setChecked(true);
        } else {
            profileGenderM.setChecked(true);
        }

        profileHeight = findViewById(R.id.editTxtProfileHeight);
        profileHeight.setText(tf.format(height));
        profileWeight = findViewById(R.id.editTxtProfileWeight);
        profileWeight.setText(tf.format(weight));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, spinnerItems);
        spinnerProfileActivity = findViewById(R.id.spinnerProfileActivity);
        spinnerProfileActivity.setAdapter(spinnerAdapter);

        switch (activity) {
            case "Light":
                spinnerProfileActivity.setSelection(0);
                break;
            case "Moderate":
                spinnerProfileActivity.setSelection(1);
                break;
            case "Active":
                spinnerProfileActivity.setSelection(2);
                break;
        }
    }

    public void UserInfo() {
        userObj = GetUser(useremail);
        if (userObj == null) {
            Toast.makeText(this, "Record does not exists.", Toast.LENGTH_SHORT).show();
        } else {
            username = userObj.getUserName();
            gender = userObj.getUserGender();
            height = userObj.getUserHeight();
            weight = userObj.getUserWeight();
            age = userObj.getUserAge();
            activity = userObj.getActivityType();

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

}