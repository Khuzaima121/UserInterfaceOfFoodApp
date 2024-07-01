package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Home extends AppCompatActivity {

    RecyclerView rvCategories;

    DatabaseReference reference;
    Button btnsignout;
    CatagoriesAdapter adapter;

    FirebaseUser user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        init();
        btnsignout.setOnClickListener(v->{
            mAuth.signOut();
            startActivity(new Intent(Home.this,MainActivity.class));
        });


        loadCategories();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();
        btnsignout=findViewById(R.id.btnsignout);
        rvCategories = findViewById(R.id.rvCatagories);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        rvCategories.setLayoutManager(new GridLayoutManager(this,2));

        reference = FirebaseDatabase.getInstance().getReference().child("categories");

        FirebaseRecyclerOptions<model_catagories> options =
                new FirebaseRecyclerOptions.Builder<model_catagories>()
                        .setQuery(reference, model_catagories.class)
                        .build();

        adapter = new CatagoriesAdapter(options,this);
        rvCategories.setAdapter(adapter);
    }

    private void loadCategories() {
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
