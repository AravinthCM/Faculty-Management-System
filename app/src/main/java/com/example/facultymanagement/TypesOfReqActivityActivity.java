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

        DatabaseReference leaveRequestsRef = FirebaseDatabase.getInstance().getReference().child("leaveRequests");
        leaveRequestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
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
