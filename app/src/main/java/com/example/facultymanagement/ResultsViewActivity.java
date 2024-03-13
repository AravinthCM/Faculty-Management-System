// MainActivity.java
package com.example.facultymanagement;

import android.os.Bundle;
import android.text.format.DateFormat;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultsViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<ResultModel, ResultViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference resultsRef = FirebaseDatabase.getInstance().getReference().child("Results");
        FirebaseRecyclerOptions<ResultModel> options =
                new FirebaseRecyclerOptions.Builder<ResultModel>()
                        .setQuery(resultsRef, ResultModel.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ResultModel, ResultViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ResultViewHolder holder, int position, @NonNull ResultModel model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView txtRequestedFacultyName, txtStatus, txtApprovedTime;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRequestedFacultyName = itemView.findViewById(R.id.txtRequestedFacultyName);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtApprovedTime = itemView.findViewById(R.id.txtApprovedTime);
        }

        public void bind(ResultModel model) {
            txtRequestedFacultyName.setText("Requested Faculty Name: " + model.getRequestedUserUid());
            txtStatus.setText("Status: " + model.getStatus());
            long timestamp = model.getTimestamp();
            CharSequence approvedTime = DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(timestamp));
            txtApprovedTime.setText("Time of action: " + approvedTime);
        }
    }
}