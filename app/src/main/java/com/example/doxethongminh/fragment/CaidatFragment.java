package com.example.doxethongminh.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class CaidatFragment extends Fragment {
    FirebaseUser user;
    DatabaseReference reference;
    String UserID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mdataReference = database.getReference();
    RelativeLayout setphieugui, reset;
    TextView txtcuoc;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_caidat, container, false);
        setphieugui = v.findViewById(R.id.phiguixe);
        reset = v.findViewById(R.id.caidat);
        txtcuoc = v.findViewById(R.id.money);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();
        hienthicuocphi();
        setphieugui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capnhatcuocphi();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        return v;
    }

    public void capnhatcuocphi() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_dialog_feeback);
        //ánh xạ
        Button btnhuy = dialog.findViewById(R.id.btnhuy);
        Button btnthemvao = dialog.findViewById(R.id.btnthem);
        EditText txttext = dialog.findViewById(R.id.txtgiatien);
        btnthemvao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtcuocphi = txttext.getText().toString().trim();
                reference.child(UserID).child("cuocphi").child("phitheongay").setValue(txtcuocphi);
                dialog.dismiss();
                hienthicuocphi();
                Toast.makeText(getActivity(), "thay đổi cước phí thành công", Toast.LENGTH_SHORT).show();

            }
        });

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //dialog.cancel
            }
        });
        dialog.show();
    }
    public void delete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận để xóa..!!!");
        // Icon Of Alert Dialog
        alertDialogBuilder.setIcon(R.drawable.ic_baseline_delete_forever_24);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Bạn có muốn xóa?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                reference.child(UserID).child("chontheove").removeValue();
                reference.child(UserID).child("xevao").removeValue();
                hienthicuocphi();
                Toast.makeText(getActivity(), "xóa dữ liệu thành công", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child("tongxera").setValue("0");
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child("tongcuocphi").setValue("0");
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).child("cuocphi").child("phitheongay").setValue("2000");
                hienthicuocphi();
                //Đóng Activity hiện tại
//                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Bạn đã click vào nút không đồng ý", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Bạn đã click vào nút hủy", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void hienthicuocphi() {
        reference.child(UserID).child("cuocphi").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String hienthi = snapshot.child("phitheongay").getValue(String.class);
                txtcuoc.setText(hienthi+" Đồng");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
