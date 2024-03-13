package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TypesOfReqActivityActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.types_of_req_activity);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Query to fetch users who have leave requests
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("requests");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through each user node
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Check if the user has leave requests
                    if (userSnapshot.hasChild("requests")) {
                        // If the user has leave requests, add them to the options
                        FirebaseRecyclerOptions<MainModel> options =
                                new FirebaseRecyclerOptions.Builder<MainModel>()
                                        .setQuery(userSnapshot.child("requests").getRef(), MainModel.class)
                                        .build();

                        // Initialize adapter with options
                        mainAdapter = new MainAdapter(options, TypesOfReqActivityActivity.this);
                        recyclerView.setAdapter(mainAdapter);
                        mainAdapter.startListening();
                        break; // Break the loop since we found a user with leave requests
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }
    }
}
