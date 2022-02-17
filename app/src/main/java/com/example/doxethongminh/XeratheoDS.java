package com.example.doxethongminh;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.doxethongminh.Model.ChontheoveModel;
import com.example.doxethongminh.fragment.DsXeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class XeratheoDS extends AppCompatActivity {
    public static final String EXTRA_DATA = "EXTRA_DATA";
    ImageView imageanh;
    TextView bienso, khuvuc, thoigianxevao, thoigianra, cuocphira;
    DatabaseReference Dataref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user;
    DatabaseReference reference;
    String UserID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_xera_theods);
        Dataref = FirebaseDatabase.getInstance().getReference().child("xevao");
        imageanh = findViewById(R.id.anhnguoigui);
        bienso = findViewById(R.id.txtbienso);
        khuvuc = findViewById(R.id.txtkhuvuc);
        thoigianxevao = findViewById(R.id.txttgvao);
        thoigianra = findViewById(R.id.txttgra);
        cuocphira = findViewById(R.id.txtcuocphi);
        Button xong = findViewById(R.id.button);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();
        String key = getIntent().getStringExtra("keya");
        //hiển thị chi tiết item danh sách xe
        reference.child(UserID).child("xevao").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String image = snapshot.child("image").getValue().toString();
                    String biensso = snapshot.child("bienso").getValue().toString();
                    String khuvvuc = snapshot.child("khuvuc").getValue().toString();
                    String thoigianvao = snapshot.child("thoigianvao").getValue().toString();

                    Picasso.get().load(image).into(imageanh);
                    khuvuc.setText(khuvvuc);
                    bienso.setText(biensso);
                    thoigianxevao.setText(thoigianvao);

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                    String thoigianxera = sdf.format(new Date());
                    reference.child(UserID).child("xevao").child(key).child("thoigianra").setValue(thoigianxera);
                    thoigianra.setText(thoigianxera);
                    ChontheoveModel chontheoveModel = snapshot.getValue(ChontheoveModel.class);
                    //lấy thời gian tính cước phí
                    DatabaseReference cuocdatabase = database.getReference();
                    reference.child(UserID).child("cuocphi").child("phitheongay").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String cuocphi = snapshot.getValue(String.class);

                            // tính cước xe ra
                            Date currentDate = new Date();
                            Date date1 = null;
                            Date date2 = null;
                            try {
                                String startDate = chontheoveModel.getThoigianvao();
                                String endDate = sdf.format(currentDate);

                                date1 = sdf.parse(startDate);
                                date2 = sdf.parse(endDate);

                                long getDiff = (date2.getTime() - date1.getTime());
                                //1ngay  24tieng-1tieng  60p  -  1p  60s  -   1s  1000milliseconds
                                long getDaysDiff = (getDiff / (24 * 60 * 60 * 1000) + 1);
                                //đổi kiểu string sang kiểu long
                                long i = Long.parseLong(cuocphi);
                                long ngay = (getDaysDiff) * i;
                                //đổi kiểu long sang string
                                String cuoc = Long.toString(ngay);
                                reference.child(UserID).child("xevao").child(key).child("cuocphi").setValue(cuoc);
                                System.out.println("Differance between date " + startDate + " and " + endDate + " is " + getDaysDiff + " days." + cuoc + "cước phí");
                                cuocphira.setText(cuoc + " Đồng ");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //ket thuc
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
//                                                Toast.makeText(getActivity(), "Mã không Hợp Lệ", Toast.LENGTH_SHORT).show();
                        }
                    });
                    xong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(XeratheoDS.this, MainActivity.class));
                            demxera();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void demxera() {
        reference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String xera = snapshot.child("tongxera").getValue(String.class);
                String cuocphi = snapshot.child("tongcuocphi").getValue(String.class);
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
