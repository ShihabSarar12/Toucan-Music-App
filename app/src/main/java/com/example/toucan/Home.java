package com.example.toucan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottomNavigation);

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