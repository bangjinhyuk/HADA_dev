package com.hada.fridgeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView detail_title;
    private LineChart lineChart;
    private ImageView detail_tempbt, detail_humibt, detail_alim;
    private boolean detailbt = true;
    private String TAG = "DetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detail_title = findViewById(R.id.detail_title);
        detail_tempbt = findViewById(R.id.detail_tempbt);
        detail_humibt = findViewById(R.id.detail_humibt);
        detail_alim = findViewById(R.id.detail_alim);

        Intent getintent = getIntent();
        detail_title.setText(getintent.getExtras().getString("title"));

        lineChart = (LineChart)findViewById(R.id.chart);

        GraphService graphService = new GraphService(lineChart);

        lineChart.getLegend().setEnabled(false);

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 1));
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 4));
        entries.add(new Entry(5, 3));
        entries.add(new Entry(6, 3));
        entries.add(new Entry(7, 3));
        entries.add(new Entry(8, 3));
        entries.add(new Entry(9, 3));
        entries.add(new Entry(10, 3));
        entries.add(new Entry(11, 3));
        entries.add(new Entry(12, 3));
        entries.add(new Entry(13, 3));
        entries.add(new Entry(14, 3));
        entries.add(new Entry(15, 3));
        entries.add(new Entry(16, 2));
        entries.add(new Entry(17, 1));
        entries.add(new Entry(18, 0));
        entries.add(new Entry(19, 3));
        entries.add(new Entry(20, 4));
        entries.add(new Entry(21, 5));
        entries.add(new Entry(22, 6));
        entries.add(new Entry(23, 7));

        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(0, 23));
        entries1.add(new Entry(1, 23));
        entries1.add(new Entry(2, 25));
        entries1.add(new Entry(3, 06));
        entries1.add(new Entry(4, 46));
        entries1.add(new Entry(5, 37));
        entries1.add(new Entry(6, 34));
        entries1.add(new Entry(7, 32));
        entries1.add(new Entry(8, 31));
        entries1.add(new Entry(9, 33));
        entries1.add(new Entry(10, 36));
        entries1.add(new Entry(11, 39));
        entries1.add(new Entry(12, 36));
        entries1.add(new Entry(13, 35));
        entries1.add(new Entry(14, 34));
        entries1.add(new Entry(15, 33));
        entries1.add(new Entry(16, 32));
        entries1.add(new Entry(17, 31));
        entries1.add(new Entry(18, 33));
        entries1.add(new Entry(19, 34));
        entries1.add(new Entry(20, 35));
        entries1.add(new Entry(21, 37));
        entries1.add(new Entry(22, 38));
        entries1.add(new Entry(23, 37));

        if (detailbt) {
            graphService.drawGraph(entries,Color.rgb(153, 153, 255));
            detail_tempbt.setImageResource(R.drawable.click_temp_tab);
            detail_humibt.setImageResource(R.drawable.humi_tab);
        }else{
//            graphService.drawGraph(entries1,Color.rgb(162, 225, 199),lineChart);
            detail_tempbt.setImageResource(R.drawable.temp_tab);
            detail_humibt.setImageResource(R.drawable.click_humi_tab);
        }

        /*
         * 온도 측정 기록 탭 클릭시
         */
        detail_tempbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!detailbt) {
                    graphService.drawGraph(entries,Color.rgb(153, 153, 255));
                    detail_tempbt.setImageResource(R.drawable.click_temp_tab);
                    detail_humibt.setImageResource(R.drawable.humi_tab);
                    detailbt = true;

                }
            }
        });
        /*
         * 습도 측정 기록 탭 클릭시
         */
        detail_humibt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailbt) {
                    graphService.drawGraph(entries1,Color.rgb(162, 225, 199));
                    detail_tempbt.setImageResource(R.drawable.temp_tab);
                    detail_humibt.setImageResource(R.drawable.click_humi_tab);
                    detailbt = false;
                }
            }
        });

        detail_alim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlimPopup.class);
                intent.putExtra("data", "Test Popup");
                v.getContext().startActivity(intent);
            }
        });




    }
}