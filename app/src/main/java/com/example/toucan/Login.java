package com.example.toucan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Login extends AppCompatActivity {
    private EditText emailTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private TextView createAccBtn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
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
        progressbar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(view->{
            String email = emailTxt.getText().toString().trim();
            String password = passwordTxt.getText().toString().trim();
            if (email.isEmpty()){
                emailTxt.setError("Please enter valid email");
                emailTxt.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailTxt.setError("Please enter valid email");
                emailTxt.requestFocus();
                return;
            }
            if (password.isEmpty()){
                passwordTxt.setError("Please enter password");
                passwordTxt.requestFocus();
                return;
            }
            if (password.length()<6){
                passwordTxt.setError("Password must contain atleast 6 char");
                passwordTxt.requestFocus();
                return;
            }
            progressbar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            });
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
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create().show();
    }
}