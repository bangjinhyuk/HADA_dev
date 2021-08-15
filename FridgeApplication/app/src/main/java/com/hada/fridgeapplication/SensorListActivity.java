package com.hada.fridgeapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import java.util.ArrayList;
import java.util.Arrays;

public class SensorListActivity extends AppCompatActivity {
    Boolean swtich_bool = false;
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


        // ========== RecyclerView 연결 영역 ==========

        final RecyclerView rv_layout = findViewById(R.id.recyclerview_detail);
        // RecyclerView의 레이아웃 매니저를 통해 레이아웃 정의
        rv_layout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // RecyclerView에 정의한 Adapter를 연결
        rv_layout.setAdapter(new SensorAdapter(data, this,width,height));

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
    }
    // ========== 테스트용 데이터 정의 영역 ==========
    ArrayList<SensorModel> data = new ArrayList<SensorModel>(Arrays.asList(
            new SensorModel(2,"2번 냉장고 온습도 센서","12.4","39",1 ),
            new SensorModel(3,"3번 냉장고 온습도 센서","13","32",2 ),
            new SensorModel(5,"4번 냉장고 온습도 센서","11","30",0 )

    ));
}

