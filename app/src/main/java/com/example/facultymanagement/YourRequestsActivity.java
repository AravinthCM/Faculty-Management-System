package com.example.facultymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class YourRequestsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_requests);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentUserEmail = currentUser.getEmail();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            usersRef.orderByChild("Email").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentUserId = null;
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        currentUserId = userSnapshot.getKey();
                        break;
                    }

                    if (currentUserId != null) {
                        FirebaseRecyclerOptions<MainModel> options =
                                new FirebaseRecyclerOptions.Builder<MainModel>()
                                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("requests"), MainModel.class)
                                        .build();

                        mainAdapter = new RequestAdapter(options);
                        recyclerView.setAdapter(mainAdapter);
                        mainAdapter.startListening(); // Start listening here
                    } else {
                        // Handle the case when the current user's ID is not found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        } else {
            // Handle the case when the current user is null (not signed in)
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // No need to start mainAdapter here
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
    }
}
