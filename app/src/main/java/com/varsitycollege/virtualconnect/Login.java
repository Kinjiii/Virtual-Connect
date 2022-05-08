package com.varsitycollege.virtualconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener
{
    private EditText et_loginName, et_loginPass;
    private Button mb_Login;

    private ProgressBar pb_loginLoading;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mb_Login = (Button) findViewById(R.id.mb_Login);
        mb_Login.setOnClickListener(this);

        et_loginName = (EditText) findViewById(R.id.et_loginName);
        et_loginPass = (EditText) findViewById(R.id.et_loginPass);

        pb_loginLoading = (ProgressBar) findViewById(R.id.pb_loginLoading);

        mAuth = FirebaseAuth.getInstance();


        SpannableString ss = new SpannableString("Not Registered? Create an account");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Login.this, Register.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 16, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView txt_register = (TextView) findViewById(R.id.txt_Register);
        txt_register.setText(ss);
        txt_register.setMovementMethod(LinkMovementMethod.getInstance());
        txt_register.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mb_Login:
                userLogin();
                break;
        }
    }

    private void userLogin()
    {
        String email = et_loginName.getText().toString().trim();
        String password = et_loginPass.getText().toString().trim();

        if(email.isEmpty()) {
            et_loginName.setError("Email is required!");
            et_loginName.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            et_loginName.setError("Please provide a valid email!(including @ character and domain name)");
            et_loginName.requestFocus();
        }
        if(password.isEmpty())
        {
            et_loginPass.setError("Password is required!");
            et_loginPass.requestFocus();
        }
        if(password.length() < 6)
        {
            et_loginPass.setError("Minimum password length is 6 characters!");
            et_loginPass.requestFocus();
        }

        pb_loginLoading.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to home page
                    pb_loginLoading.setVisibility(View.GONE);
                    startActivity(new Intent( Login.this, Home_Page.class));
                }
                else
                {
                    pb_loginLoading.setVisibility(View.GONE);
                    Toast.makeText(Login.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}