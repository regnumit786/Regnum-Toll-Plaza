package com.sepon.regnumtollplaza.pojo;

public class Previous_pojo {

    String date;
    String ctrl;
    String regular;
    String total;



    public Previous_pojo(String date, String ctrl, String regular, String total) {
        this.date = date;
        this.ctrl = ctrl;
        this.regular = regular;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCtrl() {
        return ctrl;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
