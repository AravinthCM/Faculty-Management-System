package com.example.facultymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {
    TextInputLayout regName, regEmail, regPhoneno, regPassword;
    Button regBtn, regTologin;
    FirebaseDatabase rootnode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button imageButton = findViewById(R.id.already);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.email);
        regPhoneno = findViewById(R.id.phone);
        regPassword = findViewById(R.id.passs1);
        regBtn = findViewById(R.id.submit);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser(){

        String name = regName.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phone = regPhoneno.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("users");

        if (!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail()) {
            return;
        } else{
            // Implement Firebase registration logic
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Registration success
                                saveUserData(name, email, phone, password);
                                Toast.makeText(MainActivity2.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity2.this, MainActivity3.class));
                                finish();
                            } else {
                                Toast.makeText(MainActivity2.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });;

        }
    }

    private void saveUserData(String name, String email, String phone, String password) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        User user1 = new User(name, email, phone, password);
        usersRef.child(userId).setValue(user1);
    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be Empty");
            return false;
        } else {
            regName.setError(null);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "^[a-zA-Z]+\\.(\\d+)@srec\\.ac\\.in$";   //
        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhoneno.getEditText().getText().toString();
        if (val.isEmpty()) {
            regPhoneno.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneno.setError(null);
            regPhoneno.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                //"(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

}