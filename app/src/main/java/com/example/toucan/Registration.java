package com.example.toucan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private final int GALLERY_REQ_CODE = 1000;
    private TextView loginAccBtn;
    private ShapeableImageView profileImg;
    private TextView uploadImg;
    private EditText emailTxt;
    private EditText passwordTxt;
    private Button regBtn;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginAccBtn = findViewById(R.id.loginAccBtn);
        profileImg = findViewById(R.id.profileImg);
        uploadImg = findViewById(R.id.uploadImg);
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        regBtn = findViewById(R.id.regBtn);
        progressbar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        profileImg.setOnClickListener(view->{
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });
        loginAccBtn.setOnClickListener(view->{
            startActivity(new Intent(Registration.this, Login.class));
        });
        regBtn.setOnClickListener(view->{
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
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                } else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode == GALLERY_REQ_CODE){
            profileImg.setImageURI(data.getData());
            uploadImg.setText("");
        }
    }
}