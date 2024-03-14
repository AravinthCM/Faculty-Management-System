// RetrieveApprovedRequests.java
package com.example.facultymanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrieveApprovedRequests extends AppCompatActivity implements ApprovedAdapter.OnStatusUpdateListener {

    private RecyclerView recyclerView;
    private ApprovedAdapter adapter;
    private List<ApprovedModel> approvedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_approved_requests);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        approvedList = new ArrayList<>();
        adapter = new ApprovedAdapter(approvedList, this);
        recyclerView.setAdapter(adapter);

        retrieveApprovedRequests();
    }

    private void retrieveApprovedRequests() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("leaveRequests");
        ref.orderByChild("status").equalTo("Approved").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                approvedList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ApprovedModel model = snapshot.getValue(ApprovedModel.class);
                    approvedList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

    @Override
    public void onApproveClicked(int position) {
        ApprovedModel approvedModel = approvedList.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("leaveRequests").child(approvedModel.getFacname());
        ref.child("status").setValue("Hr Approve");
    }

    @Override
    public void onDeclineClicked(int position) {
        ApprovedModel approvedModel = approvedList.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("leaveRequests").child(approvedModel.getFacname());
        ref.child("status").setValue("Hr Decline");
    }
}

