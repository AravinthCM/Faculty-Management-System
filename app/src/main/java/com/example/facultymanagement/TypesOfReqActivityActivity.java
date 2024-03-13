package com.example.facultymanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

        // Reference to the "leaveRequests" node in the database
        DatabaseReference leaveRequestsRef = FirebaseDatabase.getInstance().getReference().child("leaveRequests");

        // Query to fetch leave requests
        leaveRequestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if there are any leave requests
                if (dataSnapshot.exists()) {
                    // If there are leave requests, create FirebaseRecyclerOptions
                    FirebaseRecyclerOptions<MainModel> options =
                            new FirebaseRecyclerOptions.Builder<MainModel>()
                                    .setQuery(leaveRequestsRef, MainModel.class)
                                    .build();

                    mainAdapter = new MainAdapter(options, TypesOfReqActivityActivity.this);
                    recyclerView.setAdapter(mainAdapter);
                    mainAdapter.startListening();
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
