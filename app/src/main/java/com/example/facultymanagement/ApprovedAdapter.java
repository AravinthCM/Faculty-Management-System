package com.example.facultymanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ApprovedAdapter extends RecyclerView.Adapter<ApprovedAdapter.ViewHolder> {

    private List<ApprovedModel> approvedList;

    public ApprovedAdapter(List<ApprovedModel> approvedList) {
        this.approvedList = approvedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApprovedModel model = approvedList.get(position);
        holder.txtRequestId.setText("Request ID: " + model.getRequestId());
        holder.txtRequestedFacultyName.setText("Requested Faculty Name: " + model.getRequestedFacultyName());

        // Convert timestamp to formatted date string
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(new Date(model.getTimestamp()));
        holder.txtTimestamp.setText("Timestamp: " + formattedDate);
    }

    @Override
    public int getItemCount() {
        return approvedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRequestId, txtRequestedFacultyName, txtTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRequestId = itemView.findViewById(R.id.txtRequestId);
            txtRequestedFacultyName = itemView.findViewById(R.id.txtRequestedFacultyName);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);
        }
    }
}
