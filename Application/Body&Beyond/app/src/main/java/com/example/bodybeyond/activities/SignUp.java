package com.example.bodybeyond.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.request.RequestOptions;
import com.example.bodybeyond.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.Arrays;

public class SignUp extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button GoogleSignUp;
    private Button FacebookSignUp;
    private Button SignUpBtn;
    EditText email;

    //Google Login
    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 1;

    //Facebook Login
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.txtEmailId);
        SignUpBtn = findViewById(R.id.buttonContinue);
        SignUpBtn.setOnClickListener((View view) ->{
                Toast.makeText(SignUp.this, "Inside continue btn", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("EMAIL", email.getText().toString());
                Intent intent = new Intent(SignUp.this, SignUpDetails.class);
                intent.putExtras(bundle);
                startActivity(intent);
        });

        SharedPreferences sharedPreferences = getSharedPreferences("FIELD_VISIBILITY", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();


//        Implementation steps for gmail login:
//        1.Goto the browser and type integerating google login with android app
//        2.Select Start Integrating Google Sign-In into Your Android App (https://developers.google.com/identity/sign-in/android/start-integrating)
//        3.Copy and paste the dependencies mentioned in gridle file
//        4.Click on configure project and create new project
//        5.Enter product name and Select Android app
//        6.Provide package name and SHA1 Code
//        To get SHA1 code got to Gradle on right top corner then click on app and then signinReport.
//        7.Click on create and download client configuration credential.json.
//        8.Paste the Creential.json in app folder.
//        9.Add permission to use internet in manifest file.

        GoogleSignUp = findViewById(R.id.buttonGoogle);

//        Steps to impliment Facebook login"
//        1.Goto Facebook developer page and click on add new App.
//        2.After App creation. Click on doc and select facebook login
//        3.Select Android and select the newly created app.
//        4.Follow the instruction as follow

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSignUp = findViewById(R.id.buttonFacebook);
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        //Google login logic
        GoogleSignUp.setOnClickListener((View view) -> {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            putVisibility(edit);
            startActivityForResult(intent, SIGN_IN);
        });

        //Facebook login logic
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//              //  Log.d("Success", "Login");
//                Toast.makeText(SignUp.this, "FACEBOOK: " + firstName + lastName + email, Toast.LENGTH_LONG).show();
//                Toast.makeText(SignUp.this, "FACEBOOK2: " + loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
//                putVisibility(edit);
//                startActivity(new Intent(SignUp.this, CalculateBMIActivity.class));
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(SignUp.this, "Login Cancel", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(@NonNull FacebookException e) {
//                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        FacebookSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignUp.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //  Log.d("Success", "Login");

                        //Toast.makeText(SignUp.this, "FACEBOOK2: " + loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
                       // Profile profile = Profile.getCurrentProfile();
                       // Toast.makeText(SignUp.this, "FACEBOOK2: " + profile.getFirstName(), Toast.LENGTH_LONG).show();
                        putVisibility(edit);
                        startActivity(new Intent(SignUp.this, CalculateBMIActivity.class));
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignUp.this, "Login Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull FacebookException e) {
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void putVisibility(SharedPreferences.Editor edit) {
        edit.putBoolean("VISIBILITY", true);
        edit.commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                startActivity(new Intent(this, CalculateBMIActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login has failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
            if(accessToken != null)
            {
                loadUserProfile(accessToken);

            }
        }
    };

    private void loadUserProfile(AccessToken accessToken)
    {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                try{
                    String firstName = jsonObject.optString("first_name");
                    String lastName = jsonObject.optString("last_name");
                    String email = jsonObject.optString("email");
                    String id = jsonObject.optString("id");
                    String imgUrl = "https://graph.facebook.com"+id+"picture?type=normal";

                    Log.d("FACEBOOK..", ""+ email);
                    Toast.makeText(SignUp.this, "FACEBOOK: " + firstName + lastName + email, Toast.LENGTH_LONG).show();
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
                   // Glide.with(SignUp.this).load(imgUrl).into(imageView);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}