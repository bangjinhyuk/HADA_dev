package com.example.myapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

public class ShowGraph extends AppCompatActivity {
    private LineChart lineChart;
    private Button detail_calender;
    private ActivityResultLauncher<Intent> calendar_resultLauncher;
    private Date date;
    private Long now;
    private SimpleDateFormat dateFormat;
    private String TAG = "ShowGraph";
    private TextView detail_date;
    private GraphService graphService;

    ConnectedThread connectedThread = null;

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태

    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터

    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋

    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스

    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓

    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림

    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림

    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드

    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼

    private int readBufferPosition; // 버퍼 내 문자 저장 위치

    private TextView textViewReceive; // 수신 된 데이터를 표시하기 위한 텍스트 뷰

    private EditText editTextSend; // 송신 할 데이터를 작성하기 위한 에딧 텍스트

    private Button buttonSend; // 송신하기 위한 버튼


    Database database;
    SQLiteDatabase db;

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);

//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정
//
//        if(bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
//            // 여기에 처리 할 코드를 작성하세요.
//        }
//        else { // 디바이스가 블루투스를 지원 할 때
//
//            if(bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
//
//                connectDevice("mediandbacks");
//            }
//            else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)
//                // 블루투스를 활성화 하기 위한 다이얼로그 출력
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                // 선택한 값이 onActivityResult 함수에서 콜백된다.
//                startActivityForResult(intent, REQUEST_ENABLE_BT);
//            }
//        }



        now = System.currentTimeMillis();
        date = new Date(now);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //오늘 날짜 받아서 저장
        Log.d(TAG, "onCreate: currentTimeMillis"+now);
        //TODO 그래프 추가
        lineChart = findViewById(R.id.chart);
        detail_calender = findViewById(R.id.detail_calender);
        detail_date = findViewById(R.id.detail_date);
        graphService = new GraphService(lineChart);
        lineChart.getLegend().setEnabled(false);

        database = new Database(ShowGraph.this, "myapp.db", null, 1);
        db = database.getWritableDatabase();
        database.onCreate(db);

        drawGraph(date);

        //Todo: calender view
        detail_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),CalendarPopup.class)
                        .putExtra("date",date.getTime());
                calendar_resultLauncher.launch(intent);

            }
        });

        //Todo: get databases -date entity
        calendar_resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: calendar_resultLauncher");
                        if(result.getResultCode() == RESULT_OK){
                            setDate(new Date(result.getData().getExtras().getLong("date")));
                            Log.d(TAG, "onActivityResult: getResult" + new Date(getNow()));
                            StringTokenizer st1 = new StringTokenizer(dateFormat.format(date),"-");
                            StringTokenizer st2 = new StringTokenizer(dateFormat.format(new Date(now)),"-");
                            if (st1.nextToken().equals(st2.nextToken())&&
                                    st1.nextToken().equals(st2.nextToken())&&
                                    st1.nextToken().equals(st2.nextToken())) detail_date.setText("• 실시간 그래프");
                            else detail_date.setText("• "+dateFormat.format(date));
                        }else Log.e(TAG,"onActivityResult: calendar_resultLauncher cancel");
                        drawGraph(getDate());
                    }
                }
        );

    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void drawGraph(Date date) {
        List<Entry> entries = new ArrayList<>();

        Cursor c = db.query("mytable",null,null,null,null,null,null,null);
        while(c.moveToNext()){
            if(c.getString(c.getColumnIndex("date")).equals(dateFormat.format(date))){
                entries.add(new Entry(c.getInt(c.getColumnIndex("hour")),c.getInt(c.getColumnIndex("detections"))));
            }
        }
        entries.sort((Entry e1,Entry e2) -> (int) (e1.getX()-e2.getX()));
        System.out.println("--s---");
        System.out.println(entries.size());
        entries.forEach(System.out::println);
        System.out.println("--s0---");

        graphService.drawGraph(entries, Color.rgb(153, 153, 255));

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }


    public void connectDevice(String deviceName) {
        devices = bluetoothAdapter.getBondedDevices();

        // 페어링 된 디바이스들을 모두 탐색
        for(BluetoothDevice tempDevice : devices) {
            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
            if(deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }
        // UUID 생성
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            // 데이터 송,수신 스트림을 얻어옵니다.
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();
            // 데이터 수신 함수 호출 1
            connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {

        finish();
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            Handler mHandler = new Handler(Looper.getMainLooper());
            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        buffer = new byte[1024];
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        final String readMessage = new String((byte[]) buffer, "UTF-8");
                        Log.d("get", readMessage);
                        mHandler.post(new Runnable() {
                            @SuppressLint({"Range", "SetTextI18n"})
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                int detection = Integer.parseInt(String.valueOf(readMessage.charAt(0)));
                                Log.d("get", String.valueOf(detection));

                                if (detection == 1) {
                                    Toast.makeText(getApplicationContext(), "움직임 감지", Toast.LENGTH_LONG).show();
                                    boolean write = false;
                                    ContentValues values = new ContentValues();
                                    LocalDate now = LocalDate.now();
                                    LocalTime nowT = LocalTime.now();

                                    values.put("date", now.toString());
                                    values.put("hour", nowT.getHour());
                                    SharedPreferences sharedPreferences = getSharedPreferences("last", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    if (nowT.getHour() > 12) {
                                        editor.putString("date", LocalDate.now() + " 오후 " + (nowT.getHour() - 12) + "시 " + nowT.getMinute() + "분");
                                        editor.apply();
                                    } else {
                                        editor.putString("date", LocalDate.now() + " 오전 " + nowT.getHour() + "시 " + nowT.getMinute() + "분");
                                        editor.apply();
                                    }

                                    Cursor c = db.query("mytable", null, null, null, null, null, null, null);
                                    while (c.moveToNext()) {
                                        if (c.getString(c.getColumnIndex("date")).equals(now.toString()) && c.getInt(c.getColumnIndex("hour")) == nowT.getHour()) {
                                            values.put("detections", c.getInt(c.getColumnIndex("detections")) + 1);
                                            int id = c.getInt(c.getColumnIndex("_id"));
                                            db.update("mytable", values, "_id=" + id, null);
                                            write = true;
                                        }
                                    }
                                    if (!write) {
                                        values.put("detections", 1);
                                        db.insert("mytable", null, values);
                                    }
                                    drawGraph(getDate());

                                }

                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }
    }
}