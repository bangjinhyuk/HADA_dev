package com.hada.pillmanagement;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.StringTokenizer;
import java.util.UUID;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private ImageView plus, minus;
    private ActivityResultLauncher<Intent> plus_resultLauncher,minus_resultLauncher;
    private CalendarDay clickedDay;
    private ListView calendar_listview;
    private PillCalendarListViewAdapter pillCalendarListViewAdapter;
    Handler mHandler = null;
    ConnectedThread connectedThread = null;

    private static final int REQUEST_ENABLE_BT = 10; // ???????????? ????????? ??????

    private BluetoothAdapter bluetoothAdapter; // ???????????? ?????????

    private Set<BluetoothDevice> devices; // ???????????? ???????????? ????????? ???

    private BluetoothDevice bluetoothDevice; // ???????????? ????????????

    private BluetoothSocket bluetoothSocket = null; // ???????????? ??????

    private OutputStream outputStream = null; // ??????????????? ???????????? ???????????? ?????? ?????? ?????????

    private InputStream inputStream = null; // ??????????????? ???????????? ???????????? ?????? ?????? ?????????

    private Thread workerThread = null; // ????????? ????????? ???????????? ?????????

    private byte[] readBuffer; // ?????? ??? ???????????? ???????????? ?????? ??????

    private int readBufferPosition; // ?????? ??? ?????? ?????? ??????

    private TextView textViewReceive; // ?????? ??? ???????????? ???????????? ?????? ????????? ???

    private EditText editTextSend; // ?????? ??? ???????????? ???????????? ?????? ?????? ?????????

    private Button buttonSend; // ???????????? ?????? ??????
    Database database;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // ???????????? ???????????????

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // ???????????? ???????????? ????????? ???????????? ??????

        if(bluetoothAdapter == null) { // ??????????????? ??????????????? ???????????? ?????? ???
            // ????????? ?????? ??? ????????? ???????????????.
        }
        else { // ??????????????? ??????????????? ?????? ??? ???

            if(bluetoothAdapter.isEnabled()) { // ??????????????? ????????? ?????? (????????? ??????????????? ????????????)

                selectBluetoothDevice(); // ???????????? ???????????? ?????? ?????? ??????
            }
            else { // ??????????????? ??? ????????? ?????? (????????? ??????????????? ????????????)
                // ??????????????? ????????? ?????? ?????? ??????????????? ??????
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                // ????????? ?????? onActivityResult ???????????? ????????????.
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        }


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
                            values.put("caseNum",result.getData().getStringExtra("caseNum"));

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
                Log.d("Calendar","?????? ?????? "+date.toString() );
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
                Log.d("Calendar","DB date ?????? ?????? "+findDate+" ?????? ??? Id "+ getCompleteDatePill);
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
                // UI ?????? ?????? X
                while (true) {
                    try {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setCalendar(db,1, clickedDay);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT :
                if(requestCode == RESULT_OK) { // '??????'??? ????????? ???
                    selectBluetoothDevice(); // ???????????? ???????????? ?????? ?????? ??????
                }
                else { // '??????'??? ????????? ???
                    // ????????? ?????? ??? ????????? ???????????????.
                }
                break;
        }
    }
    public void connectDevice(String deviceName) {

        // ????????? ??? ?????????????????? ?????? ??????
        for(BluetoothDevice tempDevice : devices) {
            // ???????????? ????????? ????????? ?????? ??????????????? ???????????? ????????? ??????
            if(deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }
        // UUID ??????
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        // Rfcomm ????????? ?????? ???????????? ??????????????? ???????????? ?????? ??????
        try {
            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                                                                                                                                                                                         bluetoothSocket.connect();
            // ????????? ???,?????? ???????????? ???????????????.
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();
            // ????????? ?????? ?????? ?????? 1
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
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // ????????? ?????? ??????
                                StringTokenizer st = new StringTokenizer(readMessage,",");
                                int caseNum = Integer.parseInt(st.nextToken());
                                String open = st.nextToken();
                                Log.d("dkdkdkdkddk",readMessage);
                                Log.d("dkdkdkdkddk",caseNum+"");
                                Log.d("dkdkdkdkddk",String.valueOf(open.charAt(0)));
                                if(Integer.parseInt(String.valueOf(open.charAt(0))) == 1) setCalendar(db,caseNum+2,clickedDay);
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

    public void selectBluetoothDevice() {

        // ?????? ????????? ???????????? ???????????? ????????? ????????????.
        devices = bluetoothAdapter.getBondedDevices();

        // ????????? ??? ??????????????? ????????? ??????
        int pariedDeviceCount = devices.size();
        // ????????? ???????????? ????????? ?????? ??????
        if(pariedDeviceCount == 0) {
            // ???????????? ???????????? ?????? ??????
        }
        // ????????? ???????????? ????????? ?????? ??????
        else {

            // ??????????????? ???????????? ?????? ??????????????? ??????

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("????????? ???????????? ???????????? ???????????? ??????");

            // ????????? ??? ????????? ??????????????? ????????? ????????? ??????

            List<String> list = new ArrayList<>();

            // ?????? ??????????????? ????????? ???????????? ??????

            for(BluetoothDevice bluetoothDevice : devices) {

                list.add(bluetoothDevice.getName());

            }
            list.add("??????");


            // List??? CharSequence ????????? ??????
            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
            list.toArray(new CharSequence[list.size()]);

            // ?????? ???????????? ????????? ??? ?????? ?????? ????????? ?????????
            builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ?????? ??????????????? ???????????? ?????? ??????
                    connectDevice(charSequences[which].toString());
                }
            });
            // ???????????? ?????? ?????? ??? ?????? ??????????????? ??????
            builder.setCancelable(false);
            // ??????????????? ??????
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public void setCalendar(SQLiteDatabase db ,int type,CalendarDay calendarDay){
        //?????? ????????? ??? ??? ??????
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
        Log.d("Calendar","DB date ?????? ?????? "+findDate+" ?????? ??? Id "+ getCompleteDatePill);

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
                    if(getCompleteDatePill.contains(c.getString(c.getColumnIndex("_id"))+"-")){
                        Log.d("Calendar","DB date ?????? ?????? complete1"+c.getString(c.getColumnIndex("_id")));
                        arrayOfColorDotsToDisplay[numColor] = Color.rgb(0, 0, 255);
                        numColor++;
                    }else{
                        arrayOfColorDotsToDisplay[numColor] = Color.rgb(255, 0, 0);
                        numColor++;
                    }

                }
            }

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            boolean minus = false;
            String renameMin, renameHour, renameday, renameMonth;
            if(c.getInt(c.getColumnIndex("setMin"))<10&&c.getInt(c.getColumnIndex("setMin"))>1 ) renameMin = "0"+(c.getInt(c.getColumnIndex("setMin"))-1);
            else if(c.getInt(c.getColumnIndex("setMin"))>10) renameMin = String.valueOf(c.getInt(c.getColumnIndex("setMin"))-1);
            else {
                renameMin = "59";
                minus = true;
            }
            if(c.getInt(c.getColumnIndex("setHour"))<10) {
                if(minus) renameHour = "0" + (c.getInt(c.getColumnIndex("setHour"))-1);
                else renameHour = "0" + c.getInt(c.getColumnIndex("setHour"));
            }else {
                if(minus) renameHour = String.valueOf(c.getInt(c.getColumnIndex("setHour"))-1);
                else renameHour = String.valueOf(c.getInt(c.getColumnIndex("setHour")));
            }
            renameMonth = (CalendarDay.today().getMonth()+1<10)?"0"+(CalendarDay.today().getMonth()+1):String.valueOf(CalendarDay.today().getMonth()+1);
            renameday = (CalendarDay.today().getDay()<10)?"0"+(CalendarDay.today().getDay()):String.valueOf(CalendarDay.today().getDay());

            sb1.append(CalendarDay.today().getYear())
                    .append("-")
                    .append(renameMonth)
                    .append("-")
                    .append(renameday)
                    .append("T")
                    .append(renameHour)
                    .append(":")
                    .append(renameMin)
                    .append(":00.000");
            renameHour = (LocalTime.now().getHour()<10)?"0"+(LocalTime.now().getHour()):String.valueOf(LocalTime.now().getHour());
            renameMin = (LocalTime.now().getMinute()<10)?"0"+(LocalTime.now().getMinute()):String.valueOf(LocalTime.now().getMinute());

            sb2.append(CalendarDay.today().getYear())
                    .append("-")
                    .append(renameMonth)
                    .append("-")
                    .append(renameday)
                    .append("T")
                    .append(renameHour)
                    .append(":")
                    .append(renameMin)
                    .append(":00.000");


            LocalDateTime date1 = LocalDateTime.parse(sb1);
            LocalDateTime date2 = LocalDateTime.parse(sb2);
            if(date1.isBefore(date2) && date2.isBefore(date1.plusMinutes(30)) && type-2 ==c.getInt(c.getColumnIndex("caseNum"))){


                String sql = "UPDATE date SET completepill='"+(getCompleteDatePill+"-"+c.getString(c.getColumnIndex("_id"))+"-")+"' WHERE date ='"+today+"';";
                db.execSQL(sql);
//                ContentValues values = new ContentValues();
//                values.put("date",today);
//                values.put("completepill",getCompleteDatePill+"-"+c.getString(c.getColumnIndex("_id"))+"-");
//                db.insert("date",null,values);
            }

            while(cDate.moveToNext()){
                if(cDate.getString(cDate.getColumnIndex("date")).equals(today)) {
                    findDate = true;
                    getCompleteDatePill = cDate.getString(cDate.getColumnIndex("completepill"));
                }
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
            builder.setSmallIcon(R.drawable.pills);
            //????????????
            if(calendarDay.isInRange(startdateC,enddateC)){
                int day = calendarDay.getCalendar().get(Calendar.DAY_OF_WEEK);
                if(day>1) day -= 2;
                else day =6;
                if(c.getString(c.getColumnIndex("day")).contains(String.valueOf(day))) {
                    if(getCompleteDatePill.contains(c.getString(c.getColumnIndex("_id"))+"-")){

                        pillCalendarListViewAdapter.addItem(c.getLong(c.getColumnIndex("_id")),
                                c.getString(c.getColumnIndex("name")),
                                c.getString(c.getColumnIndex("day")),
                                c.getString(c.getColumnIndex("date")),
                                c.getInt(c.getColumnIndex("setHour")),
                                c.getInt(c.getColumnIndex("setMin")),
                                c.getString(c.getColumnIndex("lastdate")),
                                true,
                                c.getInt(c.getColumnIndex("caseNum"))
                        );
                    }else{
                        pillCalendarListViewAdapter.addItem(c.getLong(c.getColumnIndex("_id")),
                                c.getString(c.getColumnIndex("name")),
                                c.getString(c.getColumnIndex("day")),
                                c.getString(c.getColumnIndex("date")),
                                c.getInt(c.getColumnIndex("setHour")),
                                c.getInt(c.getColumnIndex("setMin")),
                                c.getString(c.getColumnIndex("lastdate")),
                                false,
                                c.getInt(c.getColumnIndex("caseNum"))
                        );

                        if(LocalTime.now().getHour() == c.getInt(c.getColumnIndex("setHour"))){

                            int setMin = c.getInt(c.getColumnIndex("setMin")), maxMin=0;
                            if(setMin<58)  maxMin=setMin+2;
                            else if(setMin==58) maxMin=0;
                            else if(setMin==59) maxMin=1;
                            if(setMin<58){
                                if(setMin<=LocalTime.now().getMinute() && maxMin>=LocalTime.now().getMinute()){
                                    Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                                    vibrator.vibrate(500); // 0.5?????? ??????

                                    builder.setContentTitle("??? ?????? ???????????????.");
                                    builder.setContentText(c.getString(c.getColumnIndex("name")));
                                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        notificationManager.createNotificationChannel(new NotificationChannel("default", "?????? ??????", NotificationManager.IMPORTANCE_DEFAULT));
                                    }
                                    notificationManager.notify(Integer.parseInt(c.getString(c.getColumnIndex("_id"))), builder.build());
                                }
                            }else{
                                if(setMin<=LocalTime.now().getMinute() || maxMin>=LocalTime.now().getMinute()){
                                    Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                                    vibrator.vibrate(500); // 0.5?????? ??????

                                    builder.setContentTitle("??? ?????? ???????????????.");
                                    builder.setContentText(c.getString(c.getColumnIndex("name")));
                                    NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        notificationManager.createNotificationChannel(new NotificationChannel("default", "?????? ??????", NotificationManager.IMPORTANCE_DEFAULT));
                                    }
                                    notificationManager.notify(Integer.parseInt(c.getString(c.getColumnIndex("_id"))), builder.build());
                                }
                            }
                        }

                    }
                }
            }
            pillCalendarListViewAdapter.notifyDataSetChanged();

        }
        Collection eventsToDisplayInTheCalendar= new ArrayList<>();
        calendarView.removeDecorators();
        calendarView.invalidateDecorators();
        if(type == 0){
            OneDayDecorator oneDayDecorator = new OneDayDecorator(eventsToDisplayInTheCalendar, Arrays.copyOfRange(arrayOfColorDotsToDisplay,0,numColor) );

            calendarView.addDecorators(oneDayDecorator);
            calendarView.setDateSelected(CalendarDay.today(),true);
        }else if(type ==1){
            OneDayPopupDecorator oneDayPopupDecorator = new OneDayPopupDecorator(eventsToDisplayInTheCalendar, Arrays.copyOfRange(arrayOfColorDotsToDisplay,0,numColor) );

            calendarView.addDecorators(oneDayPopupDecorator);
        }

    }
}