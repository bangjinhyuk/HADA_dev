package com.hada.fridgeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class SensorListActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Boolean tmp = true;
    private int tmpNum = 1;
    private ArrayList<SensorModel> data = new ArrayList<SensorModel>();
    private Context context;
    private RecyclerView rv_layout;
    private long backBtnTime=0;
    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_list);

        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;
        int height = size.y;
        context = getApplicationContext();

        // ========== RecyclerView 연결 영역 ==========
        rv_layout = findViewById(R.id.recyclerview_detail);

        FirebaseApp.initializeApp(getApplicationContext());
        mDatabase = FirebaseDatabase.getInstance().getReference("school1");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {

                        for(int i = 1;i<100;i++){
                            if(task.getResult().hasChild(""+i)){
                                Log.d("firebase", "data"+ task.getResult().child(""+i).getValue());
                                SensorModel value = task.getResult().child(""+i).getValue(SensorModel.class);
                                value.setId(i);
                                Log.d("firebase", "value"+value);

                                data.add(value);
                            }
                        }
                        // RecyclerView의 레이아웃 매니저를 통해 레이아웃 정의
                        rv_layout.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        // RecyclerView에 정의한 Adapter를 연결
                        rv_layout.setAdapter(new SensorAdapter(data, context));

                    } else {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                }
            });


        // ========== SwipeRefresh 정의 영역 ==========

        final SwipeRefreshLayout swipe_refresh = findViewById(R.id.swipe);
        final Context context = this;
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rv_layout.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                swipe_refresh.setRefreshing(false);
            }
        });
        //===================================//
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }

        if (RealService.serviceIntent==null) {
            serviceIntent = new Intent(this, RealService.class);
            startService(serviceIntent);
        } else {
            serviceIntent = RealService.serviceIntent;//getInstance().getApplication();
//            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }
        //===================================//

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //===================================//
        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
        //===================================//

    }



    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }


    }



}

