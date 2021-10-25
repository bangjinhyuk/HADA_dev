package com.example.myapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class TodayDecorator implements DayViewDecorator {
    private Context context ;
    private Date date ;

    public TodayDecorator(Context context, Date date) {
        this.context = context;
        this.date = date;
    }



    @Override
    public boolean shouldDecorate(CalendarDay day) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        StringTokenizer st1 = new StringTokenizer(dateFormat.format(date),"-");
        StringTokenizer st2 = new StringTokenizer(dateFormat.format(day.getDate()),"-");
        return st1.nextToken().equals(st2.nextToken())&&
                st1.nextToken().equals(st2.nextToken())&&
                st1.nextToken().equals(st2.nextToken());
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#0ba3de")));
    }
}