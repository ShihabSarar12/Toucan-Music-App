package com.example.toucan;

import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Login extends AppCompatActivity {
    private final int GOOGLE_SIGN_IN_REQ_CODE = 100;
    private EditText emailTxt;
    private EditText passwordTxt;
    private Button loginBtn;
    private TextView createAccBtn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private ImageView facebookBtn;
    private ImageView googleBtn;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        loginBtn = findViewById(R.id.loginBtn);
        createAccBtn = findViewById(R.id.createAccBtn);
        progressbar = findViewById(R.id.progressbar);
        googleBtn = findViewById(R.id.googleBtn);
        facebookBtn = findViewById(R.id.facebookBtn);
        mAuth = FirebaseAuth.getInstance();
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

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
        googleBtn.setOnClickListener(view->{
            progressbar.setVisibility(View.VISIBLE);
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult(intent, GOOGLE_SIGN_IN_REQ_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == GOOGLE_SIGN_IN_REQ_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Toast.makeText(getApplicationContext(), "Redirecting", Toast.LENGTH_SHORT).show();
                task.getResult(ApiException.class);
                finish();
                startActivity(new Intent(getApplicationContext(), Home.class));
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}