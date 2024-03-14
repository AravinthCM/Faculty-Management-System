// ApprovedAdapter.java
package com.example.facultymanagement;

//import static android.support.v4.media.MediaBrowserCompatApi23.getItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

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
        holder.txtLeaveReason.setText("Leave Type: " + model.getLeavereason());
        holder.txtStartDate.setText("Start Date: " + model.getStartdate());
        holder.txtEndDate.setText("End Date: " + model.getEnddate());
        holder.txtTimestamp.setText("Timestamp: " + formatDate(new Date(model.getTimestamp())));
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1000)
                        .create();

                dialogPlus.show();
                View view = dialogPlus.getHolderView();

                TextView name = view.findViewById(R.id.nameEdit);
                TextView type = view.findViewById(R.id.leaveEdit);
                TextView reason = view.findViewById(R.id.reasonEdit);
                TextView startDate = view.findViewById(R.id.stdEdit);
                TextView endDate = view.findViewById(R.id.endEdit);
                TextView status = view.findViewById(R.id.stsss);

                Button btnApprove = view.findViewById(R.id.btnApprove);
                Button btnDecline = view.findViewById(R.id.btnDecline);

                name.setText(model.getFacname());
                type.setText(model.getLeavetype());
                reason.setText(model.getLeavereason());
                startDate.setText(model.getStartdate());
                endDate.setText(model.getEnddate());
                status.setText(model.getStatus());
                dialogPlus.show();
                btnApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateStatus(holder.getAdapterPosition(), "Approved");
                        dialogPlus.dismiss();
                    }
                });

                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateStatus(holder.getAdapterPosition(), "Declined");
                        dialogPlus.dismiss();
                    }
                });
            }
        });

    }

    private void updateStatus(int position, String newStatus) {
        ApprovedModel model = approvedList.get(position);
        if (model != null) {
            String requestId = model.getFacname();
            if (requestId != null) {
                DatabaseReference leaveRequestsRef = FirebaseDatabase.getInstance().getReference().child("leaveRequests").child(requestId);
                leaveRequestsRef.child("status").setValue("Hr " + newStatus);
                leaveRequestsRef.child("timestamp").setValue(ServerValue.TIMESTAMP);
            }
        }
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

        ImageView img;
        TextView txtRequestId, txtRequestedFacultyName, txtLeaveType,txtLeaveReason, txtStartDate, txtEndDate, txtTimestamp;
        Button btnApprove, btnDecline;

        Button btnEdit;

        public ViewHolder(@NonNull View itemView, final OnStatusUpdateListener statusUpdateListener) {
            super(itemView);
            img = itemView.findViewById(R.id.imgimg);
            txtRequestId = itemView.findViewById(R.id.txtRequestId);
            txtRequestedFacultyName = itemView.findViewById(R.id.txtRequestedFacultyName);
            txtLeaveType = itemView.findViewById(R.id.txtLeaveType);
            txtLeaveReason = itemView.findViewById(R.id.txtLeaveReason);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnDecline = itemView.findViewById(R.id.btnDecline);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            /*btnApprove.setOnClickListener(new View.OnClickListener() {
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
            });*/
        }
    }

    public interface OnStatusUpdateListener {
        void onApproveClicked(int position);
        void onDeclineClicked(int position);
    }
}
