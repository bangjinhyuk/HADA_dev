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

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView detail_title;
    private LineChart lineChart;
    private ImageView detail_tempbt, detail_humibt, detail_alim, detail_battery, detail_modify_title, detail_back;
    private boolean detailbt = true;
    private String TAG = "DetailActivity";
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detail_title = findViewById(R.id.detail_title);
        detail_tempbt = findViewById(R.id.detail_tempbt);
        detail_humibt = findViewById(R.id.detail_humibt);
        detail_alim = findViewById(R.id.detail_alim);
        detail_battery = findViewById(R.id.detail_battery);
        detail_modify_title = findViewById(R.id.detail_modify_title);
        detail_back = findViewById(R.id.detail_back);

        Intent getintent = getIntent();
        detail_title.setText(getintent.getExtras().getString("title"));

        lineChart = (LineChart)findViewById(R.id.chart);

        GraphService graphService = new GraphService(lineChart);

        lineChart.getLegend().setEnabled(false);

        //Todo: 뒤로가기 버튼 이벤트 처리
        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SensorListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Todo: 냉장고 이름 수정 버튼 처리
        detail_modify_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ModifyPopup.class);
                startActivity(intent);
            }
        });


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: resultLauncher");
                        if(result.getResultCode() == RESULT_OK){
                            Log.d(TAG, "onActivityResult: resultLauncher ok");
                            //Todo: 받아온 값을 서버에 등록
                            Log.d(TAG, "onActivityResult: temp_minValue "+result.getData().getExtras().getInt("temp_minValue",0));
                            Log.d(TAG, "onActivityResult: temp_maxValue "+result.getData().getExtras().getInt("temp_maxValue",0));
                            Log.d(TAG, "onActivityResult: humi_minValue "+result.getData().getExtras().getInt("humi_minValue",0));
                            Log.d(TAG, "onActivityResult: humi_maxValue "+result.getData().getExtras().getInt("humi_maxValue",0));
                        }
                    }
                });

        //Todo: 알림 설정 버튼 처리
        detail_alim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlimPopup.class);
                intent.putExtra("temp_minValue", -12);
                intent.putExtra("temp_maxValue", 12);
                intent.putExtra("humi_minValue", 12);
                intent.putExtra("humi_maxValue", 24);
                resultLauncher.launch(intent);
            }
        });

        //Todo: 배터리 잔량 파악
        int batteryLevel = getBatteryState(getApplicationContext());
        Log.d(TAG, "onCreate: battery level = "+batteryLevel);
        if (batteryLevel>80) detail_battery.setImageResource(R.drawable.battery_high);
        else if (batteryLevel>40) detail_battery.setImageResource(R.drawable.battery_normal);
        else if (batteryLevel>10) detail_battery.setImageResource(R.drawable.battery_low);
        else detail_battery.setImageResource(R.drawable.battery_empty);

        //Todo: 서버에서 일일 측정 온도 받아오는 부분
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





    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: ");
//        if(requestCode==0){
//            if (resultCode==RESULT_OK) {
//                Toast.makeText(DetailActivity.this, "result ok!", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(DetailActivity.this, "result cancle!", Toast.LENGTH_SHORT).show();
//            }
//        }else if(requestCode==1){
//        }
//    }


    public int getBatteryState(Context context) {
        Intent batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;
        return (int)(batteryPct * 100);
    }
}