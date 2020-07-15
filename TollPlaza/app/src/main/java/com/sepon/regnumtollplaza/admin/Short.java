package com.sepon.regnumtollplaza.admin;

public class Short {

    String total;
    String ctrlR;
    String regular;

    public Short(){}

    public Short(String total, String ctrlR, String regular) {
        this.total = total;
        this.ctrlR = ctrlR;
        this.regular = regular;
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCtrlR() {
        return ctrlR;
    }

    public void setCtrlR(String ctrlR) {
        this.ctrlR = ctrlR;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }
}
