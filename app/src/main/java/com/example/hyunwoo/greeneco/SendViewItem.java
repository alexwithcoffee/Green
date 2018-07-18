package com.example.hyunwoo.greeneco;

/**
 * Created by hyunwoo on 2018-01-22.
 */

public class SendViewItem {

    String kind1;
    String create1;
    Double KG1;
    Double kg1;
    boolean status;


    public SendViewItem() {
        this.kind1 = kind1;
        this.create1 = create1;
        this.KG1 = KG1;
        this.kg1 = kg1;
        this.status = status;
    }

    public String getKind1() {
        return kind1;
    }

    public void setKind1(String kind1) {
        this.kind1 = kind1;
    }

    public String getCreate1() {
        return create1;
    }

    public void setCreate1(String create1) {
        this.create1 = create1;
    }

    public Double getKG1() {
        return KG1;
    }

    public void setKG1(Double KG1) {
        this.KG1 = KG1;
    }

    public Double getKg1() {
        return kg1;
    }

    public void setKg1(Double kg1) {
        this.kg1 = kg1;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
