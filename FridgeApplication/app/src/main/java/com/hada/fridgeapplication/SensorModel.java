package com.hada.fridgeapplication;

public class SensorModel {
    private String sensorName;
    private String temperature;
    private String humidity;
    private int status;

    public SensorModel(String sensorName, String temperature, String humidity, int status) {
        this.sensorName = sensorName;
        this.temperature = temperature;
        this.humidity = humidity;
        this.status = status;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
