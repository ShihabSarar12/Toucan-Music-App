package com.example.toucan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {
    private TextView podcastTxt;
    private Button startApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        podcastTxt = findViewById(R.id.linGrad);
        startApp = findViewById(R.id.start_app);
        Shader shader = new LinearGradient(0,0,0,podcastTxt.getLineHeight(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.REPEAT);
        podcastTxt.getPaint().setShader(shader);
        startApp.setOnClickListener(view->{
            startActivity(new Intent(MainActivity.this, Login.class));
        });
    }
}