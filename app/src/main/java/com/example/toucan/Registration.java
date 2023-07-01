package com.example.toucan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

public class Registration extends AppCompatActivity {
    private final int GALLERY_REQ_CODE = 1000;
    private TextView loginAccBtn;
    private ShapeableImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginAccBtn = findViewById(R.id.loginAccBtn);
        profileImg = findViewById(R.id.profileImg);

        profileImg.setOnClickListener(view->{
            Intent iGallery = new Intent(Intent.ACTION_PICK);
            iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });
        loginAccBtn.setOnClickListener(view->{
            startActivity(new Intent(Registration.this, Login.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode == GALLERY_REQ_CODE){
            profileImg.setImageURI(data.getData());
        }
    }
}