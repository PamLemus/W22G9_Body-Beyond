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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.databinding.ActivityCalculateBmiactivityBinding;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CalculateBMIActivity extends AppCompatActivity
{
    final String ACTIVITY_LIGHT = "Light";
    final String ACTIVITY_MODERATE = "Moderate";
    final String ACTIVITY_ACTIVE = "Active";
    final String ERROR = "ERROR_CALCULATE_BMI";
    private Button btnContinue;
    private EditText age;
    private EditText height;
    private EditText weight;
    private String name;
    private String email;
    private String socialLogin;
    private Spinner spinnerActivity;
    ActivityCalculateBmiactivityBinding binding;
    ImageButton backBtn;
    List<String> spinnerItems = new ArrayList<>(
            Arrays.asList("Choose your Activity","Exercise 3-4 times/week","Daily Exercise or intense exercise 4-5 times/week","Intense exercise" +
                    "6-7 times/week")
    );
    SharedPreferences preferences;
    boolean flag = false;
    boolean visibility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_bmiactivity);
        binding = ActivityCalculateBmiactivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,spinnerItems);
        spinnerActivity = binding.spnActCalBmi;
        spinnerActivity.setAdapter(spinnerAdapter);
        backBtn = binding.imgBackBtnCalBmi;

        preferences = getSharedPreferences("SIGNUP_PREF", MODE_PRIVATE);
        visibility = preferences.getBoolean("VISIBILITY", false);
        socialLogin = preferences.getString("SOCIAL_LOGIN", null);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(visibility)
                    startActivity(new Intent(CalculateBMIActivity.this, LoginActivity.class));
                else
                    startActivity(new Intent(CalculateBMIActivity.this, HomeActivity.class));
            }
        });
        btnContinue = binding.btnCalBmi;
        age = binding.txtBmiAge;
        height = binding.txtBmiHeight;
        weight = binding.txtBmiWeight;

        if(visibility)
        {
            age.setVisibility(View.VISIBLE);
            spinnerActivity.setVisibility(View.VISIBLE);
        }
        else{
            age.setVisibility(View.GONE);
            spinnerActivity.setVisibility(View.GONE);
        }

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                            if(visibility)
                            {
                                Validation();
                                if(flag)
                                {
                                    //Insertion operation.
                                    String selectedActivity = null;
                                    int index = spinnerActivity.getSelectedItemPosition();
                                    switch (index){
                                        case 1:
                                            selectedActivity = ACTIVITY_LIGHT;
                                            break;
                                        case 2:
                                            selectedActivity = ACTIVITY_MODERATE;
                                            break;
                                        case 3:
                                            selectedActivity = ACTIVITY_ACTIVE;
                                            break;
                                    }
                                    int userAge = Integer.parseInt(age.getText().toString());
                                    double userWeight = Double.parseDouble(weight.getText().toString());
                                    double userHeight = Double.parseDouble(height.getText().toString());
                                    email = preferences.getString("EMAIL", null);
                                    name = preferences.getString("NAME", null);
                                    String gender = preferences.getString("GENDER", null);
                                    User user = new User(email, name, userAge,
                                            gender == null ? "M" : gender, userHeight, userWeight, selectedActivity, socialLogin);
                                    if(user != null) {
                                        UserEmailPref(email);
                                        QueryExecution(user);
                                        startActivity(new Intent(CalculateBMIActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                }

                            }
                            else{
                                // Update operation.
                                Validate();
                                if(flag)
                                {
                                    double userWeight = Double.parseDouble(weight.getText().toString());
                                    double userHeight = Double.parseDouble(height.getText().toString());
                                    preferences = getSharedPreferences("USER_EMAIL", MODE_PRIVATE);
                                    email = preferences.getString("EMAIL", null);
                                    if(email != null)
                                    {
                                        boolean response = UpdateUserInfo(userHeight, userWeight, email);
                                        if(response) {
                                            startActivity(new Intent(CalculateBMIActivity.this, HomeActivity.class));
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(CalculateBMIActivity.this, "Updation is failed. !!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(CalculateBMIActivity.this, "Email id is null !!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }
        });
    }
    private void UserEmailPref(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_EMAIL", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("EMAIL", email);
        edit.commit();
    }

    private void Validate() {
        if((weight.getText().toString().isEmpty()) || (height.getText().toString().isEmpty())) {
            if(visibility) {
                Toast.makeText(CalculateBMIActivity.this, "Please enter age, height and weight.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(CalculateBMIActivity.this, "Please enter height and weight.", Toast.LENGTH_SHORT).show();
            }
            flag = false;
        }
        else if(Double.parseDouble(height.getText().toString()) <= 0 ){
            Toast.makeText(CalculateBMIActivity.this, "Height must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(Double.parseDouble(weight.getText().toString()) <= 0 ){
            Toast.makeText(CalculateBMIActivity.this, "Weight must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if (visibility && spinnerActivity.getSelectedItemPosition() == 0) {
            Toast.makeText(CalculateBMIActivity.this, "Select valid activity.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else {
            flag = true;
        }
    }

    private void QueryExecution(User user) {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
            try {
                userDao.insertUsers(user);

            } catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }
    }


    private boolean UpdateUserInfo(double usrHeight, double usrWeight, String usrEmail)
    {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
            try {
                int response =  userDao.updateUserInfo(usrHeight, usrWeight, usrEmail);
                if(response == 1)
                {
                    flag.set(true);
                }
            } catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }
        return flag.get();
    }

    private void Validation() {
        int userAge = Integer.parseInt(age.getText().toString());
        double userWeight = Double.parseDouble(weight.getText().toString());
        double userHeight = Double.parseDouble(height.getText().toString());
        if((age.getText().toString().isEmpty())
                || (weight.getText().toString().isEmpty())
                || (height.getText().toString().isEmpty())) {
            Toast.makeText(CalculateBMIActivity.this, "Please enter age, height and weight.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(userAge <= 0)
        {
            Toast.makeText(CalculateBMIActivity.this, "Age must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(userHeight <= 0 ){
            Toast.makeText(CalculateBMIActivity.this, "Height must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(userWeight <= 0 ){
            Toast.makeText(CalculateBMIActivity.this, "Weight must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(spinnerActivity.getSelectedItemPosition() == 0){
            Toast.makeText(CalculateBMIActivity.this, "Select valid activity.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else
        {
            flag = true;
        }
    }
}