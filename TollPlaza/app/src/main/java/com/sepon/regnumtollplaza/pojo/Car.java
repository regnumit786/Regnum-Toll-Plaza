package com.sepon.regnumtollplaza.pojo;

public class Car {
    String vichelClass;
    String price;
    String date_time;
    String passID;


    public Car(String vichelClass, String price, String date_time, String passID) {
        this.vichelClass = vichelClass;
        this.price = price;
        this.date_time = date_time;
        this.passID = passID;
    }

    public String getVichelClass() {
        return vichelClass;
    }

    public void setVichelClass(String vichelClass) {
        this.vichelClass = vichelClass;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPassID() {
        return passID;
    }

    public void setPassID(String passID) {
        this.passID = passID;
    }
}
