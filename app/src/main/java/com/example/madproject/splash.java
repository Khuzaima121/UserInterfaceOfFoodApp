package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class splash extends AppCompatActivity {


    ImageView ivlogo;
    TextView tvslogan;
    FirebaseUser user;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);



        init();
        if(user!=null&&user.isEmailVerified())
        {

           MoveToHome();
        }
        else {
            MoveToMain();
        }






    }

    private void MoveToHome()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(splash.this,Home.class);
                startActivity(i);
                finish();
            }
        },2000);
    }
    private void MoveToMain()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(splash.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },2000);
    }

    private void init()
    {
        tvslogan=findViewById(R.id.tvslogan);
        ivlogo=findViewById(R.id.ivlogo);
        Animation slogan_anim= AnimationUtils.loadAnimation(this,R.anim.slogan_animation);
        Animation logo_anim= AnimationUtils.loadAnimation(this,R.anim.logo_animation);
        ivlogo.setAnimation(logo_anim);
        tvslogan.setAnimation(slogan_anim);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();


        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
    }






}