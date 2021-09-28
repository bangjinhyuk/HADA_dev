package com.hada.pillmanagement;

import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class plusPopup extends Activity {

    private Button monday,tuesday,wednesday,thursday,friday,saturday,sunday,ampm;
    private EditText hour, min;
    private boolean [] click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plus_popup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        ampm = findViewById(R.id.ampm);
        hour = findViewById(R.id.hour);
        min = findViewById(R.id.min);

        click = new boolean[8];

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[0]){
                    setTextColor(monday,0);
                    monday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[0] = false;
                }else{
                    setTextColor(monday,1);
                    monday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[0] = true;
                }
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[1]){
                    setTextColor(tuesday,0);
                    tuesday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[1] = false;
                }else{
                    setTextColor(tuesday,1);
                    tuesday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[1] = true;
                }
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[2]){
                    setTextColor(wednesday,0);
                    wednesday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[2] = false;
                }else{
                    setTextColor(wednesday,1);
                    wednesday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[2] = true;
                }
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[3]){
                    setTextColor(thursday,0);
                    thursday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[3] = false;
                }else{
                    setTextColor(thursday,1);
                    thursday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[3] = true;
                }
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[4]){
                    setTextColor(friday,0);
                    friday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[4] = false;
                }else{
                    setTextColor(friday,1);
                    friday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[4] = true;
                }
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[5]){
                    setTextColor(saturday,0);
                    saturday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[5] = false;
                }else{
                    setTextColor(saturday,1);
                    saturday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[5] = true;
                }
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[6]){
                    setTextColor(sunday,0);
                    sunday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[6] = false;
                }else{
                    setTextColor(sunday,1);
                    sunday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[6] = true;
                }
            }
        });

        ampm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[7]){
                    ampm.setText("오후");
                    click[7] = false;
                }else{
                    ampm.setText("오전");
                    click[7] = true;
                }
            }
        });

    }
    public void setTextColor(Button button, int i){
        if(i==0){
            button.setTextColor(Color.BLACK);
        }else{
            button.setTextColor(Color.WHITE);
        }
    }
}