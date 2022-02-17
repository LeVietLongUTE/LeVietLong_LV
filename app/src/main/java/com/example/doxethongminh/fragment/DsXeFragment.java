package com.example.doxethongminh.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doxethongminh.ChitietDSXe;
import com.example.doxethongminh.Model.XevaoModel;
import com.example.doxethongminh.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class DsXeFragment extends Fragment {
    private RecyclerView recyclerView;
    FirebaseRecyclerOptions<XevaoModel> options;
    FirebaseRecyclerAdapter<XevaoModel, MyViewholder> adapter;
    DatabaseReference Dataref;
    EditText search;
    FirebaseUser user;
    DatabaseReference reference;
    String UserID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_danhsachxe, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        search = v.findViewById(R.id.txtsearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();
        Dataref = reference.child(UserID).child("xevao");
        options = new FirebaseRecyclerOptions.Builder<XevaoModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Users").child(UserID).child("xevao"), XevaoModel.class)
                .build();

        //load dữ liệu lên recyclerview
        loadData("");
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    String txtsearch = search.getText().toString();
                    txtsearch = txtsearch.toUpperCase();
                    txtsearch = txtsearch.trim();
                    loadData(txtsearch.toString());
                } else {
                    loadData("");
                }
            }
        });
        return v;
    }

    private void loadData(String data) {
        Query query = Dataref.orderByChild("bienso").startAt(data).endAt(data + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<XevaoModel>().setQuery(query, XevaoModel.class).build();
        adapter = new FirebaseRecyclerAdapter<XevaoModel, MyViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewholder holder, @SuppressLint("RecyclerView") int position, @NonNull XevaoModel model) {
                holder.bienso.setText(model.bienso);
                holder.khuvuc.setText(model.khuvuc);
                holder.thvao.setText(model.thoigianvao);
                Glide.with(holder.imagenguoigui.getContext()).load(model.getImage()).into(holder.imagenguoigui);
                //xử lý sự kiện khi click vao 1 item
                holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ChitietDSXe.class);
                        intent.putExtra("key", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_danhsachxe, parent, false);
                return new MyViewholder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
