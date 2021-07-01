package com.example.justalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView userRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> list;
    ImageView log_out, setting_btn;
    boolean doubleBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference().child("user");

        list = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    list.add(users);

                    if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid()))
                            list.remove(users);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRecyclerView = findViewById(R.id.userRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(HomeActivity.this,list);
        userRecyclerView.setAdapter(adapter);
        log_out = findViewById(R.id.log_out);

        setting_btn = findViewById(R.id.settings_btn);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(HomeActivity.this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog_layout);

                TextView yes_btn,no_btn;

                yes_btn = dialog.findViewById(R.id.yes_btn);
                no_btn = dialog.findViewById(R.id.no_btn);

                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });

                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            }
        });

        if(auth.getCurrentUser() == null)
        {
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        if(doubleBackPressed)
        {
            super.onBackPressed();
            return;
        }
        Toast.makeText(HomeActivity.this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        doubleBackPressed = true;
    }
}