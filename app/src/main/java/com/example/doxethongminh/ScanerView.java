package com.example.doxethongminh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.doxethongminh.Model.CuocphiModel;
import com.example.doxethongminh.Model.XevaoModel;
import com.example.doxethongminh.fragment.xeraFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanerView extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    FirebaseUser user;
    DatabaseReference reference;
    String UserID;
    ZXingScannerView zXingScannerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mdatabase = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);
        mdatabase = database.getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        UserID=user.getUid();

    }

    @Override
    public void handleResult(Result result) {

        reference.child(UserID).child("xevao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //lấy thời gian hiện tại xe ra



                Iterable<DataSnapshot> nodechill = snapshot.getChildren();
                for (DataSnapshot dataSnapshot : nodechill) {
                    XevaoModel xevaoModel = dataSnapshot.getValue(XevaoModel.class);
//                    Log.d( "onDataChange: kiemtra",xevaoModel.getKhuvuc().toString());
                    //so sánh biển số trên firebase với người dùng đưa mã .equals
                    if (xevaoModel.getBienso().toString().equals(result.getText())) {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                        String thoigianra = sdf.format(new Date());
                        // hiển thị ảnh lên imageview lấy url từ firebase
                        reference.child(UserID).child("xevao").child(dataSnapshot.getKey()).child("image").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String message = snapshot.getValue(String.class);
                                Picasso.get().load(message).into(xeraFragment.txtanhnguoigui);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        //chill tới node xe vao get key tạo 1 node thoigian ra
                        reference.child(UserID).child("xevao").child(dataSnapshot.getKey()).child("thoigianra").setValue(thoigianra);
                        Toast.makeText(ScanerView.this, "Mã Hợp Lệ", Toast.LENGTH_SHORT).show();
                        xeraFragment.txtbiensso.setText(result.getText());
                        xeraFragment.txtkhuvuc.setText(xevaoModel.getKhuvuc());
                        xeraFragment.txttgvao.setText(xevaoModel.getThoigianvao());
                        xeraFragment.txtthoigianra.setText(thoigianra);

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
                                    String startDate = xevaoModel.getThoigianvao();
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
                                    reference.child(UserID).child("xevao").child(dataSnapshot.getKey()).child("cuocphi").setValue(cuoc);
                                    System.out.println("Differance between date " + startDate + " and " + endDate + " is " + getDaysDiff + " days." + cuoc + "cước phí");
                                    xeraFragment.txtcuocphi.setText(cuoc + " Đồng ");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //ket thuc
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
//                    else {
//                        Toast.makeText(ScanerView.this, "Mã không Hợp Lệ", Toast.LENGTH_SHORT).show();
//                        xeraFragment.txtbiensso.setText("Mã Không Hợp Lệ!!");
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }
}