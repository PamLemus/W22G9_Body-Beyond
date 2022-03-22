package com.example.bodybeyond.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bodybeyond.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    ImageButton bckBtn;
    Button changePwdBtn;
    EditText newPwd;
    EditText confirmPwd;
    
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
                if(newPwd.getText().toString().isEmpty() || confirmPwd.getText().toString().isEmpty())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Password field is empty.", Toast.LENGTH_SHORT).show();
                }
                else if(newPwd.getText().toString() != confirmPwd.getText().toString()){
                    Toast.makeText(ForgotPasswordActivity.this, "Passwords is not same.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Bundle bundle = getIntent().getExtras();
                    String email = bundle.getString("EMAIL", null);
                    if(email != null && !email.isEmpty())
                    {
                        //Do database operation to same new password in user table
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    }
                    else
                    {
                        Toast.makeText(ForgotPasswordActivity.this,"Email is not valid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        
    }
}