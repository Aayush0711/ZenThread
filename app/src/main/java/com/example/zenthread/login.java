package com.example.zenthread;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login extends AppCompatActivity {
    Button button;
    TextView logsignup;
    EditText email, password;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    android.app.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please Wait... ");
        progressDialog.setCancelable(false);

        auth= FirebaseAuth.getInstance();
        button= findViewById(R.id.Logbutton);
        email= findViewById(R.id.editTextLogEmailAddress);
        password= findViewById(R.id.editTextLogPassword);
        logsignup= findViewById(R.id.logsignup);

        logsignup.setOnClickListener(v -> {
            Intent intent = new Intent(login.this,registration.class);
            startActivity(intent);
            finish();
        });

        button.setOnClickListener(v -> {
            String Email = email.getText().toString();
            String Password= password.getText().toString();

            if(TextUtils.isEmpty(Email)){
                progressDialog.dismiss();
                Toast.makeText(login.this,"Enter the Email",Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(Password)) {
                progressDialog.dismiss();
                Toast.makeText(login.this,"Enter The Password",Toast.LENGTH_SHORT).show();
            }else if(!Email.matches(emailPattern)){
                progressDialog.dismiss();
                email.setError("Give Proper Email Address");

            } else if (password.length()<6) {
                progressDialog.dismiss();
                password.setError("Too Short");
                Toast.makeText(login.this,"Password need be Longer than 6 Character",Toast.LENGTH_SHORT).show();

            }else {
                auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressDialog.show();
                        try {
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch(Exception e){
                            Toast.makeText(login.this, e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        Toast.makeText(login.this, Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
    }
}