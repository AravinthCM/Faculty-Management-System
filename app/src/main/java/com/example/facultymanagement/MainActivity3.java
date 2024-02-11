package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity3 extends AppCompatActivity {

    TextInputLayout fullName,password;
    Button login;
    private FirebaseAuth firebaseAuth;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        progressBar=findViewById(R.id.progress1);
        Button imageButton = findViewById(R.id.already);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        Button adminonto = findViewById(R.id.adminonto);
        adminonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity3.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });

        fullName=findViewById(R.id.fullname);
        password=findViewById(R.id.passs1);
        login=findViewById(R.id.sub2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidateFullname()|!ValidatePassword()){

                }else{

                    loginUser();
                }
            }
        });

    }

    public void loginUser(){
        String userUserName=fullName.getEditText().getText().toString().trim();
        String userPassword=password.getEditText().getText().toString().trim();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(userUserName, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.VISIBLE);

                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity3.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity3.this, MainActivity5.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity3.this, "Login failed. Please check your email and password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Boolean ValidateFullname(){
        String val=fullName.getEditText().getText().toString();

        if (val.isEmpty()){
            fullName.setError("UserId cannot be Empty");
            return false;
        }
        else{
            fullName.setError(null);
            return true;
        }
    }
    private Boolean ValidatePassword(){
        String val=password.getEditText().getText().toString();

        if (val.isEmpty()){
            password.setError("Password cannot be Empty");
            return false;
        }
        else{
            password.setError(null);
            return true;
        }
    }
}
