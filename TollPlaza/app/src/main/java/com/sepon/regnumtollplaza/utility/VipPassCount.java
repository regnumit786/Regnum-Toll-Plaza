
package com.sepon.regnumtollplaza.utility;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VipPassCount {

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("pass_id")
    @Expose
    private String passId;
    @SerializedName("RegNO")
    @Expose
    private String regNO;
    @SerializedName("Rickshaw_Van")
    @Expose
    private String rickshawVan;
    @SerializedName("MotorCycle")
    @Expose
    private String motorCycle;
    @SerializedName("three_four_Wheeler")
    @Expose
    private String threeFourWheeler;
    @SerializedName("Sedan_Car")
    @Expose
    private String sedanCar;
    @SerializedName("4Wheeler")
    @Expose
    private String _4Wheeler;
    @SerializedName("Micro_Bus")
    @Expose
    private String microBus;
    @SerializedName("Mini_Bus")
    @Expose
    private String miniBus;
    @SerializedName("Agro_Use")
    @Expose
    private String agroUse;
    @SerializedName("Mini_Truck")
    @Expose
    private String miniTruck;
    @SerializedName("Big_Bus")
    @Expose
    private String bigBus;
    @SerializedName("Medium_Truck")
    @Expose
    private String mediumTruck;
    @SerializedName("Heavy_Truck")
    @Expose
    private String heavyTruck;
    @SerializedName("Trailer_Long")
    @Expose
    private String trailerLong;
    @SerializedName("date_time")
    @Expose
    private String dateTime;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPassId() {
        return passId;
    }

    public void setPassId(String passId) {
        this.passId = passId;
    }

    public String getRegNO() {
        return regNO;
    }

    public void setRegNO(String regNO) {
        this.regNO = regNO;
    }

    public String getRickshawVan() {
        return rickshawVan;
    }

    public void setRickshawVan(String rickshawVan) {
        this.rickshawVan = rickshawVan;
    }

    public String getMotorCycle() {
        return motorCycle;
    }

    public void setMotorCycle(String motorCycle) {
        this.motorCycle = motorCycle;
    }

    public String getThreeFourWheeler() {
        return threeFourWheeler;
    }

    public void setThreeFourWheeler(String threeFourWheeler) {
        this.threeFourWheeler = threeFourWheeler;
    }

    public String getSedanCar() {
        return sedanCar;
    }

    public void setSedanCar(String sedanCar) {
        this.sedanCar = sedanCar;
    }

    public String get4Wheeler() {
        return _4Wheeler;
    }

    public void set4Wheeler(String _4Wheeler) {
        this._4Wheeler = _4Wheeler;
    }

    public String getMicroBus() {
        return microBus;
    }

    public void setMicroBus(String microBus) {
        this.microBus = microBus;
    }

    public String getMiniBus() {
        return miniBus;
    }

    public void setMiniBus(String miniBus) {
        this.miniBus = miniBus;
    }

    public String getAgroUse() {
        return agroUse;
    }

    public void setAgroUse(String agroUse) {
        this.agroUse = agroUse;
    }

    public String getMiniTruck() {
        return miniTruck;
    }

    public void setMiniTruck(String miniTruck) {
        this.miniTruck = miniTruck;
    }

    public String getBigBus() {
        return bigBus;
    }

    public void setBigBus(String bigBus) {
        this.bigBus = bigBus;
    }

    public String getMediumTruck() {
        return mediumTruck;
    }

    public void setMediumTruck(String mediumTruck) {
        this.mediumTruck = mediumTruck;
    }

    public String getHeavyTruck() {
        return heavyTruck;
    }

    public void setHeavyTruck(String heavyTruck) {
        this.heavyTruck = heavyTruck;
    }

    public String getTrailerLong() {
        return trailerLong;
    }

    public void setTrailerLong(String trailerLong) {
        this.trailerLong = trailerLong;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
