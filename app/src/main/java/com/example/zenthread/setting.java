package com.example.zenthread;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class setting extends AppCompatActivity {

    ImageView setprofile;
    EditText setname, setstatus;
    Button donebut;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String email,password;
     Uri setImageUri;
     ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        setprofile= findViewById(R.id.settingprofile);
        setname= findViewById(R.id.settingname);
        setstatus= findViewById(R.id.settingstatus);
        donebut= findViewById(R.id.donebut);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);

        DatabaseReference reference= database.getReference().child("user").child(Objects.requireNonNull(auth.getUid()));
        StorageReference storageReference= storage.getReference().child("upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email= Objects.requireNonNull(snapshot.child("mail").getValue()).toString();
                password = Objects.requireNonNull(snapshot.child("password").getValue()).toString();
                String name = Objects.requireNonNull(snapshot.child("username").getValue()).toString();
                String profile= Objects.requireNonNull(snapshot.child("profilepic").getValue()).toString();
                String  status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                setname.setText(name);
                setstatus.setText(status);
                Picasso.get().load(profile).into(setprofile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        setprofile.setOnClickListener(v -> {
            Intent intent= new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
        });

        donebut.setOnClickListener(v -> {
            progressDialog.show();

            String name= setname.getText().toString();
            String status= setstatus.getText().toString();
            if(setImageUri!= null){
                storageReference.putFile(setImageUri).addOnCompleteListener(task -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String finalimageUri= uri.toString();
                    Users users= new Users(auth.getUid(),name,email,password,finalimageUri,status);
                    reference.setValue(users).addOnCompleteListener(task1 -> {
                      if(task1.isSuccessful()){
                          progressDialog.dismiss();
                          Toast.makeText(setting.this, "Data Is Saved", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(setting.this,MainActivity.class);
                          startActivity(intent);
                          finish();

                      }else {
                          progressDialog.dismiss();
                          Toast.makeText(setting.this, "Some Thing went Wrong", Toast.LENGTH_SHORT).show();
                      }
                    });

                }));
            }else {
                storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String finalImageUri= uri.toString();
                    Users users= new Users(auth.getUid(),name,email,password,finalImageUri,status);
                    reference.setValue(users).addOnCompleteListener(task -> {
                      if(task.isSuccessful()){
                          progressDialog.dismiss();
                          Toast.makeText(setting.this, "Data Is Saved", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(setting.this,MainActivity.class);
                          startActivity(intent);
                          finish();
                      }else {
                          progressDialog.dismiss();
                          Toast.makeText(setting.this, "Some Thing went Wrong", Toast.LENGTH_SHORT).show();
                      }
                    });
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!= null){
                setImageUri= data.getData();
                setprofile.setImageURI(setImageUri);
            }
        }


    }
}