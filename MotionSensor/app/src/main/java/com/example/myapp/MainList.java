package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class MainList extends AppCompatActivity {

    private TextView tv_time;
    private Button timesettingB, showGraphB, contactB;
    private Switch switch1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        tv_time = findViewById(R.id.tv_time);
        timesettingB = findViewById(R.id.timesettingB);
        showGraphB = findViewById(R.id.showGraphB);
        contactB = findViewById(R.id.contactB);
        switch1 = findViewById(R.id.switch1);

        timesettingB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TimeSetting.class);
                startActivity(intent);
            }
        });
        showGraphB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ShowGraph.class);
                startActivity(intent);
            }
        });
        contactB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ContactInformation.class);
                startActivity(intent);
            }
        });



    }
}