package com.example.zenthread;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {
    TextView loginbut;
    EditText rg_username,rg_email,rg_password,rg_repassword;
    Button rg_signup;
    CircleImageView rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Establishing The Account");
        progressDialog.setCancelable(false);
        auth= FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage= FirebaseStorage.getInstance();
        loginbut= findViewById(R.id.Loginbut);
        rg_username= findViewById(R.id.rgusername);
        rg_email= findViewById(R.id.rgemail);
        rg_password= findViewById(R.id.rgpassword);
        rg_repassword= findViewById(R.id.rgrepassword);
        rg_profileImg = findViewById(R.id.profilerg0);
        rg_signup= findViewById(R.id.signupbutton);

        loginbut.setOnClickListener(v -> {
            Intent intent = new Intent(registration.this,login.class);
            startActivity(intent);
            finish();

        });

       rg_signup.setOnClickListener(v -> {
           String namee = rg_username.getText().toString();
           String emaill = rg_email.getText().toString();
           String Password = rg_password.getText().toString();
           String cPassword = rg_repassword.getText().toString();
           String status = "Hey I'm Using This Application";

           if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword)) {
               progressDialog.dismiss();
               Toast.makeText(registration.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
           } else if (!emaill.matches(emailPattern)) {
               progressDialog.dismiss();
               rg_email.setError("Type A Valid Email Here");
           } else if (Password.length()<6) {
               progressDialog.dismiss();
               rg_password.setError("Password Must Be More Than 6 Character ");

           } else if (!Password.equals(cPassword)) {
               progressDialog.dismiss();
               rg_password.setError(" The Password Doesn't Match");

           }else {
               auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(task -> {
                  if (task.isSuccessful()){
                      String id= Objects.requireNonNull(task.getResult().getUser()).getUid();
                      DatabaseReference reference = database.getReference().child("user").child(id);
                      StorageReference storageReference= storage.getReference().child("Upload").child(id);

                      if (imageURI!= null){
                          storageReference.putFile(imageURI).addOnCompleteListener(task1 -> {
                              if (task1.isSuccessful()){
                                  storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                   imageuri = uri.toString();
                                   Users users= new Users(id,namee,emaill,Password,imageuri,status);
                                  reference.setValue(users).addOnCompleteListener(task11 -> {
                                  if (task11.isSuccessful())
                                  {
                                      progressDialog.show();
                                      Intent intent=   new Intent(registration.this,MainActivity.class);
                                      startActivity(intent);
                                      finish();
                                  }else{
                                      Toast.makeText(registration.this,"Error in creating the user",Toast.LENGTH_SHORT).show();
                                  }
                                  });
                                  });
                              }
                          });
                      }else{
                          String status1 ="Hey I'm Using This Application";
                          imageuri = "https://firebasestorage.googleapis.com/v0/b/zenthread-68088.appspot.com/o/man.png?alt=media&token=31ecab2f-084b-4701-a941-ab7ab2579810";
                           Users users = new Users(id,namee,emaill,Password,imageuri, status1);
                          reference.setValue(users).addOnCompleteListener(task12 -> {
                              if (task12.isSuccessful())
                              {
                                  progressDialog.show();
                                  Intent intent=   new Intent(registration.this,MainActivity.class);
                                  startActivity(intent);
                                  finish();
                              }else{
                                  Toast.makeText(registration.this,"Error in creating the user",Toast.LENGTH_SHORT).show();
                              }
                          });
                      }
                  }else {
                      Toast.makeText(registration.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                  }
               });
           }

       });


        rg_profileImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
            if(data!=null){
               imageURI = data.getData();
               rg_profileImg.setImageURI(imageURI);
            }
    }
}