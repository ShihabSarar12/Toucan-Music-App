package com.example.toucan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Objects;

public class Profile extends AppCompatActivity {
    private Button uploadBtn;
    private TextView uploadTxt;
    private StorageReference reference;
    private DatabaseReference databaseReference;
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uploadBtn = findViewById(R.id.uploadBtn);
        uploadTxt = findViewById(R.id.uploadTxt);
        reference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        uploadBtn.setOnClickListener(view->{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri selectedFileUri = data.getData();
            uploadAudioFile(selectedFileUri);
        }
    }

    private void uploadAudioFile(Uri fileUri) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading files...");
        progressDialog.show();
        StorageReference upRef = reference.child("Uploads/"+fileUri.getLastPathSegment());
        upRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    upRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        storeInDatabase(downloadUrl);
                    });
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Successfully uploaded", Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progress = (double)(100 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded: "+(int)progress+"%");
                })
                .addOnFailureListener(exception-> {
                    Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }

    private void storeInDatabase(String downloadUrl){
        databaseReference.child("audio").push().setValue(downloadUrl);
    }
}