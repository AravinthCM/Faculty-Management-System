// ApprovedAdapter.java
package com.example.facultymanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApprovedAdapter extends RecyclerView.Adapter<ApprovedAdapter.ViewHolder> {

    private List<ApprovedModel> approvedList;
    private OnStatusUpdateListener statusUpdateListener;

    public ApprovedAdapter(List<ApprovedModel> approvedList, OnStatusUpdateListener statusUpdateListener) {
        this.approvedList = approvedList;
        this.statusUpdateListener = statusUpdateListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.approved_item, parent, false);
        return new ViewHolder(view, statusUpdateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApprovedModel model = approvedList.get(position);
        holder.txtRequestId.setText("Faculty Name: " + model.getFacname()); // Assuming facname is the requestId
        holder.txtRequestedFacultyName.setText("Requested Faculty UID " + model.getUserUid());
        holder.txtLeaveType.setText("Leave Type: " + model.getLeavetype());
        holder.txtStartDate.setText("Start Date: " + model.getStartdate());
        holder.txtEndDate.setText("End Date: " + model.getEnddate());
        holder.txtTimestamp.setText("Timestamp: " + formatDate(new Date(model.getTimestamp())));
    }

    private String formatDate(Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(date);
        } else {
            return "N/A";
        }
    }

    @Override
    public int getItemCount() {
        return approvedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRequestId, txtRequestedFacultyName, txtLeaveType, txtStartDate, txtEndDate, txtTimestamp;
        Button btnApprove, btnDecline;

        public ViewHolder(@NonNull View itemView, final OnStatusUpdateListener statusUpdateListener) {
            super(itemView);
            txtRequestId = itemView.findViewById(R.id.txtRequestId);
            txtRequestedFacultyName = itemView.findViewById(R.id.txtRequestedFacultyName);
            txtLeaveType = itemView.findViewById(R.id.txtLeaveType);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDecline = itemView.findViewById(R.id.btnDecline);

            btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (statusUpdateListener != null) {
                        statusUpdateListener.onApproveClicked(getAdapterPosition());
                    }
                }
            });

            btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (statusUpdateListener != null) {
                        statusUpdateListener.onDeclineClicked(getAdapterPosition());
                    }
                }
            });
        }
    }

    public interface OnStatusUpdateListener {
        void onApproveClicked(int position);
        void onDeclineClicked(int position);
    }
}
