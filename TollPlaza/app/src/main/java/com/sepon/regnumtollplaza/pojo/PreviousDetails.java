package com.sepon.regnumtollplaza.pojo;

public class PreviousDetails {

    String date;
    String vichelAmount;
    String dayTotalAmount;

    public PreviousDetails(){}

    public PreviousDetails(String date, String vichelAmount, String dayTotalAmount) {
        this.date = date;
        this.vichelAmount = vichelAmount;
        this.dayTotalAmount = dayTotalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVichelAmount() {
        return vichelAmount;
    }

    public void setVichelAmount(String vichelAmount) {
        this.vichelAmount = vichelAmount;
    }

    public String getDayTotalAmount() {
        return dayTotalAmount;
    }

    public void setDayTotalAmount(String dayTotalAmount) {
        this.dayTotalAmount = dayTotalAmount;
    }
}
