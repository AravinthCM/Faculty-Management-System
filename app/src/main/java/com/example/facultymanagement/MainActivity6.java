package com.example.facultymanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity6 extends AppCompatActivity {

    TextView profileName, profileEmail, profilePhone;
    DatabaseReference usersReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileName = findViewById(R.id.name);
        profileEmail = findViewById(R.id.email);
        profilePhone = findViewById(R.id.phoneNo);


        retrieveUserData();
    }

    private void retrieveUserData() {
        // Get the currently logged-in user
        Log.d("UserProfile", "Retrieving user data");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("users");
            String userId = currentUser.getUid();

            // Retrieve user data based on the user ID
            usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);

                        // Update the profile TextViews with user data
                        if (user != null) {
                            Log.d("UserProfile", "User data retrieved successfully");
                            profileName.setText(user.getName());
                            profileEmail.setText(user.getEmail());
                            profilePhone.setText(user.getPhoneNo());
                        }else {
                            Log.e("UserProfile", "User data is null");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }
}
