package com.hada.fridgeapplication;

public class SensorModel {
    private int id;
    private String sensorName;
    private double temp;
    private double humi;
    private int bat1;
    private int bat2;
    private String tempRange;
    private String humiRange;


    public SensorModel() {
    }

    public SensorModel(int id, String sensorName, double temp, double humi, int bat1, int bat2, String tempRange, String humiRange) {
        this.id = id;
        this.sensorName = sensorName;
        this.temp = temp;
        this.humi = humi;
        this.bat1 = bat1;
        this.bat2 = bat2;
        this.tempRange = tempRange;
        this.humiRange = humiRange;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumi() {
        return humi;
    }

    public void setHumi(double humi) {
        this.humi = humi;
    }

    public String getTempRange() {
        return tempRange;
    }

    public void setTempRange(String tempRange) {
        this.tempRange = tempRange;
    }

    public String getHumiRange() {
        return humiRange;
    }

    public void setHumiRange(String humiRange) {
        this.humiRange = humiRange;
    }

    public int getBat1() {
        return bat1;
    }

    public void setBat1(int bat1) {
        this.bat1 = bat1;
    }

    public int getBat2() {
        return bat2;
    }

    public void setBat2(int bat2) {
        this.bat2 = bat2;
    }



    @Override
    public String toString() {
        return "SensorModel{" +
                "id=" + id +
                ", sensorName='" + sensorName + '\'' +
                ", temp=" + temp +
                ", humi=" + humi +
                ", bat1=" + bat1 +
                ", bat2=" + bat2 +
                ", tempRange='" + tempRange + '\'' +
                ", humiRange='" + humiRange + '\'' +
                '}';
    }
}
