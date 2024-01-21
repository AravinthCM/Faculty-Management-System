package com.example.facultymanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.name.setText(model.getFacname());
        holder.reason.setText(model.getLeavereason());
        holder.startDate.setText(model.getStartdate());
        holder.endDate.setText(model.getEnddate());
        holder.status.setText(model.getStatus());

        //Glide.with(holder.img)

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1000)
                        .create();

                dialogPlus.show();

                View view = dialogPlus.getHolderView();

                TextView name=view.findViewById(R.id.nameEdit);
                TextView reason=view.findViewById(R.id.reasonEdit);
                TextView startDate=view.findViewById(R.id.stdEdit);
                TextView endDate=view.findViewById(R.id.endEdit);
                EditText status=view.findViewById(R.id.txtStatus);

                Button btnUpdate=view.findViewById(R.id.btnUpdate);

                name.setText(model.getFacname());
                reason.setText(model.getLeavereason());
                startDate.setText(model.getStartdate());
                endDate.setText(model.getEnddate());

                status.setText(model.getStatus());
                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("status",status.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("leaveRequests")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.status.getContext(), "Data Updated Successfully.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.status.getContext(), "Error while updating.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name,reason,startDate,endDate,status;
        Button btnEdit;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img=(ImageView) itemView.findViewById(R.id.imgimg);
            name=(TextView) itemView.findViewById(R.id.nameText);
            reason=(TextView) itemView.findViewById(R.id.leaveReason);
            startDate=(TextView) itemView.findViewById(R.id.startDate);
            endDate=(TextView) itemView.findViewById(R.id.endDate);
            status=(TextView) itemView.findViewById(R.id.status);

            btnEdit=(Button) itemView.findViewById(R.id.btnEdit);

        }
    }
}

