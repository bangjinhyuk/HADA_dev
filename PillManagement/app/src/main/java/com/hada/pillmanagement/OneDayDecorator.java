package com.hada.pillmanagement;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class OneDayDecorator implements DayViewDecorator {
    private CalendarDay date;
    private final int[] colors;
    private final HashSet<CalendarDay> dates;


    public OneDayDecorator(Collection<CalendarDay> dates, int[] colors){
        date = CalendarDay.today();
        this.dates = new HashSet<>(dates);

        this.colors = colors;
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan((new CustmMultipleDotSpan(8, colors)));
        view.addSpan(new RelativeSizeSpan(1.4f));

    }

    public void setDate(Date date){
        this.date = CalendarDay.from(date);
    }
}
