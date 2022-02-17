package com.example.doxethongminh.fragment;

import android.Manifest;
import android.os.Bundle;
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
import com.example.doxethongminh.MainActivity;
import com.example.doxethongminh.Model.ChontheoveModel;
import com.example.doxethongminh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class chontheoveFragment extends Fragment {
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mdatabase = database.getReference();
    private TextView txtma, txttgvao;

    FirebaseUser user;
    DatabaseReference reference;
    String UserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_chontheove, container, false);
        scannerView = v.findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(getActivity(), scannerView);
        txtma = v.findViewById(R.id.maidxe);
        txttgvao = v.findViewById(R.id.tgxevao);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        SimpleDateFormat thoigianthuc = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());
                        String tgxevao = thoigianthuc.format(new Date());

                        txtma.setText(result.getText());
                        txttgvao.setText(tgxevao);
                        ChontheoveModel chontheoveModel = new ChontheoveModel(txtma.getText().toString(), txttgvao.getText().toString());
                        reference.child(UserID).child("chontheove").push().setValue(chontheoveModel, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getActivity(), "Lưu dữ liệu thành công", Toast.LENGTH_SHORT).show();
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
            }
        });
        codeScanner.startPreview();
        return v;
    }


    @Override
    public void onPause() {
        super.onPause();
        codeScanner.stopPreview();
    }

    @Override
    public void onResume() {
        super.onResume();

        codeScanner.startPreview();
    }
}
