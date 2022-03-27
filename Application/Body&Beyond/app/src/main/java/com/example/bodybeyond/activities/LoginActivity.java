package com.example.bodybeyond.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;
import com.example.bodybeyond.utilities.Helper;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener {

    final String FACEBOOK_LOGIN = "FACEBOOK";
    final String GOOGLE_LOGIN = "GOOGLE";
    private Button googleLogIn;
    private Button facebookLogIn;


    //Google Login
    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 1;

    //Facebook Login
    private CallbackManager callbackManager;



    final String TAG = "LOGIN_ACTIVITY";
    EditText emailId;
    EditText password;
    Button btnLogIn;
    TextView forgetPwd;
    ImageButton backBtn;



    String email;
    String pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailId = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPwd);
        btnLogIn = findViewById(R.id.btnLogIn);
        backBtn = findViewById(R.id.imgLoginBackBtn);
        forgetPwd = findViewById(R.id.txtforgetPwd);
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(new Helper().emailValidator(emailId.getText().toString()))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("EMAIL", emailId.getText().toString());
                        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Please enter valid Email address.", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        btnLogIn.setOnClickListener((View view) -> {
            try {
                email = emailId.getText().toString();
                pwd = password.getText().toString();
                boolean isValid = Validation(email, pwd);
                if(isValid)
                {
                    boolean response = GetUserInfo(email, pwd);
                    if(response)
                    {
                        SharedPreferences sharedPreferences = getSharedPreferences("USER_EMAIL", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("EMAIL", email);
                        edit.commit();
                        startActivity(new Intent(this, HomeActivity.class));
                    }
                    else {
                        Toast.makeText(this, "No record exist. !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        });


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

        googleLogIn = findViewById(R.id.buttonGoogle);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        //Google login logic
        googleLogIn.setOnClickListener((View view) -> {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            SharedPreferences sharedPreferences = getSharedPreferences("SIGNUP_PREF", MODE_PRIVATE );
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean("VISIBILITY", true);
            edit.putString("SOCIAL_LOGIN", GOOGLE_LOGIN);
            edit.commit();
            startActivityForResult(intent, SIGN_IN);

        });

//        Steps to impliment Facebook login"
//        1.Goto Facebook developer page and click on add new App.
//        2.After App creation. Click on doc and select facebook login
//        3.Select Android and select the newly created app.
//        4.Follow the instruction as follow

        FacebookSdk.sdkInitialize(getApplicationContext());
        facebookLogIn = findViewById(R.id.buttonFacebook);
        callbackManager = CallbackManager.Factory.create();


        //Facebook login logic
        facebookLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email","public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(LoginActivity.this, CalculateBMIActivity.class));
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull FacebookException e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private boolean Validation(String email, String pwd) {
        if(email.isEmpty() || !(new Helper().emailValidator(emailId.getText().toString())))
        {
            Toast.makeText(this, "Please enter valid Email address.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Empty email id field.");
            return false;
        }
        else if(pwd.isEmpty() && pwd.length() < 8 && !(new Helper().isValidPassword(pwd)))
        {
            Toast.makeText(this, "Please enter valid Password.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Empty password field.");
            return false;
        }
        else {
            return true;
        }
    }

    private boolean GetUserInfo(String email, String password) {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(() -> {
            try {
                User user = userDao.authenticateUser(email, password);
                if(user != null)
                {
                    flag.set(true);
                }
            } catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }
//        });
        return flag.get();
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
                    SharedPreferences sharedPreferences = getSharedPreferences("SIGNUP_PREF", MODE_PRIVATE );
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("VISIBILITY", true);
                    edit.putString("SOCIAL_LOGIN", FACEBOOK_LOGIN);
                    edit.putString("NAME", firstName + " " + lastName);
                    edit.putString("EMAIL", email);
                    edit.commit();
                    Log.d("FACEBOOK..", ""+ email);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();
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