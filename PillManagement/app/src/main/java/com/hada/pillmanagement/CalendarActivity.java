package com.hada.pillmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private ImageView plus, minus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendar_view);
        plus = findViewById(R.id.bt_plus);
        minus = findViewById(R.id.bt_minus);



        OneDayDecorator oneDayDecorator = new OneDayDecorator();

        calendarView.addDecorators(oneDayDecorator);

        calendarView.setDateSelected(CalendarDay.today(),true);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                widget.addDecorators();
            }
        });
    }
}