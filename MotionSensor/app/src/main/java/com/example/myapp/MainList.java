package com.example.myapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

public class MainList extends AppCompatActivity {

    private TextView tv_time;
    private Button timesettingB, showGraphB, contactB;
    private Switch switch1;
    Handler mHandler = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        // 블루투스 활성화하기

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정

        if(bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
            // 여기에 처리 할 코드를 작성하세요.
        }
        else { // 디바이스가 블루투스를 지원 할 때

            if(bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)

                selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
            }
            else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)
                // 블루투스를 활성화 하기 위한 다이얼로그 출력
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // 선택한 값이 onActivityResult 함수에서 콜백된다.
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        }

        database = new Database(MainList.this, "myapp.db", null, 1);
        db = database.getWritableDatabase();
        database.onCreate(db);

//        ContentValues values = new ContentValues();
//        values.put("date","2021-11-01");
//        values.put("hour","15");
//        values.put("detections","3");
//        db.insert("mytable",null,values);
//        values = new ContentValues();
//        values.put("date","2021-10-25");
//        values.put("hour","3");
//        values.put("detections","14");
//        db.insert("mytable",null,values);
//        values = new ContentValues();
//        values.put("date","2021-10-25");
//        values.put("hour","4");
//        values.put("detections","8");
//        db.insert("mytable",null,values);
//        values = new ContentValues();
//        values.put("date","2021-10-25");
//        values.put("hour","5");
//        values.put("detections","5");
//        db.insert("mytable",null,values);
//
//        values = new ContentValues();
//        values.put("date","2021-10-24");
//        values.put("hour","10");
//        values.put("detections","3");
//        db.insert("mytable",null,values);
//        values = new ContentValues();
//        values.put("date","2021-10-24");
//        values.put("hour","11");
//        values.put("detections","14");
//        db.insert("mytable",null,values);
//        values = new ContentValues();
//        values.put("date","2021-10-24");
//        values.put("hour","12");
//        values.put("detections","8");
//        db.insert("mytable",null,values);
//        values = new ContentValues();
//        values.put("date","2021-10-24");
//        values.put("hour","13");
//        values.put("detections","5");
//        db.insert("mytable",null,values);


        tv_time = findViewById(R.id.tv_time);
        timesettingB = findViewById(R.id.timesettingB);
        showGraphB = findViewById(R.id.showGraphB);
        contactB = findViewById(R.id.contactB);
        switch1 = findViewById(R.id.switch1);

        SharedPreferences sharedPreferences = getSharedPreferences("last",MODE_PRIVATE);
        tv_time.setText(sharedPreferences.getString("date",""));

        SharedPreferences sharedPreferences1 = getSharedPreferences("alarm",MODE_PRIVATE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Thread th = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                while(true){
                    Log.d("123", String.valueOf(sharedPreferences1.getInt("alarmType",0)));
                    if(sharedPreferences1.getInt("alarmType",0)==1&& !switch1.isChecked()) {
                        StringTokenizer st = new StringTokenizer(sharedPreferences1.getString("alarmTime", ""), ":");
                        StringTokenizer st2 = new StringTokenizer(sharedPreferences.getString("date", ""), " ");
                        String date = st2.nextToken();
                        Log.d("123", String.valueOf(st.countTokens() > 0));
                        Log.d("123", String.valueOf(st2.countTokens() > 0));
                        Log.d("123", String.valueOf(date.equals(LocalDate.now().toString())));

                        if (st.countTokens() > 0 && st2.countTokens() > 0 && date.equals(LocalDate.now().toString())) {
                            String amPm = st2.nextToken();
                            int hour, min, setHour, setMin;
                            if (amPm.equals("오후")) {
                                StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "시");
                                StringTokenizer st4 = new StringTokenizer(st2.nextToken(), "분");
                                hour = Integer.parseInt(st3.nextToken()) + 12;
                                min = Integer.parseInt(st4.nextToken());

                            } else {
                                StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "시");
                                StringTokenizer st4 = new StringTokenizer(st2.nextToken(), "분");
                                hour = Integer.parseInt(st3.nextToken());
                                min = Integer.parseInt(st4.nextToken());
                            }
                            setHour = Integer.parseInt(st.nextToken());
                            setMin = Integer.parseInt(st.nextToken());
                            LocalTime nowT = LocalTime.now();

                            hour += setHour;
                            min += setMin;

                            Log.d("123", String.valueOf(hour));
                            Log.d("123", String.valueOf(nowT.getHour()));
                            Log.d("123", String.valueOf(min));

                            if (min > 59) {
                                min -= 60;
                                hour++;
                            }
                            Log.d("123", String.valueOf(hour == nowT.getHour()));
                            Log.d("123", String.valueOf(min == nowT.getMinute()));


                            if (hour == nowT.getHour() && min == nowT.getMinute()) {
                                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                vibrator.vibrate(500); // 0.5초간 진동
                                createNotification();

                            }


                        }
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        th.start();



        timesettingB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TimeSetting.class);
                startActivity(intent);
            }
        });
        showGraphB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ShowGraph.class);
                startActivity(intent);
            }
        });
        contactB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ContactInformation.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT :
                if(requestCode == RESULT_OK) { // '사용'을 눌렀을 때
                    selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
                }
                else { // '취소'를 눌렀을 때
                    // 여기에 처리 할 코드를 작성하세요.
                }
                break;
        }
    }
    public void connectDevice(String deviceName) {

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
                        Log.d("get",readMessage);
                        mHandler.post(new Runnable() {
                            @SuppressLint({"Range", "SetTextI18n"})
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                int detection = Integer.parseInt(String.valueOf(readMessage.charAt(0)));
                                Log.d("get",String.valueOf(detection));

                                if(detection == 1) {
                                    Toast.makeText(getApplicationContext(),"움직임 감지",Toast.LENGTH_LONG).show();
                                    boolean write = false;
                                    ContentValues values = new ContentValues();
                                    LocalDate now = LocalDate.now();
                                    LocalTime nowT = LocalTime.now();

                                    values.put("date",now.toString());
                                    values.put("hour",nowT.getHour());
                                    SharedPreferences sharedPreferences = getSharedPreferences("last",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    if(nowT.getHour()>12) {
                                        tv_time.setText(LocalDate.now() + " 오후 " + (nowT.getHour() - 12) + "시 " + nowT.getMinute() + "분");
                                        editor.putString("date",LocalDate.now() + " 오후 " + (nowT.getHour() - 12) + "시 " + nowT.getMinute() + "분");
                                        editor.apply();
                                    }
                                    else {
                                        tv_time.setText(LocalDate.now() + " 오전 " + nowT.getHour() + "시 " + nowT.getMinute() + "분");
                                        editor.putString("date",LocalDate.now() + " 오전 " + nowT.getHour() + "시 " + nowT.getMinute() + "분");
                                        editor.apply();
                                    }

                                    Cursor c = db.query("mytable",null,null,null,null,null,null,null);
                                    while(c.moveToNext()){
                                        if(c.getString(c.getColumnIndex("date")).equals(now.toString())&& c.getInt(c.getColumnIndex("hour")) == nowT.getHour()){
                                            values.put("detections",c.getInt(c.getColumnIndex("detections"))+1);
                                            int id = c.getInt(c.getColumnIndex("_id"));
                                            db.update("mytable",values,"_id="+id,null);
                                            write =true;
                                        }
                                    }
                                    if (!write){
                                        values.put("detections",1);
                                        db.insert("mytable",null,values);
                                    }
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

        public void write(int input) {
            try {
                mmOutStream.write(input);
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("움직임 감지");
        builder.setContentText("확인 필");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    public void selectBluetoothDevice() {

        // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
        devices = bluetoothAdapter.getBondedDevices();

        // 페어링 된 디바이스의 크기를 저장
        int pariedDeviceCount = devices.size();
        // 페어링 되어있는 장치가 없는 경우
        if(pariedDeviceCount == 0) {
            // 페어링을 하기위한 함수 호출
        }
        // 페어링 되어있는 장치가 있는 경우
        else {

            try {
                connectDevice("mediandbacks");

            }catch (Exception e){
                // 디바이스를 선택하기 위한 다이얼로그 생성

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");

                // 페어링 된 각각의 디바이스의 이름과 주소를 저장

                List<String> list = new ArrayList<>();

                // 모든 디바이스의 이름을 리스트에 추가

                for(BluetoothDevice bluetoothDevice : devices) {

                    list.add(bluetoothDevice.getName());

                }
                list.add("취소");


                // List를 CharSequence 배열로 변경
                final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
                list.toArray(new CharSequence[list.size()]);

                // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너
                builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 해당 디바이스와 연결하는 함수 호출
                        connectDevice(charSequences[which].toString());
                    }
                });
                // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정
                builder.setCancelable(false);
                // 다이얼로그 생성
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }


        }
    }
}