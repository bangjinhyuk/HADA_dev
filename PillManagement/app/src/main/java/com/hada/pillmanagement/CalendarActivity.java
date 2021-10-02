package com.hada.pillmanagement;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.time.LocalTime;
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
    private ListView calendar_listview;
    private PillCalendarListViewAdapter pillCalendarListViewAdapter;
    Handler mHandler = null;

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
        calendar_listview = findViewById(R.id.calendar_listview);

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
                        setCalendar(db,1,clickedDay);
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
                            setCalendar(db,1,clickedDay);
                        }
                    }
                });

        clickedDay = CalendarDay.today();
        setCalendar(db,0,clickedDay);



        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.d("Calendar","날짜 변경 "+date.toString() );
                clickedDay = date;
                String today = date.getYear()+"."+(date.getMonth()+1)+"."+date.getDay(), getCompleteDatePill = "";
                boolean findDate = false;
                Cursor c = db.query("mytable",null,null,null,null,null,null,null);
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
                setCalendar(db,1,clickedDay);
            }
        });
        mHandler = new Handler();
        Thread t = new Thread(new Runnable(){
            @Override public void run() {
                // UI 작업 수행 X
                while (true) {
                    try {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setCalendar(db, 1, clickedDay);
                            }
                        });
                        Thread.sleep(1000*10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();


    }

    public void setCalendar(SQLiteDatabase db,int type,CalendarDay calendarDay){
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
        pillCalendarListViewAdapter = new PillCalendarListViewAdapter(CalendarActivity.this);
        calendar_listview.setAdapter(pillCalendarListViewAdapter);
        calendar_listview.setClickable(false);
        pillCalendarListViewAdapter.clear();
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
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

            builder.setSmallIcon(R.drawable.pills);
            //리스트뷰
            if(calendarDay.isInRange(startdateC,enddateC)){
                int day = calendarDay.getCalendar().get(Calendar.DAY_OF_WEEK);
                if(day>1) day -= 2;
                else day =6;
                if(c.getString(c.getColumnIndex("day")).contains(String.valueOf(day))) {
                    if(getCompleteDatePill.contains(c.getString(c.getColumnIndex("_id")))){
                        pillCalendarListViewAdapter.addItem(c.getLong(c.getColumnIndex("_id")),
                                c.getString(c.getColumnIndex("name")),
                                c.getString(c.getColumnIndex("day")),
                                c.getString(c.getColumnIndex("date")),
                                c.getInt(c.getColumnIndex("setHour")),
                                c.getInt(c.getColumnIndex("setMin")),
                                c.getString(c.getColumnIndex("lastdate")),
                                true);
                    }else{
                        pillCalendarListViewAdapter.addItem(c.getLong(c.getColumnIndex("_id")),
                                c.getString(c.getColumnIndex("name")),
                                c.getString(c.getColumnIndex("day")),
                                c.getString(c.getColumnIndex("date")),
                                c.getInt(c.getColumnIndex("setHour")),
                                c.getInt(c.getColumnIndex("setMin")),
                                c.getString(c.getColumnIndex("lastdate")),
                                false);

                    }
                    if(LocalTime.now().getHour() == c.getInt(c.getColumnIndex("setHour"))&&
                            LocalTime.now().getMinute() == c.getInt(c.getColumnIndex("setMin"))){
                        builder.setContentTitle("약 섭취 시간입니다.");
                        builder.setContentText(c.getString(c.getColumnIndex("name")));
                        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                        }
                        notificationManager.notify(Integer.parseInt(c.getString(c.getColumnIndex("_id"))), builder.build());
                    }

                }
            }
            pillCalendarListViewAdapter.notifyDataSetChanged();

        }
        Toast.makeText(this.getApplicationContext(),"getText중",Toast.LENGTH_SHORT).show();
        Collection eventsToDisplayInTheCalendar= new ArrayList<>();
        calendarView.removeDecorators();
        calendarView.invalidateDecorators();
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