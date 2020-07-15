package com.sepon.regnumtollplaza.admin;

public class Report {

    private String transactionNumber;
    private String lane;
    private String dateTime;
    private String vehicleNumber;
    private String tcClass;
    private String avcClass;
    private String paymentMethod;
    private String fare;
    private String totalAxles;
    private String axleWiseWeight;
    private String totalWeight;

    public Report(){}

    public Report(String transactionNumber, String lane, String dateTime, String vehicleNumber, String tcClass,
                  String avcClass, String paymentMethod, String fare, String totalAxles, String axleWiseWeight, String totalWeight) {
        this.transactionNumber = transactionNumber;
        this.lane = lane;
        this.dateTime = dateTime;
        this.vehicleNumber = vehicleNumber;
        this.tcClass = tcClass;
        this.avcClass = avcClass;
        this.paymentMethod = paymentMethod;
        this.fare = fare;
        this.totalAxles = totalAxles;
        this.axleWiseWeight = axleWiseWeight;
        this.totalWeight = totalWeight;
    }


    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getTcClass() {
        return tcClass;
    }

    public void setTcClass(String tcClass) {
        this.tcClass = tcClass;
    }

    public String getAvcClass() {
        return avcClass;
    }

    public void setAvcClass(String avcClass) {
        this.avcClass = avcClass;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getTotalAxles() {
        return totalAxles;
    }

    public void setTotalAxles(String totalAxles) {
        this.totalAxles = totalAxles;
    }

    public String getAxleWiseWeight() {
        return axleWiseWeight;
    }

    public void setAxleWiseWeight(String axleWiseWeight) {
        this.axleWiseWeight = axleWiseWeight;
    }

    public String getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(String totalWeight) {
        this.totalWeight = totalWeight;
    }
}
