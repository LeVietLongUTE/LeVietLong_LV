package com.example.doxethongminh.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doxethongminh.ChitietDSXe;
import com.example.doxethongminh.Model.XevaoModel;
import com.example.doxethongminh.R;
import com.example.doxethongminh.fragment.DsXeFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class DanhsachxeAdapter extends FirebaseRecyclerAdapter<XevaoModel, DanhsachxeAdapter.Danhsachmyviewholder> {


    public DanhsachxeAdapter(@NonNull FirebaseRecyclerOptions<XevaoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Danhsachmyviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull XevaoModel model) {
        View view = holder.itemView;
        holder.bienso.setText(model.bienso);
        holder.khuvuc.setText(model.khuvuc);
        holder.thvao.setText(model.thoigianvao);

        Glide.with(holder.imagenguoigui.getContext()).load(model.getImage()).into(holder.imagenguoigui);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ChitietDSXe.class);
                intent.putExtra("key", getRef(position).getKey());
            }
        });

    }


    @NonNull
    @Override
    public Danhsachmyviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_danhsachxe, parent, false);
        return new Danhsachmyviewholder(view);
    }

    public class Danhsachmyviewholder extends RecyclerView.ViewHolder {
        private TextView bienso, khuvuc, thvao;
        RelativeLayout relativeLayout;
        ImageView imagenguoigui;

        public Danhsachmyviewholder(@NonNull View itemView) {
            super(itemView);
            bienso = itemView.findViewById(R.id.txtbienso);
            khuvuc = itemView.findViewById(R.id.txtkhuvuc);
            thvao = itemView.findViewById(R.id.txttgvao);
            imagenguoigui = itemView.findViewById(R.id.imageanh);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
        }
    }
}
