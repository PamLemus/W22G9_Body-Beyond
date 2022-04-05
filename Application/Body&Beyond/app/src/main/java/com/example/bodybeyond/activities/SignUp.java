package com.example.bodybeyond.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.bodybeyond.R;
import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.models.User;
import com.example.bodybeyond.utilities.Helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class SignUp extends AppCompatActivity{

    final String TAG = "SIGNUP_ACTIVITY";
    EditText email;
    ImageButton backBtn;
    private Button signUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        backBtn = findViewById(R.id.imgSignupBckBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
        });
        email = findViewById(R.id.txtEmailId);
        signUpBtn = findViewById(R.id.buttonContinue);
        signUpBtn.setOnClickListener((View view) ->{
                if(Validation(email.getText().toString()))
                {
                    if(!(GetUser(email.getText().toString()))) {
                        Bundle bundle = new Bundle();
                        bundle.putString("EMAIL", email.getText().toString());
                        Intent intent = new Intent(SignUp.this, SignUpDetails.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(this, "Email already Exist. !!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Invalid Email Id. !!", Toast.LENGTH_SHORT).show();
                }
        });
    }

    private boolean Validation(String email) {
        if(email.isEmpty() || !(new Helper().emailValidator(email)))
        {
            Toast.makeText(this, "Please enter valid Email address.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Empty email id field.");
            return false;
        }
        else {
            return true;
        }
    }

    private boolean GetUser(String email){
        BodyAndBeyondDB db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
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
        return flag.get();
    }
}