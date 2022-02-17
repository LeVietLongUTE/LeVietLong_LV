package com.example.doxethongminh.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doxethongminh.R;
import com.example.doxethongminh.ScanerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class xeraFragment extends Fragment  {
      public static   TextView txtbiensso,txtkhuvuc,txttgvao,txtthoigianra,txtcuocphi;
    public static    ImageView txtanhnguoigui;
        Button button;
    FirebaseUser user;
    DatabaseReference reference;
    String UserID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.layout_xera,container,false);
        txtbiensso=v.findViewById(R.id.txtbienso);
        txtanhnguoigui=v.findViewById(R.id.anhnguoigui);
        txtkhuvuc=v.findViewById(R.id.txtkhuvuc);
        txttgvao=v.findViewById(R.id.txttgvao);
        txtthoigianra=v.findViewById(R.id.txttgra);
        txtcuocphi=v.findViewById(R.id.txtcuocphi);
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        UserID=user.getUid();
       button=v.findViewById(R.id.button);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//
               startActivity(new Intent(getActivity(), ScanerView.class));
             //  demxera();
           }
       });



        return v;
    }
    public  void  demxera(){
        reference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String xera = snapshot.child("tongxera").getValue(String.class);
                String cuocphi = snapshot.child("tongcuocphi").getValue(String.class);
                reference.child(UserID).child("xevao").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshots) {
                        for (DataSnapshot appleSnapshot: snapshots.getChildren()) {
                            if (appleSnapshot.child("thoigianra").getValue()!=null&&appleSnapshot.child("cuocphi").getValue()!=null){
                                //đếm xe ra bãi
                                int v=  Integer.parseInt(xera)+1;
                                Log.d( "onDataChange: kiemtra",Integer.toString(v));
                                String xedara=Integer.toString(v);
                                //dem tong tien
                                String a=  appleSnapshot.child("cuocphi").getValue(String.class);
                                Log.d( "onDataChange: kiemtra",a);
                                int z = Integer.parseInt(a);
                                int i = Integer.parseInt(cuocphi);
                                int h=i+z;
                                String cuoc12 = Integer.toString(h);
                                xoa();
                                reference.child(UserID).child("tongxera").setValue(xedara);
                                reference.child(UserID).child("tongcuocphi").setValue(cuoc12);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public  void  xoa(){
        reference.child(UserID).child("xevao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    if (appleSnapshot.child("thoigianra").getValue()!=null){
                        appleSnapshot.getRef().removeValue();
                        Log.d( "onDataChange: kiemtra:",appleSnapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
