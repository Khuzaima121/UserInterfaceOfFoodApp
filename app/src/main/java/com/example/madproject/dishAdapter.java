package com.example.madproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class dishAdapter extends FirebaseRecyclerAdapter<model_dishes, dishAdapter.dishViewHolder> {

    DatabaseReference reference;
    FirebaseAuth mAuth;
    FirebaseUser user;

    public dishAdapter(@NonNull FirebaseRecyclerOptions<model_dishes> options) {
        super(options);
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull dishViewHolder holder, int i, @NonNull model_dishes modelDishes) {

        holder.dishName.setText(modelDishes.getName());
        holder.dishPrice.setText(modelDishes.getPrice());
        holder.dishDescription.setText(modelDishes.getDescription());

        Glide.with(holder.dishImage.getContext())
                .load(modelDishes.getImageUrl())
                .into(holder.dishImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder order = new AlertDialog.Builder(v.getContext());
                order.setTitle("Choose one");

                order.setPositiveButton("Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder buy = new AlertDialog.Builder(v.getContext());
                        buy.setTitle("Buy now");

                        View view = LayoutInflater.from(v.getContext())
                                .inflate(R.layout.order_layout, null, false);
                        buy.setView(view);
                        EditText etQuantity = view.findViewById(R.id.etQuantity);
                        EditText etAddress = view.findViewById(R.id.etAddress);
                        EditText etName = view.findViewById(R.id.etName);
                        EditText etPhone = view.findViewById(R.id.etPhone);
                        buy.setPositiveButton("Purchase now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (user == null) {
                                    Toast.makeText(view.getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String Name = etName.getText().toString().trim();
                                String Quantity = etQuantity.getText().toString().trim();
                                String Address = etAddress.getText().toString().trim();
                                String Phone = etPhone.getText().toString().trim();
                                int quantity = 1;

                                if (Name.isEmpty()) {
                                    Toast.makeText(view.getContext(), "Enter your Name please", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (Quantity.isEmpty()||Quantity.equals("0")) {
                                    Toast.makeText(view.getContext(), "Quantity taken as 1 ", Toast.LENGTH_SHORT).show();
                                }
                                if (Phone.isEmpty()) {
                                    Toast.makeText(view.getContext(), "Enter Phone Number please", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (Address.isEmpty()) {
                                    Toast.makeText(view.getContext(), "Enter your Address please", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                quantity = Integer.parseInt(Quantity);
                                int total = quantity * Integer.parseInt(modelDishes.getPrice());
                                String DishName = modelDishes.getName();
                                String Total = Integer.toString(total);
                                HashMap<String, Object> data = new HashMap<>();
                                data.put("UserName", Name);
                                data.put("UserPhone", Phone);
                                data.put("TotalBill", Total);
                                data.put("DishName", DishName);
                                data.put("Address", Address);
                                reference.child("Orders")
                                        .push()
                                        .setValue(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(view.getContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(), "Order placement failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        buy.show();
                    }
                });

                order.setNegativeButton("Add to cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String DishName = modelDishes.getName();
                        String Price = modelDishes.getPrice();

                        AlertDialog.Builder add = new AlertDialog.Builder(v.getContext());
                        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.quantity_layout, null, false);
                        add.setView(view);
                        add.setTitle("Add to cart");
                        EditText etQuantity = view.findViewById(R.id.etQuantitty);
                        add.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String Quantity = etQuantity.getText().toString().trim();

                                if (Quantity.isEmpty()||Quantity=="0") {
                                    Toast.makeText(view.getContext(), "Quantity taken as 1 ", Toast.LENGTH_SHORT).show();
                                    Quantity="1";
                                }


                                if (user == null) {
                                    Toast.makeText(view.getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String userId = user.getUid();
                                HashMap<String, Object> cartItem = new HashMap<>();
                                cartItem.put("DishName", DishName);
                                cartItem.put("Price", Price);
                                cartItem.put("Quantity", Quantity);

                                reference.child("Carts").child(userId)
                                        .push()
                                        .setValue(cartItem)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(view.getContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(), "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                        add.show();
                    }
                });

                order.show();
            }
        });
    }

    @NonNull
    @Override
    public dishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dishes_adapter_design, parent, false);
        return new dishViewHolder(v);
    }

    public static class dishViewHolder extends RecyclerView.ViewHolder {
        ImageView dishImage;
        TextView dishName;
        TextView dishPrice;
        TextView dishDescription;

        public dishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.dish_image);
            dishDescription = itemView.findViewById(R.id.dish_description);
            dishPrice = itemView.findViewById(R.id.dish_price);
            dishName = itemView.findViewById(R.id.dish_name);
        }
    }
}
