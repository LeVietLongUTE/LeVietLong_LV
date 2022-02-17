package com.example.doxethongminh.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doxethongminh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThongkeFragment extends Fragment {
    FirebaseUser user;
    DatabaseReference reference;
    String UserID;
    TextView tongxe, xerabai, tongcuocphi;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_thongke, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();
        tongxe = v.findViewById(R.id.txttongxe);
        xerabai = v.findViewById(R.id.txtxerabai);
        tongcuocphi = v.findViewById(R.id.txtsophi);

        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                    if (appleSnapshot.child("chontheove").child("thoigianra").getValue() == null ||
                            appleSnapshot.child("xevao").child("thoigianra").getValue() == null) {
                        long tongxea = snapshot.child("xevao").getChildrenCount() + snapshot.child("chontheove").getChildrenCount();
                        String tongxe1 = Long.toString(tongxea);
                        tongxe.setText(tongxe1);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String xera = snapshot.child("tongxera").getValue(String.class);
                String cuocphi = snapshot.child("tongcuocphi").getValue(String.class);
                tongcuocphi.setText(cuocphi + " Đồng");
                xerabai.setText(xera);
                reference.child(UserID).child("xevao").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshots) {
                        for (DataSnapshot appleSnapshot : snapshots.getChildren()) {
                            if (appleSnapshot.child("thoigianra").getValue() != null && appleSnapshot.child("cuocphi").getValue() != null) {
                                //đếm xe ra bãi
                                int v = Integer.parseInt(xera) + 1;
                                Log.d("onDataChange: kiemtra", Integer.toString(v));
                                String xedara = Integer.toString(v);
                                //dem tong tien
                                String a = appleSnapshot.child("cuocphi").getValue(String.class);
                                Log.d("onDataChange: kiemtra", a);
                                int z = Integer.parseInt(a);
                                int i = Integer.parseInt(cuocphi);
                                int h = i + z;
                                String cuoc12 = Integer.toString(h);
                                  xoaad();
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

        reference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String xera = snapshot.child("tongxera").getValue(String.class);
                String cuocphi = snapshot.child("tongcuocphi").getValue(String.class);
                tongcuocphi.setText(cuocphi + " Đồng");
                xerabai.setText(xera);
                reference.child(UserID).child("chontheove").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshots) {
                        for (DataSnapshot appleSnapshot : snapshots.getChildren()) {
                            if (appleSnapshot.child("thoigianra").getValue() != null && appleSnapshot.child("cuocphi").getValue() != null) {
                                //đếm xe ra bãi
                                int v = Integer.parseInt(xera) + 1;
                                Log.d("onDataChange: kiemtra", Integer.toString(v));
                                String xedara = Integer.toString(v);
                                //dem tong tien
                                String a = appleSnapshot.child("cuocphi").getValue(String.class);
                                Log.d("onDataChange: kiemtra", a);
                                int z = Integer.parseInt(a);
                                int i = Integer.parseInt(cuocphi);
                                int h = i + z;
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

//
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }

    public void xoa() {
        reference.child(UserID).child("chontheove").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    if (appleSnapshot.child("thoigianra").getValue() != null) {
                        appleSnapshot.getRef().removeValue();
                        Log.d("onDataChange: kiemtra:", appleSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void xoaad() {
        reference.child(UserID).child("xevao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    if (appleSnapshot.child("thoigianra").getValue() != null) {
                        appleSnapshot.getRef().removeValue();
                        Log.d("onDataChange: kiemtra:", appleSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
