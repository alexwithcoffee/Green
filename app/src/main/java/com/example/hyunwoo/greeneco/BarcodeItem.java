package com.example.hyunwoo.greeneco;

/**
 * Created by hyunwoo on 2017-11-24.
 */

public class BarcodeItem {
    String no;
    String kind;
    String create;
    Double KG;
    Double kg;
    String bc;
    String pro;
    int status;

    public BarcodeItem(String no, String kind, String create, Double KG, Double kg, String bc, String pro, int status) {
        this.no = no;
        this.kind = kind;
        this.create = create;
        this.KG = KG;
        this.kg = kg;
        this.bc = bc;
        this.pro = pro;
        this.status = status;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public Double getKG() {
        return KG;
    }

    public void setKG(Double KG) {
        this.KG = KG;
    }

    public Double getKg() {
        return kg;
    }

    public void setKg(Double kg) {
        this.kg = kg;
    }

    public String getBc() {
        return bc;
    }

    public void setBc(String bc) {
        this.bc = bc;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BarcodeItem{" +
                "no='" + no + '\'' +
                ", kind='" + kind + '\'' +
                ", create='" + create + '\'' +
                ", KG=" + KG +
                ", kg=" + kg +
                ", bc='" + bc + '\'' +
                ", pro='" + pro + '\'' +
                '}';
    }
}

