package com.example.toucan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private FirebaseDatabase db;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        String key = reference.push().getKey();
        reference.child(key).setValue(new Track("good music", 4.5));
        Toast.makeText(getApplicationContext(), "successfully added!", Toast.LENGTH_LONG).show();

        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottomHome){
                return true;
            } else if (item.getItemId() == R.id.bottomSearch) {
                return true;
            } else if (item.getItemId() == R.id.bottomMusic){
                startActivity(new Intent(Home.this, Music.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottomPerson) {
                return true;
            }
            return false;
        });
    }
}