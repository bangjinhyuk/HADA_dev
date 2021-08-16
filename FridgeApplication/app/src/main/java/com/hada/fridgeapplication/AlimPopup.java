package com.hada.fridgeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

public class AlimPopup extends Activity {

    CrystalRangeSeekbar range_temp,range_humi;
    TextView temperature_popup, humidity_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alim_popup);

        range_temp = findViewById(R.id.range_temp);
        range_humi = findViewById(R.id.range_humi);
        temperature_popup = findViewById(R.id.temperature_popup);
        humidity_popup = findViewById(R.id.humidity_popup);

        range_temp.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                temperature_popup.setText(minValue+"℃ ~ "+maxValue+"℃");
            }
        });

        range_humi.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                humidity_popup.setText(minValue+"% ~ "+maxValue+"%");
            }
        });
    }
}