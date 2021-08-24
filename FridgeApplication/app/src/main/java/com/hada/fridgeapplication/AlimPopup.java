package com.hada.fridgeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlimPopup extends Activity {

    private CrystalRangeSeekbar range_temp,range_humi;
    private TextView temperature_popup, humidity_popup;
    private ImageView alim_cancel,alim_set;
    private int temp_minValue,temp_maxValue,humi_minValue,humi_maxValue;
    private String TAG= "AlimPopup";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alim_popup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FirebaseApp.initializeApp(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference("school1");

        range_temp = findViewById(R.id.range_temp);
        range_humi = findViewById(R.id.range_humi);
        temperature_popup = findViewById(R.id.temperature_popup);
        humidity_popup = findViewById(R.id.humidity_popup);
        alim_cancel = findViewById(R.id.alim_cancel);
        alim_set = findViewById(R.id.alim_set);

        Intent getintent = getIntent();

        range_temp.setMinStartValue(getintent.getExtras().getInt("temp_minValue",0))
                .setMaxStartValue(getintent.getExtras().getInt("temp_maxValue",0))
                .apply();
        range_humi.setMinStartValue(getintent.getExtras().getInt("humi_minValue",0))
                .setMaxStartValue(getintent.getExtras().getInt("humi_maxValue",0))
                .apply();


        range_temp.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                temp_minValue = Integer.parseInt(String.valueOf(minValue));
                temp_maxValue = Integer.parseInt(String.valueOf(maxValue));;
                temperature_popup.setText(minValue+"℃ ~ "+maxValue+"℃");
            }
        });

        range_humi.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                humi_minValue = Integer.parseInt(String.valueOf(minValue));
                humi_maxValue = Integer.parseInt(String.valueOf(maxValue));;
                humidity_popup.setText(minValue+"% ~ "+maxValue+"%");
            }
        });

        alim_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(getintent
                                .getExtras()
                                .getString("id"))
                        .child("tempRange")
                        .setValue(temp_minValue+"~"+temp_maxValue);
                mDatabase.child(getintent
                                .getExtras()
                                .getString("id"))
                        .child("humiRange")
                        .setValue(humi_minValue+"~"+humi_maxValue);

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class)
                        .putExtra("temp_minValue", temp_minValue)
                        .putExtra("temp_maxValue", temp_maxValue)
                        .putExtra("humi_minValue", humi_minValue)
                        .putExtra("humi_maxValue", humi_maxValue);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        alim_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}