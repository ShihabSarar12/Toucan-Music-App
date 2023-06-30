package com.example.toucan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText emailTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private TextView createAccBtn;
    private ImageView facebookBtn;
    private ImageView googleBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        loginBtn = findViewById(R.id.loginBtn);
        createAccBtn = findViewById(R.id.createAccBtn);

        loginBtn.setOnClickListener(view->{
            String emailTxtStr = emailTxt.getText().toString();
            String passwordTxtStr = passwordTxt.getText().toString();
            if(emailTxtStr.equals("shihab") && passwordTxtStr.equals("donno")){
                startActivity(new Intent(Login.this, Home.class));
            }
        });
        createAccBtn.setOnClickListener(view->{
            startActivity(new Intent(Login.this, Registration.class));
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));
                        finish();
                    }

                }).create().show();
    }
}