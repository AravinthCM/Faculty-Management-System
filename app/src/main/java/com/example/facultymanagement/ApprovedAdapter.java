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
        holder.txtRequestedFacultyName.setText("Requested Faculty Name: " + model.getRequestedUserUid());
        holder.txtLeaveType.setText("Leave Type: " + model.getLeaveType());
        holder.txtStartDate.setText("Start Date: " + formatDate(model.getStartDate()));
        holder.txtEndDate.setText("End Date: " + formatDate(model.getEndDate()));
        holder.txtTimestamp.setText("Timestamp: " + formatDate(new Date(model.getTimestamp())));
    }

    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(date);
        } else {
            return "N/A"; // Or any other appropriate value for null dates
        }
    }


    @Override
    public int getItemCount() {
        return approvedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRequestId, txtRequestedFacultyName, txtLeaveType, txtStartDate, txtEndDate, txtTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRequestId = itemView.findViewById(R.id.txtRequestId);
            txtRequestedFacultyName = itemView.findViewById(R.id.txtRequestedFacultyName);
            txtLeaveType = itemView.findViewById(R.id.txtLeaveType);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);
        }
    }
}
