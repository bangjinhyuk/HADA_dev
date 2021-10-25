package com.example.myapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class ShowGraph extends AppCompatActivity {
    private LineChart lineChart;
    private Button detail_calender;
    private ActivityResultLauncher<Intent> calendar_resultLauncher;
    private Date date;
    private Long now;
    private SimpleDateFormat dateFormat;
    private String TAG = "ShowGraph";
    private TextView detail_date;
    private GraphService graphService;

    Database database;
    SQLiteDatabase db;

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);
        now = System.currentTimeMillis();
        date = new Date(now);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //오늘 날짜 받아서 저장
        Log.d(TAG, "onCreate: currentTimeMillis"+now);
        //TODO 그래프 추가
        lineChart = findViewById(R.id.chart);
        detail_calender = findViewById(R.id.detail_calender);
        detail_date = findViewById(R.id.detail_date);
        graphService = new GraphService(lineChart);
        lineChart.getLegend().setEnabled(false);

        database = new Database(ShowGraph.this, "myapp.db", null, 1);
        db = database.getWritableDatabase();
        database.onCreate(db);

        drawGraph(date);

        //Todo: calender view
        detail_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),CalendarPopup.class)
                        .putExtra("date",date.getTime());
                calendar_resultLauncher.launch(intent);

            }
        });

        //Todo: get databases -date entity
        calendar_resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: calendar_resultLauncher");
                        if(result.getResultCode() == RESULT_OK){
                            setDate(new Date(result.getData().getExtras().getLong("date")));
                            Log.d(TAG, "onActivityResult: getResult" + new Date(getNow()));
                            StringTokenizer st1 = new StringTokenizer(dateFormat.format(date),"-");
                            StringTokenizer st2 = new StringTokenizer(dateFormat.format(new Date(now)),"-");
                            if (st1.nextToken().equals(st2.nextToken())&&
                                    st1.nextToken().equals(st2.nextToken())&&
                                    st1.nextToken().equals(st2.nextToken())) detail_date.setText("• 실시간 그래프");
                            else detail_date.setText("• "+dateFormat.format(date));
                        }else Log.e(TAG,"onActivityResult: calendar_resultLauncher cancel");
                        drawGraph(getDate());
                    }
                }
        );

    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void drawGraph(Date date) {
        List<Entry> entries = new ArrayList<>();

        Cursor c = db.query("mytable",null,null,null,null,null,null,null);
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex("date")).equals(dateFormat.format(date))){
                entries.add(new Entry(c.getInt(c.getColumnIndex("hour")),c.getInt(c.getColumnIndex("detections"))));
            }
        }
        entries.sort((Entry e1,Entry e2) -> (int) (e1.getX()-e2.getX()));
        System.out.println("--s---");
        System.out.println(entries.size());
        entries.forEach(System.out::println);
        System.out.println("--s0---");

        graphService.drawGraph(entries, Color.rgb(153, 153, 255));

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }
}