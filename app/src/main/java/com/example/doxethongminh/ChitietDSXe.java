package com.example.doxethongminh;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.doxethongminh.fragment.DsXeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChitietDSXe extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseUser user;
    DatabaseReference reference;
    String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chitietdanhsachxe);
        ImageView imageholder = findViewById(R.id.anhnguoigui);
        TextView khuvucxe = findViewById(R.id.txtkhuvuc);
        TextView biensoxe = findViewById(R.id.txtbienso);
        TextView thoigianvaoxe = findViewById(R.id.txttgvao);
        Button quaylai = findViewById(R.id.btnquaylai);
        Button btnxera = findViewById(R.id.btnxera);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference().child("xevao");
        String key = getIntent().getStringExtra("key");
        //hiển thị chi tiết item danh sách xe
        reference.child(UserID).child("xevao").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String image = snapshot.child("image").getValue().toString();
                    String biensso = snapshot.child("bienso").getValue().toString();
                    String khuvvuc = snapshot.child("khuvuc").getValue().toString();
                    String thoigianvao = snapshot.child("thoigianvao").getValue().toString();
                    Picasso.get().load(image).into(imageholder);
                    khuvucxe.setText(khuvvuc);
                    biensoxe.setText(biensso);
                    thoigianvaoxe.setText(thoigianvao);

                    btnxera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ChitietDSXe.this, XeratheoDS.class);
                            intent.putExtra("keya", key);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
