package com.hada.pillmanagement;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.StringTokenizer;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private ImageView plus, minus;
    private ActivityResultLauncher<Intent> plus_resultLauncher,minus_resultLauncher;
    private CalendarDay clickedDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Database database;
        SQLiteDatabase db;
        database = new Database(CalendarActivity.this, "pill.db", null, 1);
        db = database.getWritableDatabase();
        database.onCreate(db);

        calendarView = findViewById(R.id.calendar_view);
        plus = findViewById(R.id.bt_plus);
        minus = findViewById(R.id.bt_minus);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),minusPopup.class);
                minus_resultLauncher.launch(intent);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),plusPopup.class);
                plus_resultLauncher.launch(intent);
            }
        });

        minus_resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        setCalendar(db,1);
                    }
                });

        plus_resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){

                            ContentValues values = new ContentValues();
                            values.put("name",result.getData().getStringExtra("name"));
                            values.put("day",result.getData().getStringExtra("day"));
                            values.put("date",CalendarDay.today().getYear()+"."+(CalendarDay.today().getMonth()+1)+"."+CalendarDay.today().getDay());
                            values.put("setHour",result.getData().getIntExtra("setHour",0));
                            values.put("setMin",result.getData().getIntExtra("setMin",0));
                            values.put("lastdate",result.getData().getStringExtra("lastdate"));

                            db.insert("mytable",null,values);

                            Cursor c = db.query("mytable",null,null,null,null,null,null,null);
                            while(c.moveToNext()){
                                System.out.println("name : "+c.getString(c.getColumnIndex("name")));
                            }
                            setCalendar(db,1);
                        }
                    }
                });

        clickedDay = CalendarDay.today();
        setCalendar(db,0);



        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.d("Calendar","날짜 변경 "+date.toString() );
                clickedDay = date;
                String today = date.getYear()+"."+(date.getMonth()+1)+"."+date.getDay(), getCompleteDatePill = "";
                boolean findDate = false;
                Cursor cDate = db.query("date",null,null,null,null,null,null,null);
                while(cDate.moveToNext()){
                    if(cDate.getString(cDate.getColumnIndex("date")).equals(today)) {
                        findDate = true;
                        getCompleteDatePill = cDate.getString(cDate.getColumnIndex("completepill"));
                    }
                }
                Log.d("Calendar","DB date 존재 여부 "+findDate+" 먹은 약 Id "+ getCompleteDatePill);
                if(!findDate) {
                    ContentValues values = new ContentValues();
                    values.put("date",today);
                    values.put("completepill","");
                    db.insert("date",null,values);
                }
            }
        });

    }

    public void setCalendar(SQLiteDatabase db,int type){
        //오늘 먹어야 할 약 계산
        Cursor c = db.query("mytable",null,null,null,null,null,null,null);
        Cursor cDate = db.query("date",null,null,null,null,null,null,null);
        String startdate, enddate;
        CalendarDay startdateC, enddateC;
        int[] arrayOfColorDotsToDisplay = new int[5];
        int numColor=0;
        String today = CalendarDay.today().getYear()+"."+(CalendarDay.today().getMonth()+1)+"."+CalendarDay.today().getDay(), getCompleteDatePill = "";
        boolean findDate = false;
        while(cDate.moveToNext()){
            if(cDate.getString(cDate.getColumnIndex("date")).equals(today)) {
                findDate = true;
                getCompleteDatePill = cDate.getString(cDate.getColumnIndex("completepill"));
            }
        }
        Log.d("Calendar","DB date 존재 여부 "+findDate+" 먹은 약 Id "+ getCompleteDatePill);

        if(!findDate) {
            ContentValues values = new ContentValues();
            values.put("date",today);
            values.put("completepill","");
            db.insert("date",null,values);
        }

        while(c.moveToNext()){
            startdate = c.getString(c.getColumnIndex("date"));
            enddate = c.getString(c.getColumnIndex("lastdate"));
            StringTokenizer startdateToken = new StringTokenizer(startdate,".");
            StringTokenizer enddateToken = new StringTokenizer(enddate,".");
            startdateC = CalendarDay.from(Integer.parseInt(startdateToken.nextToken()),
                    Integer.parseInt(startdateToken.nextToken())-1,
                    Integer.parseInt(startdateToken.nextToken()));

            enddateC = CalendarDay.from(Integer.parseInt(enddateToken.nextToken()),
                    Integer.parseInt(enddateToken.nextToken())-1,
                    Integer.parseInt(enddateToken.nextToken()));


            if(CalendarDay.today().isInRange(startdateC,enddateC)){
                int day = CalendarDay.today().getCalendar().get(Calendar.DAY_OF_WEEK);
                if(day>1) day -= 2;
                else day =6;
                if(c.getString(c.getColumnIndex("day")).contains(String.valueOf(day))) {
                    if(getCompleteDatePill.contains(c.getString(c.getColumnIndex("_id")))){
                        arrayOfColorDotsToDisplay[numColor] = Color.rgb(0, 0, 255);
                        numColor++;
                    }else{
                        arrayOfColorDotsToDisplay[numColor] = Color.rgb(255, 0, 0);
                        numColor++;
                    }

                }
            }
        }

        Collection eventsToDisplayInTheCalendar= new ArrayList<>();

        if(type == 0){
            OneDayDecorator oneDayDecorator = new OneDayDecorator(eventsToDisplayInTheCalendar, Arrays.copyOfRange(arrayOfColorDotsToDisplay,0,numColor) );

            calendarView.addDecorators(oneDayDecorator);
            calendarView.setDateSelected(CalendarDay.today(),true);
        }else{
            OneDayPopupDecorator oneDayPopupDecorator = new OneDayPopupDecorator(eventsToDisplayInTheCalendar, Arrays.copyOfRange(arrayOfColorDotsToDisplay,0,numColor) );

            calendarView.addDecorators(oneDayPopupDecorator);
        }

    }
}