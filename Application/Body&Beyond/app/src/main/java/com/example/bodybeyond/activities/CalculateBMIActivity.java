package com.example.bodybeyond.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.databinding.ActivityCalculateBmiactivityBinding;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;
import com.example.bodybeyond.utilities.Helper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CalculateBMIActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    final String FACEBOOK_LOGIN = "FACEBOOK";
    final String GOOGLE_LOGIN = "GOOGLE";
    final String ACTIVITY_LIGHT = "Light";
    final String ACTIVITY_MODERATE = "Moderate";
    final String ACTIVITY_ACTIVE = "Active";
    final String ERROR = "ERROR_CALCULATE_BMI";

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
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
            Arrays.asList("Choose your Activity","Exercise 4-5 times/week","Daily Exercise or intense exercise 3-4 times/week","Intense exercise" +
                    "6-7 times/week")
    );

    SharedPreferences preferences;
    boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_bmiactivity);
        binding = ActivityCalculateBmiactivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,spinnerItems);
        spinnerActivity = binding.spnActCalBmi;
        spinnerActivity.setAdapter(spinnerAdapter);
        backBtn = binding.imgBackBtnCalBmi;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalculateBMIActivity.this, LoginActivity.class));
            }
        });
        preferences = getSharedPreferences("SIGNUP_PREF", MODE_PRIVATE);
        boolean visibility = preferences.getBoolean("VISIBILITY", false);
        socialLogin = preferences.getString("SOCIAL_LOGIN", null);

        if(visibility) {
            if (socialLogin.equals(FACEBOOK_LOGIN)) {
                GetUserDetail();
            }
            if (socialLogin.equals(GOOGLE_LOGIN)) {
                preferences = getSharedPreferences("GOOGLE_PREF", MODE_PRIVATE);
                GetUserDetail();
            }
        }

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
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            //Perform Database operation

                            if(visibility)
                            {
                                Validation();
                                if(flag)
                                {
                                    //TODO Insertion operation.
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
                                    User user = new User(email, name, userAge,
                                            "M", userHeight, userWeight, selectedActivity, socialLogin);
                                    if(user != null) {
                                        QueryExecution(user);
                                        startActivity(new Intent(CalculateBMIActivity.this, HomeActivity.class));
                                        finish();
                                    }
                                }

                            }
                            else{
                                //TODO Updation operation.
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
                                            Toast.makeText(CalculateBMIActivity.this, "Update response is invalid !!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(CalculateBMIActivity.this, "Email id is null !!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(CalculateBMIActivity.this, "Log out failed!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void Validate() {
        double userWeight = Double.parseDouble(weight.getText().toString());
        double userHeight = Double.parseDouble(height.getText().toString());
        if((weight.getText().toString().isEmpty()) || (height.getText().toString().isEmpty())) {
            Toast.makeText(CalculateBMIActivity.this, "Please enter age, height and weight.", Toast.LENGTH_SHORT).show();
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
        else {
            flag = true;
        }
    }

    private synchronized  void GetUserDetail() {
        try{
            email = preferences.getString("EMAIL", null);
            name = preferences.getString("NAME", null);
            boolean response = GetUser(email);
            if(response)
            {
                startActivity(new Intent(CalculateBMIActivity.this, HomeActivity.class));
            }
        }
        catch (Exception e)
        {
            Log.e(ERROR,"Something happened during GetUserDetail().");
            e.printStackTrace();
        }

    }

    private synchronized boolean GetUser(String email){
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                    .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
            try {
                User user = userDao.getUserInfo(email);
                if(user != null)
                {
                    flag.set(true);
                }
            }
            catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }

//        });

        return flag.get();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Log.d("DETAILS", " "  + account.getDisplayName() + "\n" + account.getEmail()+ "\n"
                    + account.getFamilyName() + "\n" + account.describeContents());
            // Storing data into SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("GOOGLE_PREF", MODE_PRIVATE);

            // Creating an Editor object to edit(write to the file)
            SharedPreferences.Editor edit = sharedPreferences.edit();

            // Storing the key and its value as the data fetched from edittext
            edit.putString("NAME", account.getDisplayName());
            edit.putString("EMAIL", account.getEmail());
            edit.commit();

            // Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profileImage);
        } else {
            //  gotoMainActivity();
            Toast.makeText(CalculateBMIActivity.this, "Sign in not successful!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optionalPendingResult.isDone()) {
            GoogleSignInResult googleSignInResult = optionalPendingResult.get();
            handleSignInResult(googleSignInResult);
        } else {
            optionalPendingResult.setResultCallback((@NonNull GoogleSignInResult result) -> {
                handleSignInResult(result);
            });
        }
    }

    private void QueryExecution(User user) {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db").build();
        UserDao userDao = db.userDao();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                userDao.insertUsers(user);

            } catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }
        });
    }


    private boolean UpdateUserInfo(double usrHeight, double usrWeight, String usrEmail)
    {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
            try {
                int response =  userDao.updateUserInfo(usrHeight, usrWeight, usrEmail);
                if(response == 1)
                {
                    flag.set(true);
                }
            } catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }

//        });

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