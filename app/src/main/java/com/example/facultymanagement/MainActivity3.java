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

public class MainActivity3 extends AppCompatActivity {

    TextInputLayout fullName,password;
    Button login;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Button imageButton = findViewById(R.id.already);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        Button sub2 = findViewById(R.id.sub2);
        sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity3.this, MainActivity5.class);
                startActivity(intent);
            }
        });

        /*
        fullName=findViewById(R.id.fullname);
        password=findViewById(R.id.passs1);
        login=findViewById(R.id.sub2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=fullName.getEditText().toString();
                String getpassword=password.getEditText().toString();

                login(email,getpassword);
            }
        });*/
    }

   /* private void login(String email,String getpassword){
        firebaseAuth.signInWithEmailAndPassword(email, getpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login success, update UI or navigate to the next screen
                            onLoginSuccess();
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(MainActivity3.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void onLoginSuccess() {
        // Update UI or navigate to the next screen (e.g., HomeActivity)
        Intent intent = new Intent(MainActivity3.this, MainActivity5.class);
        startActivity(intent);
        finish(); // Optionally, finish the login activity to prevent going back
    }*/
}
