package com.example.toucan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private DatabaseReference reference;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private TextView homeTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        homeTxt = findViewById(R.id.homeTxt);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account != null){
            homeTxt.setText(account.getDisplayName()+" "+account.getEmail());
        }

        reference = FirebaseDatabase.getInstance().getReference("Track");
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
                startActivity(new Intent(Home.this, Profile.class));
                finish();
                return true;
            }
            return false;
        });
        homeTxt.setOnClickListener(view->{
            googleSignInClient.signOut().addOnCompleteListener(task->{
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}