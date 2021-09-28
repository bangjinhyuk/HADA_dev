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
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private ImageView plus, minus;
    private ActivityResultLauncher<Intent> plus_resultLauncher;
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

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),plusPopup.class);
                plus_resultLauncher.launch(intent);
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
                            values.put("setHour",result.getData().getIntExtra("setHour",0));
                            values.put("setMin",result.getData().getIntExtra("setMin",0));
                            values.put("week",result.getData().getStringExtra("week"));

                            db.insert("mytable",null,values);

                            System.out.println("===============");

                            Cursor c = db.query("mytable",null,null,null,null,null,null,null);
                            while(c.moveToNext()){
                                System.out.println("name : "+c.getString(c.getColumnIndex("name")));
                            }
                            System.out.println("===============");

                            System.out.println(result.getData().getStringExtra("name"));
                            System.out.println(result.getData().getStringExtra("day"));
                            System.out.println(result.getData().getIntExtra("setHour",0));
                            System.out.println(result.getData().getIntExtra("setMin",0));
                            System.out.println(result.getData().getStringExtra("week"));


                        }
                    }
                });



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