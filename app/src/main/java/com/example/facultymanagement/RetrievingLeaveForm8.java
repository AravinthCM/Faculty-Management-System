package com.example.facultymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RetrievingLeaveForm8 extends AppCompatActivity{


    private ListView listView;

    ImageView img;
    private DatabaseReference leaveRequestsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieving_leave_form8);

        img=findViewById(R.id.menu);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(RetrievingLeaveForm8.this, MainActivity5.class);
                startActivity(intent);
            }
        });

        listView=findViewById(R.id.listView);
        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.item,R.id.textView,list);
        listView.setAdapter(adapter);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("leaveRequests");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    UserList info=snapshot1.getValue(UserList.class);
                    String txt="Name:           "+info.getFacname()+"\n\n"+"Reason:        "+ info.getLeavereason()+"\n\n"+"Start Date:    "+info.getStartdate()+"\n\n"+"End Date:     "+info.getEnddate()+"\n\n"+"Status:          "+info.getStatus();
                    list.add(txt);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
