package com.example.myapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GraphService {

    LineChart lineChart;


    public GraphService(LineChart lineChart) {
        this.lineChart = lineChart;
    }


    public void drawGraph(List<Entry> entries, int color){
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
//        lineDataSet.setLineWidth(2);
//        lineDataSet.setCircleRadius(6);
//        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
//        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
//        lineDataSet.setDrawCircleHole(false);
//        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setDrawValues(true);
        lineDataSet.setCubicIntensity(0.3f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setLineWidth(0.6f);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleColor(Color.rgb(112,92,147));
//        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setColor(color);
        lineDataSet.setFillColor(color);
        lineDataSet.setFillAlpha(100);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawVerticalHighlightIndicator(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 10, 0);
        xAxis.setGranularity(1f);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(24f);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawLabels(true);
        yLAxis.setAxisMinimum(0);
        yLAxis.setDrawAxisLine(false);
        yLAxis.setDrawGridLines(false);
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        Description description = new Description();
        description.setText("");

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }



}
