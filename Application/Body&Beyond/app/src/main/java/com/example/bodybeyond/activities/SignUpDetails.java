package com.example.bodybeyond.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;
import com.example.bodybeyond.utilities.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpDetails extends AppCompatActivity {

    EditText name;
    EditText age;
    RadioGroup rdGrpGender;
    EditText height;
    EditText weight;
    EditText password;
    Spinner spinnerActivity;
    Button signup;
    ImageButton back;
    String userGender;
    final String FEMALE = "F";
    final String MALE = "M";
    final String ACTIVITY_LIGHT = "Light";
    final String ACTIVITY_MODERATE = "Moderate";
    final String ACTIVITY_ACTIVE = "Active";
    List<String> spinnerItems = new ArrayList<>(
            Arrays.asList("Choose your Activity", "Light: Exercise 1-3 times/week", "Moderate: Daily Exercise or intense exercise 3-5 times/week", "Active: Intense exercise 6-7 times/week")
    );
    BodyAndBeyondDB db;
    boolean flag = false;
    int userAge;
    double userWeight, userHeight;
    String userPwd;
    String email;
    User user = null;
    UserDao userDao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, spinnerItems);

        email = getIntent().getExtras().getString("EMAIL", null);
        name = findViewById(R.id.editTextTextName);
        age = findViewById(R.id.editTextAge);
        rdGrpGender = findViewById(R.id.radioGroupGender);
        height = findViewById(R.id.editTextHeight);
        weight = findViewById(R.id.editTextWeight);
        spinnerActivity = findViewById(R.id.spinnerActivity);
        spinnerActivity.setAdapter(spinnerAdapter);
        signup = findViewById(R.id.buttonSignup);
        password = findViewById(R.id.txtUserPwd);
        back = findViewById(R.id.imageButtonBackDetails);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpDetails.this, SignUp.class));
            }
        });
        signup.setOnClickListener((View view) -> {
            userAge = Integer.parseInt(age.getText().toString());
            userWeight = Double.parseDouble(weight.getText().toString());
            userHeight = Double.parseDouble(height.getText().toString());
            userPwd = password.getText().toString();
            Validation();
            if (flag) {

                if (rdGrpGender.getCheckedRadioButtonId() == R.id.radioButtonFemale) {
                    userGender = FEMALE;
                } else {
                    userGender = MALE;
                }

                String selectedActivity = null;
                int index = spinnerActivity.getSelectedItemPosition();
                switch (index) {
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
                user = new User(email, name.getText().toString(), userAge,
                        userGender, userHeight, userWeight, selectedActivity, userPwd);
                DBConnection();
                if (user != null && userDao != null) {
                    QueryExecution(user, userDao);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserName", name.getText().toString());
                    editor.putString("UserEmail", email);
                    editor.commit();
                    Log.d("USERINFOSignup", name.getText().toString()+ "," + email);

                     // sharedPreferences.edit().remove("SIGNUP_PREF"). commit();
                    startActivity(new Intent(SignUpDetails.this, LoginActivity.class));
                }
            } else {
                Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Validation() {
        if ((age.getText().toString().isEmpty())
                || (weight.getText().toString().isEmpty())
                || (height.getText().toString().isEmpty())) {
            Toast.makeText(SignUpDetails.this, "Please enter age, height and weight.", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (name.getText().toString().isEmpty()) {
            Toast.makeText(SignUpDetails.this, "Name field is empty.", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (userAge <= 0) {
            Toast.makeText(SignUpDetails.this, "Age must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (userHeight <= 0) {
            Toast.makeText(SignUpDetails.this, "Height must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (userWeight <= 0) {
            Toast.makeText(SignUpDetails.this, "Weight must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (spinnerActivity.getSelectedItemPosition() == 0) {
            Toast.makeText(SignUpDetails.this, "Select valid activity.", Toast.LENGTH_SHORT).show();
            flag = false;
        }  else if (userPwd.length() < 8 || !(new Helper().isValidPassword(userPwd))) {
            // Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character.
            Toast.makeText(SignUpDetails.this, "Password is not valid.", Toast.LENGTH_SHORT).show();
            flag = false;
        }  else if (!(new Helper().emailValidator(email))) {
            Toast.makeText(SignUpDetails.this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
            flag = false;
        } else {
            flag = true;
        }
    }

    private void DBConnection() {
        db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        userDao = db.userDao();
    }

    private void QueryExecution(User user, UserDao userdao) {
        try {
            userdao.insertUsers(user);
            } catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }
    }
}