package com.example.doxethongminh.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.doxethongminh.Model.ChontheoveModel;
import com.example.doxethongminh.Model.XevaoModel;
import com.example.doxethongminh.R;
import com.example.doxethongminh.ScanerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class XeratheoveFragment extends Fragment {
    String thoigianra;
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mdatabase = database.getReference();
    private TextView txtma, txttgvao, txttgra, txtcuocphi;


    FirebaseUser user;
    DatabaseReference reference;
    String UserID;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_xeratheove, container, false);
        scannerView = v.findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(getActivity(), scannerView);
        txtma = v.findViewById(R.id.maidxe);
        txttgvao = v.findViewById(R.id.tgxevao);
        txttgra = v.findViewById(R.id.tgxera);
        txtcuocphi = v.findViewById(R.id.money);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        reference.child(UserID).child("chontheove").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //lấy thời gian hiện tại xe ra
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                                thoigianra = sdf.format(new Date());
                                Iterable<DataSnapshot> nodechill = snapshot.getChildren();
                                for (DataSnapshot dataSnapshot : nodechill) {
                                    ChontheoveModel chontheoveModel = dataSnapshot.getValue(ChontheoveModel.class);
//                    Log.d( "onDataChange: kiemtra",xevaoModel.getKhuvuc().toString());
                                    //so sánh biển số trên firebase với người dùng đưa mã .equals
                                    if (chontheoveModel.getMaVe().equals(result.getText())) {
                                        //chill tới node xe vao get key tạo 1 node thoigian ra
                                        reference.child(UserID).child("chontheove").child(dataSnapshot.getKey()).child("thoigianra").setValue(thoigianra);
//                                        Toast.makeText(getActivity(), "Mã Hợp Lệ", Toast.LENGTH_SHORT).show();
                                        txtma.setText(result.getText());
                                        txttgvao.setText(chontheoveModel.getThoigianvao());
                                        txttgra.setText(thoigianra);

                                        //lấy thời gian tính cước phí
//                                        DatabaseReference cuocdatabase = database.getReference();
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
                                                    reference.child(UserID).child("chontheove").child(dataSnapshot.getKey()).child("cuocphi").setValue(cuoc);
                                                    System.out.println("Differance between date " + startDate + " and " + endDate + " is " + getDaysDiff + " days." + cuoc + "cước phí");
                                                    txtcuocphi.setText(cuoc + " Đồng ");

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
                                    }
//                                    else {
//                                        Toast.makeText(getActivity(), "Mã không Hợp Lệ", Toast.LENGTH_SHORT).show();
//                                        txtma.setText("Mã Không hợp lệ!!");
//                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });


        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
                txtma.setText("");
                txttgvao.setText("");
                txttgra.setText("");
                txtcuocphi.setText("");
                demxera();
            }
        });
        codeScanner.startPreview();
        demxera();
        return v;
    }

    public void demxera() {
        reference.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String xera = snapshot.child("tongxera").getValue(String.class);
                String cuocphi = snapshot.child("tongcuocphi").getValue(String.class);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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


//    @Override
//    public void onPause() {
//        super.onPause();
//        codeScanner.stopPreview();
//    }

    @Override
    public void onResume() {
        super.onResume();

        codeScanner.startPreview();
    }
}
