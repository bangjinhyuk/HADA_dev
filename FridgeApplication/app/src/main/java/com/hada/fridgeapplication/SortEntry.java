package com.hada.fridgeapplication;

public class SortEntry implements Comparable<SortEntry>{
    private float time;
    private float data;

    public SortEntry(float time, float data) {
        this.time = time;
        this.data = data;
    }

    @Override
    public int compareTo(SortEntry o) {
        return Float.compare(this.time,o.time);
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SortEntry{" +
                "time=" + time +
                ", data=" + data +
                '}';
    }
}
