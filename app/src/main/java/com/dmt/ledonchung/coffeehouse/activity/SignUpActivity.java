package com.dmt.ledonchung.coffeehouse.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText registerRePassword, registerPassword, registerEmail;
    private Button signUp;
    private TextView signIn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();
        eventButtonListener();
    }
    public void initUI() {
        registerRePassword = findViewById(R.id.registerRePassword);
        registerPassword = findViewById(R.id.registerPassword);
        registerEmail = findViewById(R.id.registerEmail);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signIn);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đăng kí");
        progressDialog.setMessage("Vui lòng đợi trong vài giây...");
        progressDialog.dismiss();

    }
    public void eventButtonListener() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registerEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)) {
                    registerEmail.setError("Vui lòng nhập Email");
                    return;
                }

                String password = registerPassword.getText().toString().trim();
                if(TextUtils.isEmpty(password)) {
                    registerPassword.setError("Vui lòng nhập mật khẩu");
                    return;
                }

                String rePassword = registerRePassword.getText().toString().trim();
                if(TextUtils.isEmpty(rePassword)) {
                    registerRePassword.setError("Vui lòng nhập lại mật khẩu");
                    return;
                }

                // Kiểm tra mật khẩu nhập sai
                if(!checkRePasswordSignUp()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập lại mật khẩu chính xác", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!email.endsWith("@gmail.com")) {
                    Toast.makeText(SignUpActivity.this, "Email phải đúng định dạng example@gmail.com", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                signUpWithEmailAndPassword(email, password);

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);                finish();
                startActivity(intent);
            }
        });
    }

    private void signUpWithEmailAndPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()) {
                    // chuyển qua main activity
                    // tạo progress
                    Map map = new HashMap();
                    map.put("email", email);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                    reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .updateChildren(map);

                    Toast.makeText(SignUpActivity.this, "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();
                    Log.d("Sign Up", "Sign up with firebase success");
                    startMainActivity();
                    finish();
                } else {
                    Log.d("Sign Up Fail", task.getException().getMessage().toString()+"");
                    Toast.makeText(SignUpActivity.this, "Đăng kí tài khoản thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void startMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public boolean checkRePasswordSignUp() {
        String password = registerPassword.getText().toString().trim();
        String rePassword = registerRePassword.getText().toString().trim();
        return password.equals(rePassword);
    }
}