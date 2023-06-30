package com.example.toucan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Registration extends AppCompatActivity {
    private TextView loginAccBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginAccBtn = findViewById(R.id.loginAccBtn);

        loginAccBtn.setOnClickListener(view->{
            startActivity(new Intent(Registration.this, Login.class));
        });
    }
}