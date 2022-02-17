package com.example.doxethongminh.Model;

public class ChontheoveModel {
    String MaVe,thoigianvao;

    public ChontheoveModel(String maVe, String thoigianvao) {
        MaVe = maVe;
        this.thoigianvao = thoigianvao;
    }
    public  ChontheoveModel(){}
    public String getMaVe() {
        return MaVe;
    }

    public void setMaVe(String maVe) {
        MaVe = maVe;
    }

    public String getThoigianvao() {
        return thoigianvao;
    }

    public void setThoigianvao(String thoigianvao) {
        this.thoigianvao = thoigianvao;
    }
}
