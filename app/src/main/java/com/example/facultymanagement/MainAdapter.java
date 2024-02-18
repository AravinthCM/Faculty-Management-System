package com.example.facultymanagement;

import static android.content.Context.NOTIFICATION_SERVICE;
//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;
import static androidx.core.content.ContextCompat.getSystemService;
//import com.google.firebase.messaging.FirebaseMessaging;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.NotificationManager;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
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
                        Toast.makeText(v.getContext(), "Leave request approved.", Toast.LENGTH_SHORT).show();
                    }
                });

                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateStatus(holder.getAdapterPosition(), "Declined");
                        dialogPlus.dismiss();
                        Toast.makeText(v.getContext(), "Leave request declined.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateStatus(int position, String newStatus) {
        String requestId = getRef(position).getKey();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && requestId != null) {
            String currentUserId = currentUser.getUid();

            Map<String, Object> map = new HashMap<>();
            map.put("status", newStatus);

            // Update status in the current user's "requests" node
            FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("requests").child(requestId).updateChildren(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Toast.makeText(context, "Done.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Toast.makeText(context, "Error while updating.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }
    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,type, reason, startDate, endDate, status;
        Button btnEdit, btnApprove, btnDecline;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imgimg);
            name = (TextView) itemView.findViewById(R.id.nameText);
            reason = (TextView) itemView.findViewById(R.id.leaveReason);
            type = (TextView) itemView.findViewById(R.id.leaveType);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            endDate = (TextView) itemView.findViewById(R.id.endDate);
            status = (TextView) itemView.findViewById(R.id.status);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
        }
    }
}
