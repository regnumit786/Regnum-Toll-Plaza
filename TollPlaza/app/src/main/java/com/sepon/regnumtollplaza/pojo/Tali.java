package com.sepon.regnumtollplaza.pojo;

public class Tali {

    String taliName;
    String taliCount;
    String taliCountedTaka;
    int image;
    String  rate;



    public Tali( String taliName, String taliCount, String taliCountedTaka, int image, String rate) {

        this.taliName = taliName;
        this.taliCount = taliCount;
        this.taliCountedTaka = taliCountedTaka;
        this.image = image;
        this.rate = rate;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTaliName() {
        return taliName;
    }

    public void setTaliName(String taliName) {
        this.taliName = taliName;
    }

    public String getTaliCount() {
        return taliCount;
    }

    public void setTaliCount(String taliCount) {
        this.taliCount = taliCount;
    }

    public String getTaliCountedTaka() {
        return taliCountedTaka;
    }

    public void setTaliCountedTaka(String taliCountedTaka) {
        this.taliCountedTaka = taliCountedTaka;
    }
}
