package com.example.bodybeyond.activities;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.bumptech.glide.request.RequestOptions;
import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;
import com.example.bodybeyond.utilities.Helper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class LoginActivity extends AppCompatActivity {

    final String FACEBOOK_LOGIN = "FACEBOOK";
    final String GOOGLE_LOGIN = "GOOGLE";
    final String TAG = "LOGIN_ACTIVITY";
    static final int RC_SIGN_IN = 0;

    private Button googleLogIn;
    private Button facebookLogIn;

    //Facebook Login
    private CallbackManager callbackManager;

    EditText emailId;
    EditText password;
    Button btnLogIn;
    TextView forgetPwd;
    ImageButton backBtn;
    String email;
    String pwd;
    String fb_email;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailId = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPwd);
        btnLogIn = findViewById(R.id.btnLogIn);
        backBtn = findViewById(R.id.imgLoginBackBtn);
        forgetPwd = findViewById(R.id.txtforgetPwd);
        forgetPwd.setOnClickListener((View view) -> {
            try {
                if (new Helper().emailValidator(emailId.getText().toString())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("EMAIL", emailId.getText().toString());
                    Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter valid Email address.", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
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
                if (isValid) {
                    boolean response = GetUserInfo(email, pwd);
                    if (response) {
                        UserEmailPref(emailId.getText().toString());
                        startActivity(new Intent(this, HomeActivity.class));
                    } else {
                        Toast.makeText(this, "No record exist. !!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("63183385366-uhk47tp8is54p0lo6huk22hv39ndkhto.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleLogIn.setOnClickListener((View view) -> {
            switch (view.getId()) {
                case R.id.buttonGoogle:
                    signIn();
                    break;
            }
        });

//        Steps to implement Facebook login"
//        1.Goto Facebook developer page and click on add new App.
//        2.After App creation. Click on doc and select facebook login
//        3.Select Android and select the newly created app.
//        4.Follow the instruction as follow

        facebookLogIn = findViewById(R.id.buttonFacebook);
        callbackManager = CallbackManager.Factory.create();

        facebookLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email, user_gender, public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                                try {
                                    String firstName = jsonObject.optString("first_name");
                                    String lastName = jsonObject.optString("last_name");
                                    String email = jsonObject.optString("email");
                                    String gender = jsonObject.optString("gender");
                                    fb_email = email;
                                    if (!GetUser(email)) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("SIGNUP_PREF", MODE_PRIVATE);
                                        SharedPreferences.Editor edit = sharedPreferences.edit();
                                        edit.putBoolean("VISIBILITY", true);
                                        edit.putString("SOCIAL_LOGIN", FACEBOOK_LOGIN);
                                        edit.putString("NAME", firstName + " " + lastName);
                                        edit.putString("EMAIL", email);
                                        edit.putString("GENDER", gender);
                                        edit.commit();
                                        // UserEmailPref(email);
                                        Log.d("FACEBOOK..", jsonObject.toString());
                                        RequestOptions requestOptions = new RequestOptions();
                                        requestOptions.dontAnimate();
                                        startActivity(new Intent(LoginActivity.this, CalculateBMIActivity.class));
                                    } else {
                                        UserEmailPref(email);
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "gender,first_name,last_name,email,id");
                        request.setParameters(parameters);
                        request.executeAsync();
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

    private void UserEmailPref(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_EMAIL", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("EMAIL", email);
        edit.commit();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean Validation(String email, String pwd) {
        if (email.isEmpty() || !(new Helper().emailValidator(emailId.getText().toString()))) {
            Toast.makeText(this, "Please enter valid Email address.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Empty email id field.");
            return false;
        } else if (pwd.isEmpty() && pwd.length() < 8 && !(new Helper().isValidPassword(pwd))) {
            Toast.makeText(this, "Please enter valid Password.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Empty password field.");
            return false;
        } else {
            return true;
        }
    }

    private boolean GetUserInfo(String email, String password) {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
        try {
            User user = userDao.authenticateUser(email, password);
            if (user != null) {
                flag.set(true);
            }
        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
        return flag.get();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                if (!GetUser(account.getEmail())) {
                    SharedPreferences sharedPreferences = getSharedPreferences("SIGNUP_PREF", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("VISIBILITY", true);
                    edit.putString("SOCIAL_LOGIN", GOOGLE_LOGIN);
                    edit.putString("NAME", account.getDisplayName());
                    edit.putString("EMAIL", account.getEmail());
                    edit.commit();
                    startActivity(new Intent(this, CalculateBMIActivity.class));
                } else {
                    UserEmailPref(account.getEmail());
                    startActivity(new Intent(this, HomeActivity.class));
                }
                Toast.makeText(this, "Sign in successful. !!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Sign in not successful!!", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private boolean GetUser(String email) {
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
        try {
            User user = userDao.getUserInfo(email);
            if (user != null) {
                flag.set(true);
            }
        } catch (Exception ex) {
            Log.d("Db", ex.getMessage());
        }
        return flag.get();
    }
}