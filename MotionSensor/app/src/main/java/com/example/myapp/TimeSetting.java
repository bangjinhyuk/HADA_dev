package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

public class TimeSetting extends AppCompatActivity {
    private TextView timepicker;
    private ImageView down_arrow,up_arrow;
    private Button BforAlarm,BforCall,save;
    private int hour,min;
    private String TAG = "TimeSetting";
    private int alarmType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);

        timepicker = findViewById(R.id.timepicker);
        down_arrow = findViewById(R.id.down_arrow);
        up_arrow = findViewById(R.id.up_arrow);
        BforAlarm = findViewById(R.id.BforAlarm);
        BforCall = findViewById(R.id.BforCall);
        save = findViewById(R.id.save);

        down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSet(0);
            }
        });

        up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSet(1);
            }
        });

        BforAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarmType!=1){
                    alarmType =1;
                    BforAlarm.setBackgroundColor(Color.rgb(94,69,138));
                    BforAlarm.setTextColor(Color.WHITE);
                    BforCall.setBackgroundColor(Color.WHITE);
                    BforCall.setTextColor(Color.BLACK);

                }

            }
        });
        BforCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarmType!=2){
                    alarmType =2;
                    BforCall.setBackgroundColor(Color.rgb(94,69,138));
                    BforCall.setTextColor(Color.WHITE);
                    BforAlarm.setBackgroundColor(Color.WHITE);
                    BforAlarm.setTextColor(Color.BLACK);

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 저장하는 부분 구현 필요
                finish();
            }
        });
    }

    private void timeSet(int type) {
        StringTokenizer st = new StringTokenizer(timepicker.getText().toString(),":");
        hour = Integer.parseInt(st.nextToken());
        min = Integer.parseInt(st.nextToken());

        if(type ==0){
            Log.d(TAG, "timeSet: minus");

            if(min ==0){
                hour-=1;
                min = 50;
            } else min-=10;
            if(hour<0){
                hour =0;
                min = 0;
            }
        }else{
            Log.d(TAG, "timeSet: plus");
            if(min ==50){
                hour+=1;
                min = 0;
            }else min+=10;
        }

        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("00");

        sb.append(df.format(hour)).append(":").append(df.format(min));
        timepicker.setText(sb);

    }
}