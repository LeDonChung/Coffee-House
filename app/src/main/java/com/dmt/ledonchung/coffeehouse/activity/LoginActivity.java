package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPassword;
    private Button loginFacebook, loginGoogle, signIn;
    private TextView forgetPassword, register;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        eventButtonListener();
    }

    public void eventButtonListener() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)) {
                    loginEmail.setError("Vui lòng nhập Email");
                    return;
                }
                progressDialog.show();
                senPasswordResetEmail(email);

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)) {
                    loginEmail.setError("Vui lòng nhập Email");
                    return;
                }

                String password = loginPassword.getText().toString().trim();
                if(TextUtils.isEmpty(password)) {
                    loginPassword.setError("Vui lòng nhập mật khẩu");
                    return;
                }
                progressDialog.show();
                signInWithPasswordAndEmail(email, password);
            }
        });

    }

    private void senPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Email đã gửi..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Email không chính xác", Toast.LENGTH_SHORT).show();
                    loginEmail.setError("Email không chính xác");

                }
            }
        });
    }

    private void signInWithPasswordAndEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    startMainActivity();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initUI() {
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginFacebook = findViewById(R.id.loginFacebook);
        loginGoogle = findViewById(R.id.loginGoogle);
        forgetPassword = findViewById(R.id.forgetPassword);
        register = findViewById(R.id.register);
        signIn = findViewById(R.id.signIn);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đăng nhập");
        progressDialog.setMessage("Vui lòng đợi trong vài giây...");
        progressDialog.dismiss();
    }
    public void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}