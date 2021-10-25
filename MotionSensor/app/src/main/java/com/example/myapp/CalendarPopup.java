package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Window;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarPopup extends Activity {

    private com.prolificinteractive.materialcalendarview.MaterialCalendarView calendar_view;
    private String TAG = "CalendarPopup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar_popup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        calendar_view = findViewById(R.id.calendar_view);
        Intent intent= getIntent();
        Date date = new Date(intent.getExtras().getLong("date"));

        calendar_view.setCurrentDate(date);
        TodayDecorator todayDecorator = new TodayDecorator(getApplicationContext(),date);
        calendar_view.addDecorator(todayDecorator);
        calendar_view.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = new Intent(widget.getContext(), ShowGraph.class)
                        .putExtra("date",date.getDate().getTime());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
