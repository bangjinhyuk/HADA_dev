package com.hada.pillmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class PillCalendarListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CalendarItem> items = new ArrayList<CalendarItem>();

    public PillCalendarListViewAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void clear(){
        items = new ArrayList<>();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // item.xml 레이아웃을 inflate해서 참조획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.calendar_item, parent, false);
        }

        // item.xml 의 참조 획득
        TextView txt_name = (TextView)convertView.findViewById(R.id.pill_cal_name);
        TextView txt_date = (TextView)convertView.findViewById(R.id.pill_cal_date);
        TextView txt_day = (TextView)convertView.findViewById(R.id.pill_cal_day);
        TextView txt_time = (TextView)convertView.findViewById(R.id.pill_cal_time);
        TextView case_num = (TextView)convertView.findViewById(R.id.tv_caseNum);
        ImageView image_complete = (ImageView)convertView.findViewById(R.id.btn_cal_complete);

        CalendarItem item = items.get(position);

        StringTokenizer st = new StringTokenizer(item.getDay(),"-");
        StringBuilder renameDay = new StringBuilder();
        String tmp;
        int index = st.countTokens();
        for(int i=0;i<index;i++){
            tmp = st.nextToken();
            if(tmp.equals("0")){
                renameDay.append("월");
            }else if(tmp.equals("1")){
                renameDay.append("화");
            }else if(tmp.equals("2")){
                renameDay.append("수");
            }else if(tmp.equals("3")){
                renameDay.append("목");
            }else if(tmp.equals("4")){
                renameDay.append("금");
            }else if(tmp.equals("5")){
                renameDay.append("토");
            }else renameDay.append("일");

            if(i!=index-1 && index!=1) renameDay.append("-");
        }
        String ampm = "오전";
        int hour=0, min=0;
        if(item.getHour()>12) {
            ampm = "오후";
            hour = item.getHour()-12;
        }else hour = item.getHour();
        min = item.getMin();
        StringBuilder renameTime = new StringBuilder();

        renameTime.append(ampm)
                .append(" ")
                .append(hour+"시 ")
                .append(min+"분");


        // 가져온 데이터를 텍스트뷰에 입력
        txt_name.setText(item.getName());
        txt_date.setText(item.getDate()+"~"+item.getLastDate());
        txt_day.setText(renameDay.toString());
        txt_time.setText(renameTime.toString());
        case_num.setText(String.valueOf(": "+ item.getCaseNum())+"번 약통");

        Log.d("item",String.valueOf(item.isComplete()));
        if(item.isComplete()) {
            image_complete.setImageResource(R.drawable.checkmark);
        }


        return convertView;
    }

    public void addItem(Long id, String name, String day, String date,int sethour, int setmin, String lastDate, boolean complete,int caseNum){
        CalendarItem calendarItem = new CalendarItem();
        calendarItem.setId(id);
        calendarItem.setName(name);
        calendarItem.setDate(date);
        calendarItem.setDay(day);
        calendarItem.setLastDate(lastDate);
        calendarItem.setHour(sethour);
        calendarItem.setMin(setmin);
        calendarItem.setComplete(complete);
        calendarItem.setCaseNum(caseNum);

        items.add(calendarItem);
    }
}