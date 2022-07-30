package com.dmt.ledonchung.coffeehouse.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dmt.ledonchung.coffeehouse.MainActivity;
import com.dmt.ledonchung.coffeehouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView logo;
    private int SPLASH_SCREEN = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        initUI();
        setAnimLogoSplashActivity();
        postDelayedLogo();
    }
    public void initUI() {
        logo = findViewById(R.id.logo);
    }

    public void setAnimLogoSplashActivity() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_logo_splash_activity);
        logo.setAnimation(animation);

    }
    public void postDelayedLogo() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity();
            }
        }, SPLASH_SCREEN);
    }

    private void startActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent login = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(login);
        } else {
            Intent main = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(main);
        }
        finish();
    }
}