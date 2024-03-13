package com.example.facultymanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {

    private Context context; // Declare context variable

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options, Context context) {
        super(options);
        this.context = context; // Initialize context variable
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,int position, @NonNull MainModel model) {
        holder.name.setText(model.getFacname());
        holder.type.setText(model.getLeavetype());
        holder.reason.setText(model.getLeavereason());
        holder.startDate.setText(model.getStartdate());
        holder.endDate.setText(model.getEnddate());
        holder.status.setText(model.getStatus());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
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
                        Toast.makeText(context, "Leave request approved.", Toast.LENGTH_SHORT).show(); // Use context variable
                    }
                });

                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateStatus(holder.getAdapterPosition(), "Declined");
                        dialogPlus.dismiss();
                        Toast.makeText(context, "Leave request declined.", Toast.LENGTH_SHORT).show(); // Use context variable
                    }
                });
            }
        });
    }

    private void updateStatus(int position, String newStatus) {
        MainModel model = getItem(position);
        if (model != null) {
            String requestId = getRef(position).getKey(); // Get the leave request ID

            if (requestId != null) {
                String userUid = model.getFacname(); // Get the user's unique ID

                // Update status in the existing leave request node under the user's ID
                FirebaseDatabase.getInstance().getReference()
                        .child("users").child(userUid).child(requestId).child("status").setValue(newStatus)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Leave request " + newStatus.toLowerCase() + ".", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Error while updating.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, type, reason, startDate, endDate, status;
        Button btnEdit;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgimg);
            name = itemView.findViewById(R.id.nameText);
            reason = itemView.findViewById(R.id.leaveReason);
            type = itemView.findViewById(R.id.leaveType);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            status = itemView.findViewById(R.id.status);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
