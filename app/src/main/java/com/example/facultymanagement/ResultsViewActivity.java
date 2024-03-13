package com.example.facultymanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResultsViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<ResultModel, ResultViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_view);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up FirebaseRecyclerAdapter
        DatabaseReference resultsRef = FirebaseDatabase.getInstance().getReference().child("Results");
        FirebaseRecyclerOptions<ResultModel> options =
                new FirebaseRecyclerOptions.Builder<ResultModel>()
                        .setQuery(resultsRef, ResultModel.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ResultModel, ResultViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ResultViewHolder holder, int position, @NonNull ResultModel model) {
                // Bind data to ViewHolder
                holder.bind(model);
            }

            @NonNull
            @Override
            public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create ViewHolder
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
                return new ResultViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // ViewHolder for RecyclerView
    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView txtRequestId, txtRequestedFacultyName, txtStatus, txtTimestamp;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRequestId = itemView.findViewById(R.id.txtRequestId);
            txtRequestedFacultyName = itemView.findViewById(R.id.txtRequestedFacultyName);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);
        }

        public void bind(ResultModel model) {
            // Bind data to TextViews
            txtRequestId.setText("Request ID: " + model.getRequestId());
            txtRequestedFacultyName.setText("Requested Faculty Name: " + model.getRequestedFacultyName());
            txtStatus.setText("Status: " + model.getStatus());
            txtTimestamp.setText("Timestamp: " + model.getTimestamp());
        }
    }
}
