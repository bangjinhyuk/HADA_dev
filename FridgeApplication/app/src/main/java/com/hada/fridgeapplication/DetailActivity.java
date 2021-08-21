package com.hada.fridgeapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class DetailActivity extends AppCompatActivity {
    private TextView detail_title, detail_date;
    private LineChart lineChart;
    private ImageView detail_tempbt, detail_humibt, detail_alim, detail_battery_1, detail_battery_2, detail_modify_title, detail_back, detail_calender;
    private boolean detailbt = true;
    private String TAG = "DetailActivity";
    private ActivityResultLauncher<Intent> alim_resultLauncher,modify_resultLauncher, calendar_resultLauncher;
    private Long now;
    private Date date;
    private DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detail_title = findViewById(R.id.detail_title);
        detail_tempbt = findViewById(R.id.detail_tempbt);
        detail_humibt = findViewById(R.id.detail_humibt);
        detail_alim = findViewById(R.id.detail_alim);
        detail_battery_1 = findViewById(R.id.detail_battery_1);
        detail_battery_2 = findViewById(R.id.detail_battery_2);
        detail_modify_title = findViewById(R.id.detail_modify_title);
        detail_back = findViewById(R.id.detail_back);
        detail_calender = findViewById(R.id.detail_calender);
        detail_date = findViewById(R.id.detail_date);
        now = System.currentTimeMillis();
        date = new Date(now);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String df = dateFormat.format(date);
        //오늘 날짜 받아서 저장
        Log.d(TAG, "onCreate: currentTimeMillis"+now);
//
        Intent getintent = getIntent();
        detail_title.setText(getintent.getExtras().getString("title"));

        lineChart = (LineChart)findViewById(R.id.chart);

        GraphService graphService = new GraphService(lineChart);

        lineChart.getLegend().setEnabled(false);

        //뒤로가기 버튼 이벤트
        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SensorListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Todo: sensor name change event
        detail_modify_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ModifyPopup.class);
                modify_resultLauncher.launch(intent);
            }
        });

        modify_resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: modify_resultLauncher");
                        if(result.getResultCode() == RESULT_OK){
                            Log.d(TAG, "onActivityResult: resultLauncher ok");
                            detail_title.setText(result.getData().getExtras().getString("modify_name"));
                        }else Log.e(TAG,"onActivityResult: resultLauncher cancel");
                    }
                });

        //Todo: send to database -alim set
        alim_resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: alim_resultLauncher");
                        if(result.getResultCode() == RESULT_OK){
                            Log.d(TAG, "onActivityResult: resultLauncher ok");



                            Log.d(TAG, "onActivityResult: temp_minValue "+result.getData().getExtras().getInt("temp_minValue",0));
                            Log.d(TAG, "onActivityResult: temp_maxValue "+result.getData().getExtras().getInt("temp_maxValue",0));
                            Log.d(TAG, "onActivityResult: humi_minValue "+result.getData().getExtras().getInt("humi_minValue",0));
                            Log.d(TAG, "onActivityResult: humi_maxValue "+result.getData().getExtras().getInt("humi_maxValue",0));

                        }else Log.e(TAG,"onActivityResult: resultLauncher cancel");
                    }
                });

        //Todo: get database -alim set
        detail_alim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlimPopup.class);
                intent.putExtra("temp_minValue", -12);
                intent.putExtra("temp_maxValue", 12);
                intent.putExtra("humi_minValue", 12);
                intent.putExtra("humi_maxValue", 24);
                alim_resultLauncher.launch(intent);
            }
        });

        //Todo: get database -battery 1

        //Todo: get database -battery 1



        //Todo: get database -entries
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
                    }
                }
        );


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