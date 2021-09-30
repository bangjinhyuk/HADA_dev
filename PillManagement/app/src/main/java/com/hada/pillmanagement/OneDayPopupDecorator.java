package com.hada.pillmanagement;

import android.graphics.Typeface;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class OneDayPopupDecorator implements DayViewDecorator {
    private CalendarDay date;
    private final int[] colors;
    private HashSet<CalendarDay> dates;


    public OneDayPopupDecorator(Collection<CalendarDay> dates, int[] colors){
        date = CalendarDay.today();
        this.dates = new HashSet<>(dates);
        this.colors = colors;
    }

    public void removeHashSet() {
        this.dates = new HashSet<>();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan((new CustmMultipleDotSpan(8, colors)));

    }

    public void setDate(Date date){
        this.date = CalendarDay.from(date);
    }
}
