package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpDetails extends AppCompatActivity {

    EditText name;
    EditText age;
    RadioGroup rdGrpgender;
    EditText height;
    EditText weight;
    Spinner spinnerActivity;
    Button signup;
    String userGender;
    final String FEMALE = "F";
    final String MALE = "M";
    final String ACTIVITY_LIGHT = "Light";
    final String ACTIVITY_MODERATE = "Moderate";
    final String ACTIVITY_ACTIVE = "Active";
    List<String> spinnerItems = new ArrayList<>(
            Arrays.asList("Choose your Activity","Exercise 4-5 times/week","Daily Exercise or intense exercise 3-4 times/week","Intense exercise" +
                    "6-7 times/week")
    );
    BodyAndBeyondDB db;
    boolean flag = false;
    int userage;
    double userWeight,userHeight;
    User user = null;
    UserDao userdao = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,spinnerItems);

        String email = getIntent().getExtras().getString("EMAIL", null);
        name = findViewById(R.id.editTextTextName);
        age = findViewById(R.id.editTextAge);
        rdGrpgender = findViewById(R.id.radioGroupGender);
        height = findViewById(R.id.editTextHeight);
        weight = findViewById(R.id.editTextWeight);
        spinnerActivity = findViewById(R.id.spinnerActivity);
        spinnerActivity.setAdapter(spinnerAdapter);
        signup = findViewById(R.id.buttonSignup);
        /*int userage = Integer.parseInt(age.getText().toString());
        double userWeight = Double.parseDouble(weight.getText().toString());
        double userHeight = Double.parseDouble(height.getText().toString());
        if(rdGrpgender.getCheckedRadioButtonId() == R.id.radioButtonFemale){
            userGender = FEMALE;
        }
        else{
            userGender = MALE;
        }*/

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Validation();
                    if(flag)
                    {
                        if(rdGrpgender.getCheckedRadioButtonId() == R.id.radioButtonFemale){
                            userGender = FEMALE;
                        }
                        else{
                            userGender = MALE;
                        }

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
                        user = new User(email, name.getText().toString(), userage,
                                userGender, userHeight, userWeight, selectedActivity);
                         DBConnection();
                         if(user != null &&  userdao != null)
                         {
                             QueryExecution(user, userdao);
                             startActivity(new Intent(SignUpDetails.this, HomeActivity.class));
                         }

                    }
                }
        });
    }

    private void Validation() {
        if((age.getText().toString().isEmpty() || age.getText().toString() == null)
                || (weight.getText().toString().isEmpty() || weight.getText().toString() == null)
                || (height.getText().toString().isEmpty() || height.getText().toString() == null)) {
            Toast.makeText(SignUpDetails.this, "Please enter age, height and weight.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            userage = Integer.parseInt(age.getText().toString());
            userWeight = Double.parseDouble(weight.getText().toString());
           userHeight = Double.parseDouble(height.getText().toString());
        }
        if(name.getText().toString().isEmpty())
        {
            Toast.makeText(SignUpDetails.this, "Name field is empty.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(userage <= 0)
        {
            Toast.makeText(SignUpDetails.this, "Age must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(userHeight <= 0 ){
            Toast.makeText(SignUpDetails.this, "Height must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(userWeight <= 0 ){
            Toast.makeText(SignUpDetails.this, "Weight must be greater than zero.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(spinnerActivity.getSelectedItemPosition() == 0){
            Toast.makeText(SignUpDetails.this, "Select valid activity.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else
        {
            flag = true;
        }
    }

    private void DBConnection() {
        db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyandBeyondDB.db").build();
        userdao = db.userDao();
    }


    private void QueryExecution(User user, UserDao userdao) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                userdao.insertUsers(user);

            } catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }

        });
    }
}