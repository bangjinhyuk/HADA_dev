package com.hada.fridgeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

public class AlimPopup extends Activity {

    CrystalRangeSeekbar range_temp,range_humi;
    TextView temperature_popup, humidity_popup;
    private int temp_minValue,temp_maxValue,humi_minValue,humi_maxValue;
    private String TAG= "AlimPopup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alim_popup);

        range_temp = findViewById(R.id.range_temp);
        range_humi = findViewById(R.id.range_humi);
        temperature_popup = findViewById(R.id.temperature_popup);
        humidity_popup = findViewById(R.id.humidity_popup);

        Intent intent = getIntent();
//        Log.d(TAG, "onCreate: temp_minValue "+intent.getExtras().getInt("temp_minValue",0));
//        Log.d(TAG, "onCreate: temp_maxValue "+intent.getExtras().getInt("temp_maxValue",0));
//        Log.d(TAG, "onCreate: humi_minValue "+intent.getExtras().getInt("humi_minValue",0));
//        Log.d(TAG, "onCreate: humi_maxValue "+intent.getExtras().getInt("humi_maxValue",0));

        range_temp.setMinStartValue(intent.getExtras().getInt("temp_minValue",0))
                .setMaxStartValue(intent.getExtras().getInt("temp_maxValue",0))
                .apply();
        range_humi.setMinStartValue(intent.getExtras().getInt("humi_minValue",0))
                .setMaxStartValue(intent.getExtras().getInt("humi_maxValue",0))
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
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d("dispatchTouchEvent", "dispatchTouchEvent: ");
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class)
                .putExtra("temp_minValue", temp_minValue)
                .putExtra("temp_maxValue", temp_maxValue)
                .putExtra("humi_minValue", humi_minValue)
                .putExtra("humi_maxValue", humi_maxValue);
        setResult(RESULT_OK, intent);

        return super.dispatchTouchEvent(ev);
    }

}