package com.example.zenthread;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class splash extends AppCompatActivity {
    ImageView logo;
    TextView name, own1, own2;
    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //Objects.requireNonNull(getSupportActionBar()).hide();
        logo= findViewById(R.id.logoimg);
        name= findViewById(R.id.logonameimg);
        own1= findViewById(R.id.owner1);
        own2= findViewById(R.id.owner2);

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        logo.setAnimation(topAnim);
        name.setAnimation(bottomAnim);
        own1.setAnimation(bottomAnim);
        own2.setAnimation(bottomAnim);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(splash.this, login.class);
            startActivity(intent);
            finish();
        },4000);

    }
}