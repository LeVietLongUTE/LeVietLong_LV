package com.example.doxethongminh.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doxethongminh.R;

import java.text.BreakIterator;

public class MyViewholder extends RecyclerView.ViewHolder {

    TextView bienso,khuvuc,thvao;
    RelativeLayout relativeLayout;
    ImageView imagenguoigui;
    public MyViewholder(@NonNull View itemView) {
        super(itemView);
        bienso=itemView.findViewById(R.id.txtbienso);
        khuvuc=itemView.findViewById(R.id.txtkhuvuc);
        thvao=itemView.findViewById(R.id.txttgvao);
        imagenguoigui=itemView.findViewById(R.id.imageanh);
        relativeLayout=itemView.findViewById(R.id.relativelayout);
    }
}
