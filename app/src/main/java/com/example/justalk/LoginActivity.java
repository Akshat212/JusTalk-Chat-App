package com.example.justalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    TextView signup;
    EditText login_email,login_password;
    TextView signin_btn;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        signup = findViewById(R.id.signup);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        signin_btn = findViewById(R.id.signin_btn);

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = login_email.getText().toString();
                String password = login_password.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Somethin' is Missing", Toast.LENGTH_SHORT).show();
                } else if(!email.matches(emailPattern))
                {
                    progressDialog.dismiss();
                    login_email.setError("Invalid Email");
                    Toast.makeText(LoginActivity.this,"Looks Like it's an Invalid email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Oops! Log In Error\nRecheck your password", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }
}