package com.hada.fridgeapplication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class RealService extends Service {
    private Thread mainThread;
    public static Intent serviceIntent = null;
    private DatabaseReference mDatabase;
    private ArrayList<SensorModel> data = new ArrayList<SensorModel>();
    private ImageView sensor_color;
    private TextView color_state;

    public RealService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
//        showToast(getApplication(), "Start Service");

        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("aa hh:mm");
                boolean run = true;



                while (run) {
                    try {
                        FirebaseApp.initializeApp(getApplicationContext());

                        mDatabase = FirebaseDatabase.getInstance().getReference("school1");
                        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                data = new ArrayList<SensorModel>();
                                if (task.isSuccessful()) {
                                    for(int i = 1;i<100;i++){
                                        if(task.getResult().hasChild(""+i)){
                                            Log.d("firebase", "data"+ task.getResult().child(""+i).getValue());
                                            SensorModel value = task.getResult().child(""+i).getValue(SensorModel.class);
                                            value.setId(i);
                                            Log.d("firebase", "value"+value);
                                            data.add(value);


                                            if(value.getTempRange()!=null&&value.getHumiRange()!=null){
                                                StringTokenizer temp = new StringTokenizer(value.getTempRange(),"~");
                                                StringTokenizer humi = new StringTokenizer(value.getHumiRange(),"~");
                                                if (value.getTemp()>=Double.parseDouble(temp.nextToken())&&
                                                        value.getTemp()<=Double.parseDouble(temp.nextToken())&&
                                                        value.getHumi()>=Double.parseDouble(humi.nextToken())&&
                                                        value.getHumi()<=Double.parseDouble(humi.nextToken())){
                                                }else {
                                                    sendNotification(value.getSensorName());

                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                            }
                        });

//
                        Thread.sleep(1000 * 60 * 1); // 1 minute
                        Date date = new Date();
//                        showToast(getApplication(), sdf.format(date));
//                        sendNotification(sdf.format(date));
                    } catch (InterruptedException e) {
                        run = false;
                        e.printStackTrace();
                    }
                }
            }
        });
        mainThread.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        serviceIntent = null;
        setAlarmTimer();
        Thread.currentThread().interrupt();

        if (mainThread != null) {
            mainThread.interrupt();
            mainThread = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void showToast(final Application application, final String msg) {
        Handler h = new Handler(application.getMainLooper());
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(application, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Intent intent = new Intent(this, AlarmRecever.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.getalarm)//drawable.splash)
//                        .setContentTitle(messageBody)
                        .setContentTitle("냉장고")
                        .setContentText("확인 필요")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
