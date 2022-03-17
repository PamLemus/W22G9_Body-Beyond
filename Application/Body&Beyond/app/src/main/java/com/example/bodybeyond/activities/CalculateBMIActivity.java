package com.example.bodybeyond.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bodybeyond.R;
import com.example.bodybeyond.databinding.ActivityCalculateBmiactivityBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class CalculateBMIActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private Button btnContinue;
    private EditText age;
    private EditText height;
    private EditText weight;
    ActivityCalculateBmiactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_bmiactivity);
        binding = ActivityCalculateBmiactivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences preferences = getSharedPreferences("FIELD_VISIBILITY", MODE_PRIVATE);
        boolean visibility = preferences.getBoolean("VISIBILITY", false);

        btnContinue = binding.btnCalBmi;
        age = binding.txtBmiAge;
        //age.setVisibility(View.GONE);
        if(visibility)
        {
            age.setVisibility(View.VISIBLE);
        }
        else{
            age.setVisibility(View.GONE);
        }

        height = binding.txtBmiHeight;
        weight = binding.txtBmiWeight;

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            //Perform Database operation
                            startActivity(new Intent(CalculateBMIActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(CalculateBMIActivity.this, "Log out failed!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
//        btnContinue.setOnClickListener((View view) -> {
//            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//                @Override
//                public void onResult(@NonNull Status status) {
//                    if (status.isSuccess()) {
//                        startActivity(new Intent(CalculateBMIActivity.this, HomeActivity.class));
//                        finish();
//                    } else {
//                        Toast.makeText(CalculateBMIActivity.this, "Log out failed!!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Log.d("DETAILS", " "  + account.getDisplayName() + "\n" + account.getEmail()+ "\n"
                    + account.getFamilyName() + "\n" + account.describeContents());
            // Storing data into SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("GooglePref", MODE_PRIVATE);

            // Creating an Editor object to edit(write to the file)
            SharedPreferences.Editor edit = sharedPreferences.edit();

            // Storing the key and its value as the data fetched from edittext
            edit.putString("USER_NAME", account.getDisplayName());
            edit.putString("USER_EMAIL", account.getEmail());
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
}