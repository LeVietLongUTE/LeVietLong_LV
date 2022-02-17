package com.example.doxethongminh.Model;

public class XevaoModel {
   public  String image;
    public  String khuvuc;
      public  String bienso;
 public  String maqrcode;
    public  String thoigianvao;

    public XevaoModel(String image, String khuvuc,String bienso,String maqrcode,String thoigianvao) {
        this.image = image;
        this.khuvuc = khuvuc;
        this.bienso = bienso;
        this.maqrcode = maqrcode;
        this.thoigianvao = thoigianvao;
    }

    public XevaoModel() {

    }

    public String getThoigianvao() {
        return thoigianvao;
    }

    public void setThoigianvao(String thoigianvao) {
        this.thoigianvao = thoigianvao;
    }

    public String getMaqrcode() {
        return maqrcode;
    }

    public void setMaqrcode(String maqrcode) {
        this.maqrcode = maqrcode;
    }

    public String getBienso() {
        return bienso;
    }

    public void setBienso(String bienso) {
        this.bienso = bienso;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKhuvuc() {
        return khuvuc;
    }

    public void setKhuvuc(String khuvuc) {
        this.khuvuc = khuvuc;
    }
}
