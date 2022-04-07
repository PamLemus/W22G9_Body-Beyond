package com.example.bodybeyond.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.bodybeyond.utilities.Helper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProfileActivity extends AppCompatActivity {

    TextView profileEmail;
    EditText profileName;
    EditText profileAge;
    RadioButton profileGenderM;
    RadioButton profileGenderF;
    EditText profileHeight;
    EditText profileWeight;
    Spinner spinnerProfileActivity;
    ImageButton editProfileInfo;
    Button saveChanges;
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
    String updatedName;
    int updatedAge;
    double updatedHeight;
    double updatedWeight;
    boolean infoValidation = false;

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

        saveChanges = findViewById(R.id.btnChangeProfile);
        saveChanges.setVisibility(View.INVISIBLE);

        profileName = findViewById(R.id.editTxtProfileName);
        profileName.setText(username);
        profileName.setFocusable(false);
        profileName.setBackgroundColor(getResources().getColor(R.color.light_gray));

        profileAge = findViewById(R.id.editTxtProfileAge);
        profileAge.setText(tf.format(age));
        profileAge.setFocusable(false);
        profileAge.setBackgroundColor(getResources().getColor(R.color.light_gray));

        profileGenderM = findViewById(R.id.radioBtnMale);
        profileGenderF = findViewById(R.id.radioBtnFemale);
        if (gender.equals("F")) {
            profileGenderF.setChecked(true);
        } else {
            profileGenderM.setChecked(true);
        }
        profileGenderF.setEnabled(false);
        profileGenderM.setEnabled(false);

        profileHeight = findViewById(R.id.editTxtProfileHeight);
        profileHeight.setText(tf.format(height));
        profileHeight.setFocusable(false);
        profileHeight.setBackgroundColor(getResources().getColor(R.color.light_gray));

        profileWeight = findViewById(R.id.editTxtProfileWeight);
        profileWeight.setText(tf.format(weight));
        profileWeight.setFocusable(false);
        profileWeight.setBackgroundColor(getResources().getColor(R.color.light_gray));

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
        spinnerProfileActivity.setEnabled(false);

        saveChanges = findViewById(R.id.btnChangeProfile);
        editProfileInfo = findViewById(R.id.imgbtnEditProfileInfo);

        editProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges.setVisibility(View.VISIBLE);
                profileName.setFocusableInTouchMode(true);
                profileName.setBackgroundColor(getResources().getColor(R.color.white));

                profileAge.setFocusableInTouchMode(true);
                profileAge.setBackgroundColor(getResources().getColor(R.color.white));

                profileGenderF.setEnabled(true);
                profileGenderM.setEnabled(true);
                profileHeight.setFocusableInTouchMode(true);
                profileHeight.setBackgroundColor(getResources().getColor(R.color.white));
                profileWeight.setFocusableInTouchMode(true);
                profileWeight.setBackgroundColor(getResources().getColor(R.color.white));
                spinnerProfileActivity.setEnabled(true);
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatedName = profileName.getText().toString();
                updatedAge = Integer.valueOf(profileAge.getText().toString());
                String updatedGender = gender;
                if (profileGenderF.isChecked()) {
                    updatedGender = "F";
                } else if (profileGenderM.isChecked()) {
                    updatedGender = "M";
                }
                updatedHeight = Double.valueOf(profileHeight.getText().toString());
                updatedWeight = Double.valueOf(profileWeight.getText().toString());
                String updatedActivity = activity;
                int indexUpdatedActivity = spinnerProfileActivity.getSelectedItemPosition();
                switch (indexUpdatedActivity) {
                    case 0:
                        updatedActivity = "Light";
                        break;
                    case 1:
                        updatedActivity = "Moderate";
                        break;
                    case 2:
                        updatedActivity = "Active";
                        break;
                }
                UserInputValidation();
                if (infoValidation) {
                    boolean applyChanges = UpdateAllProfileInfo(updatedName, updatedAge, updatedGender, updatedHeight, updatedWeight, updatedActivity, useremail);
                    if (applyChanges) {
                        Toast.makeText(ProfileActivity.this, "Information has been uploaded!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Error updating profile information!", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(ProfileActivity.this, "Error updating profile information!", Toast.LENGTH_SHORT).show();

                }
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
            });
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

    private boolean UpdateAllProfileInfo(String name, int age, String gender, double height, double weight, String activity, String userEmail)
    {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
        try {
            int response =  userDao.updateAllProfileInfo(name, age, gender, height, weight, activity, userEmail);
            if(response == 1)
            {
                flag.set(true);
            }
        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
        return flag.get();
    }

    private void UserInputValidation() {
         if (updatedName.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Name field is empty.", Toast.LENGTH_SHORT).show();
            infoValidation = false;
        } else if (updatedAge <= 0) {
            Toast.makeText(ProfileActivity.this, "Age must be greater than zero.", Toast.LENGTH_SHORT).show();
            infoValidation = false;
        } else if (updatedHeight <= 0) {
            Toast.makeText(ProfileActivity.this, "Height must be greater than zero.", Toast.LENGTH_SHORT).show();
             infoValidation = false;
        } else if (updatedWeight <= 0) {
            Toast.makeText(ProfileActivity.this, "Weight must be greater than zero.", Toast.LENGTH_SHORT).show();
             infoValidation = false;
        }
        else {
            infoValidation = true;
        }
    }

}