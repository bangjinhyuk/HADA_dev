package com.hada.fridgeapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class DetailActivity extends AppCompatActivity {
    private TextView detail_title, detail_date,detail_hum,detail_tem,bluetooth;
    private LineChart lineChart;
    private ImageView detail_tempbt, detail_humibt, detail_alim, detail_battery_1, detail_battery_2, detail_modify_title, detail_back, detail_calender;
    private boolean detailbt = true;
    private String TAG = "DetailActivity";
    private ActivityResultLauncher<Intent> alim_resultLauncher,modify_resultLauncher, calendar_resultLauncher;
    private Long now;
    private Date date;
    private DateFormat dateFormat;
    private DatabaseReference mDatabase;
    private String sensorID;
    private int temp_minValue = -30,temp_maxValue =30,humi_minValue= 0,humi_maxValue = 100;
    private List<Entry> entries;
    private ArrayList sortEntryList;
    private ValueEventListener valueEventListener;




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
        detail_hum = findViewById(R.id.detail_hum);
        detail_tem = findViewById(R.id.detail_tem);
        bluetooth = findViewById(R.id.bluetooth);

        lineChart = (LineChart)findViewById(R.id.chart);

        GraphService graphService = new GraphService(lineChart);

        lineChart.getLegend().setEnabled(false);

        FirebaseApp.initializeApp(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference("school1");


        now = System.currentTimeMillis();
        date = new Date(now);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String df = dateFormat.format(date);
        //오늘 날짜 받아서 저장
        Log.d(TAG, "onCreate: currentTimeMillis"+now);

        //파이어베이스에서 값 가져오기
        Intent getintent = getIntent();
        sensorID = getintent.getExtras().getString("id");
        mDatabase.addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detail_title.setText(String.valueOf(snapshot.child(sensorID).child("sensorName").getValue()));
                detail_hum.setText(String.valueOf(snapshot.child(sensorID).child("humi").getValue()+"%"));
                detail_tem.setText(String.valueOf(snapshot.child(sensorID).child("temp").getValue()+"℃"));


                //Todo: get database -battery 1

                if(Integer.parseInt(snapshot.child(sensorID).child("bat1").getValue()+"")>80) detail_battery_1.setImageResource(R.drawable.battery_high);
                else if(Integer.parseInt(snapshot.child(sensorID).child("bat1").getValue()+"")>40) detail_battery_1.setImageResource(R.drawable.battery_normal);
                else if(Integer.parseInt(snapshot.child(sensorID).child("bat1").getValue()+"")>10) detail_battery_1.setImageResource(R.drawable.battery_low);
                else detail_battery_1.setImageResource(R.drawable.battery_empty);

                //Todo: get database -battery 1
                if(Integer.parseInt(snapshot.child(sensorID).child("bat2").getValue()+"")>80) detail_battery_2.setImageResource(R.drawable.battery_high);
                else if(Integer.parseInt(snapshot.child(sensorID).child("bat2").getValue()+"")>40) detail_battery_2.setImageResource(R.drawable.battery_normal);
                else if(Integer.parseInt(snapshot.child(sensorID).child("bat2").getValue()+"")>10) detail_battery_2.setImageResource(R.drawable.battery_low);
                else detail_battery_2.setImageResource(R.drawable.battery_empty);

                if(snapshot.child(sensorID).child("tempRange").getValue()!=null&&snapshot.child(sensorID).child("humiRange").getValue()!=null) {
                    StringTokenizer tr = new StringTokenizer(snapshot.child(sensorID).child("tempRange").getValue() + "", "~");
                    StringTokenizer hr = new StringTokenizer(snapshot.child(sensorID).child("humiRange").getValue() + "", "~");
                    temp_minValue = Integer.parseInt(tr.nextToken());
                    temp_maxValue = Integer.parseInt(tr.nextToken());
                    humi_minValue = Integer.parseInt(hr.nextToken());
                    humi_maxValue = Integer.parseInt(hr.nextToken());
                }


                StringTokenizer nowst = new StringTokenizer(dateFormat.format(date),"-");
                String tmp;
                String year = nowst.nextToken();
                String month = (tmp = nowst.nextToken()).startsWith("0")? tmp.replaceFirst("0","") : tmp;
                String day = (tmp = nowst.nextToken()).startsWith("0")? tmp.replaceFirst("0","") : tmp;
                String set;
                int rgb;
                if (snapshot.child(sensorID).child("tempset").child(month+'-'+day+'-'+year).getValue()!=null&&
                        snapshot.child(sensorID).child("humiset").child(month+'-'+day+'-'+year).getValue()!=null) {
                    if (detailbt) {
                        set = String.valueOf(snapshot.child(sensorID).child("tempset").child(month + '-' + day + '-' + year).getValue());
                        detail_tempbt.setImageResource(R.drawable.click_temp_tab);
                        detail_humibt.setImageResource(R.drawable.humi_tab);
                        rgb = Color.rgb(153, 153, 255);
                    } else {
                        set = String.valueOf(snapshot.child(sensorID).child("humiset").child(month + '-' + day + '-' + year).getValue());
                        detail_tempbt.setImageResource(R.drawable.temp_tab);
                        detail_humibt.setImageResource(R.drawable.click_humi_tab);
                        rgb = Color.rgb(162, 225, 199);
                    }
                    set = set.replaceAll("\\{", "").replaceAll("\\}", "");


                    entries = new ArrayList<>();
                    sortEntryList = new ArrayList();

                    StringTokenizer setk = new StringTokenizer(set, ","); //데이터 하나씩 분류
                    float hour;
                    float min;
                    float sec;
                    String apm;
                    int setkNum = setk.countTokens();
                    for (int i = 0; i < setkNum; i++) {
                        StringTokenizer datatk = new StringTokenizer(setk.nextToken(), "=");
                        StringTokenizer timetk = new StringTokenizer(datatk.nextToken(), ":");
                        hour = Float.parseFloat(timetk.nextToken());
                        min = Float.parseFloat(timetk.nextToken());
                        StringTokenizer sectk = new StringTokenizer(timetk.nextToken(), " ");
                        sec = Float.parseFloat(sectk.nextToken());
                        apm = sectk.nextToken();
                        if (apm.equals("PM")) hour += 12;
                        //Todo: 시분초로 0~24 사잇값으로 만들어 datatk.nextToken값과 함께 entry 저장
                        sortEntryList.add(new SortEntry(hour + (min / 60f) + (sec / 3600f), Float.parseFloat(datatk.nextToken())));
                    }
                    Collections.sort(sortEntryList);
                    for (Object sortEntry : sortEntryList) {
                        System.out.println(sortEntry);
                        entries.add(new Entry(((SortEntry) sortEntry).getTime(), ((SortEntry) sortEntry).getData()));
                    }


                    graphService.drawGraph(entries, rgb);
                }else graphService.drawGraph(new ArrayList<>(),Color.rgb(0, 0, 0));

                if (snapshot.child(sensorID).child("connect").getValue().equals(0)){
                    bluetooth.setVisibility(View.VISIBLE);
                }else {
                    bluetooth.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
                Intent intent = new Intent(v.getContext(),ModifyPopup.class).putExtra("id",sensorID);
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
//                            detail_title.setText(result.getData().getExtras().getString("modify_name"));
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
                intent.putExtra("id",sensorID);
                intent.putExtra("temp_minValue", temp_minValue);
                intent.putExtra("temp_maxValue", temp_maxValue);
                intent.putExtra("humi_minValue", humi_minValue);
                intent.putExtra("humi_maxValue", humi_maxValue);
                alim_resultLauncher.launch(intent);
            }
        });


        /*
         * 온도 측정 기록 탭 클릭시
         */
        detail_tempbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!detailbt) {
                    mDatabase.addListenerForSingleValueEvent(valueEventListener);
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
                    mDatabase.addListenerForSingleValueEvent(valueEventListener);
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
                            mDatabase.addListenerForSingleValueEvent(valueEventListener);
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