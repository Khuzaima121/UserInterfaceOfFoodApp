package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    FloatingActionButton fabCart;
    FirebaseUser user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        init();
        fabCart.setOnClickListener(v->{
            startActivity(new Intent(Home.this,CartItems.class));
        });

        loadCategories();
    }

    private void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();
        rvCategories = findViewById(R.id.rvCatagories);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        fabCart=findViewById(R.id.fabCart);
        rvCategories.setLayoutManager(new WrapContentGridLayoutManager(this, 2));

        reference = FirebaseDatabase.getInstance().getReference().child("categories");

        FirebaseRecyclerOptions<model_catagories> options =
                new FirebaseRecyclerOptions.Builder<model_catagories>()
                        .setQuery(reference, model_catagories.class)
                        .build();

        adapter = new CatagoriesAdapter(options, this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the RecyclerView state
        Parcelable listState = rvCategories.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("recycler_state", listState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the RecyclerView state
        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable("recycler_state");
            rvCategories.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
