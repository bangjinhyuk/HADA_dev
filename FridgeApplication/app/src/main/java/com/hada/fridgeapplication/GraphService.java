package com.hada.fridgeapplication;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphService {

    LineChart lineChart;

    public GraphService(LineChart lineChart) {
        this.lineChart = lineChart;
    }

    public void drawGraph(List<Entry> entries, int color){
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
//        lineDataSet.setLineWidth(2);
//        lineDataSet.setCircleRadius(6);
//        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
//        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
//        lineDataSet.setDrawCircleHole(false);
//        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setCubicIntensity(0.3f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(0.6f);
//        lineDataSet.setCircleRadius(4f);
//        lineDataSet.setCircleColor(Color.BLUE);
//        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setColor(color);
        lineDataSet.setFillColor(color);
        lineDataSet.setFillAlpha(100);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawVerticalHighlightIndicator(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 30, 0);

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawLabels(true);
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
