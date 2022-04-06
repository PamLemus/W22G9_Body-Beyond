package com.example.bodybeyond.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bodybeyond.database.BodyAndBeyondDB;
import com.example.bodybeyond.databinding.ActivityForgotPasswordBinding;
import com.example.bodybeyond.interfaces.UserDao;
import com.example.bodybeyond.utilities.Helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    ImageButton bckBtn;
    Button changePwdBtn;
    EditText newPwd;
    EditText confirmPwd;

    BodyAndBeyondDB db;
    UserDao userDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        bckBtn = binding.imgForgotPwdBackBtn;
        changePwdBtn = binding.btnChangePwdId;
        newPwd = binding.txtNewPwd;
        confirmPwd = binding.txtConfirmPwd;
        
        bckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });
        
        changePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = Validation(newPwd.getText().toString(), confirmPwd.getText().toString());
                if(isValid)
                {
                    Bundle bundle = getIntent().getExtras();
                    String email = bundle.getString("EMAIL", null);
                    if(email != null && !email.isEmpty())
                    {
                        boolean response = UpdatePassword(email,newPwd.getText().toString());
                        if(response)
                        {
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        }
                    }
                    else
                    {
                        Toast.makeText(ForgotPasswordActivity.this,"Email is not valid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        
    }

    private boolean Validation(String newPassword, String confirmPassword) {
        // 8 characters at least 1 Alphabet, 1 Number and 1 Special Character
        if(newPassword.isEmpty() || confirmPassword.isEmpty())
        {
            Toast.makeText(ForgotPasswordActivity.this, "Password field is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(newPassword.length() < 8 || !(new Helper().isValidPassword(newPassword)))
        {
            Toast.makeText(ForgotPasswordActivity.this, "New passwords is invalid.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(confirmPassword.length() < 8 || !(new Helper().isValidPassword(confirmPassword)))
        {
            Toast.makeText(ForgotPasswordActivity.this, "Confirm passwords is invalid.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!(newPassword.equals(confirmPassword))){
            Toast.makeText(ForgotPasswordActivity.this, "New passwords and confirm password is not same.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean UpdatePassword(String email, String password)
    {
        db = Room.databaseBuilder(getApplicationContext(), BodyAndBeyondDB.class, "BodyAndBeyondDB.db")
                .allowMainThreadQueries().build();
        userDao = db.userDao();
        AtomicBoolean flag = new AtomicBoolean(false);
            try {
              int response =  userDao.updateUserPassword(email, password);
              if(response == 1)
              {
                  flag.set(true);
              }
            } catch (Exception ex) {
                Log.d("Db", ex.getMessage());
            }
        return flag.get();
    }
}