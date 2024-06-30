package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Signup extends AppCompatActivity {
    TextInputEditText tietemial,tietpass,tietcpass;
    Button btnsignin;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        init();
        Signin();


    }
    private void Signin()
    {
        btnsignin.setOnClickListener(v -> {
            String email=tietemial.getText().toString().trim();
            String Pass=tietpass.getText().toString();
            String cPass=tietcpass.getText().toString();
            if(email.isEmpty())
            {
                tietemial.setError("Email can't be empty");
                return;
            }
            if(Pass.isEmpty())
            {
                tietpass.setError("Password can't be empty");
                return;
            }
            if(cPass.isEmpty())
            {
                tietcpass.setError("Confirm can't be empty");
                return;
            }
            if(!Pass.equals(cPass))
            {
                tietcpass.setError("Password and current password mismatched");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, Pass)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnSuccessListener(unused -> {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Signup.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                        startActivity(new Intent(Signup.this, Login.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Signup.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
    private void init()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();
        tietemial=findViewById(R.id.tietEmail);
        tietpass=findViewById(R.id.tietPass);
        tietcpass=findViewById(R.id.tietcPass);
        btnsignin=findViewById(R.id.btnsignin);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth=FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
    }
}