package com.hada.firebase_realtimedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class after_loginActivity extends AppCompatActivity {

    TextView textView, tv_password, tv_alarm;
    ImageView iv_storage_now;
    Button bt_yes,bt_no;
    String userid;
    DatabaseReference myRef_storage,myRef_password,myRef_alarm;

    NotificationManager notificationManager;

    PendingIntent intent1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        Intent intent = getIntent();
        userid = intent.getExtras().getString("id");
        textView = (TextView)findViewById(R.id.textView);
        textView.setText(userid+"호");
        tv_password = (TextView)findViewById(R.id.tv_password);
        tv_alarm = (TextView)findViewById(R.id.tv_alarm);
        iv_storage_now = (ImageView)findViewById(R.id.iv_storage_now);
        bt_yes = (Button)findViewById(R.id.bt_yes);

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef_storage = database.getReference("storage");
        myRef_password = database.getReference("password");
        myRef_alarm = database.getReference("alarm");

        intent1 = PendingIntent.getActivity(this, 0, new Intent(getApplicationContext(), after_loginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        //화면 실행시 데이터 베이스에서 한번 정보를 가져옴
        myRef_password.child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else if(task.getResult().getValue() != null) {
                    tv_password.setVisibility(View.VISIBLE);
                    iv_storage_now.setImageResource(R.drawable.checkmark);
                    tv_password.setText("비밀번호:"+task.getResult().getValue().toString());
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });

        myRef_alarm.child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else if(task.getResult().getValue() != null) {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    if(task.getResult().getValue().toString().equals("1")){
                        tv_alarm.setVisibility(View.VISIBLE);
                        bt_yes.setVisibility(View.VISIBLE);
                        notification();

                    }
                }
            }
        });
        //실시간 데이터 베이스 이벤트 리스너
        myRef_password.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.child(userid).getValue() != null) { // DB에 create
                    Object value = dataSnapshot.child(userid).getValue();
                    Log.d("onDataChange", "Value is: " + value.toString());
                    tv_password.setVisibility(View.VISIBLE);
                    iv_storage_now.setImageResource(R.drawable.checkmark);
                    tv_password.setText("비밀번호:"+value.toString());
                    myRef_alarm.child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else if(task.getResult().getValue() != null){
                                Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                if(task.getResult().getValue().toString().equals("1")){
                                    tv_alarm.setVisibility(View.VISIBLE);
                                    bt_yes.setVisibility(View.VISIBLE);
                                    notification();

                                }else if(task.getResult().getValue().toString().equals("0")){
                                    tv_alarm.setVisibility(View.INVISIBLE);
                                    bt_yes.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    });
                }else{  //  DB에서 delete
                    tv_password.setVisibility(View.INVISIBLE);
                    iv_storage_now.setImageResource(R.drawable.xmark);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("onCancelled", "Failed to read value.", error.toException());
            }
        });


        myRef_alarm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.child(userid).getValue() != null) { // DB에 create
                    Object value = dataSnapshot.child(userid).getValue();
                    Log.d("onDataChange", "Value is: " + value.toString());
                    if(value.toString().equals("1")){
                        tv_alarm.setVisibility(View.VISIBLE);
                        bt_yes.setVisibility(View.VISIBLE);
                        notification();
                    }else if (value.toString().equals("0")){
                        tv_alarm.setVisibility(View.INVISIBLE);
                        bt_yes.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("onCancelled", "Failed to read value.", error.toException());
            }
        });

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef_alarm.child(userid).setValue(0);
                tv_alarm.setVisibility(View.INVISIBLE);
                bt_yes.setVisibility(View.INVISIBLE);
            }
        });

    }
    public void notification(){

        //알림(Notification)을 관리하는 관리자 객체를 운영체제(Context)로부터 소환하기
        NotificationManager notificationManager=(NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        //Notification 객체를 생성해주는 건축가객체 생성(AlertDialog 와 비슷)
        NotificationCompat.Builder builder= null;

        //Oreo 버전(API26 버전)이상에서는 알림시에 NotificationChannel 이라는 개념이 필수 구성요소가 됨.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String channelID="channel_01"; //알림채널 식별자
            String channelName="MyChannel01"; //알림채널의 이름(별명)

            //알림채널 객체 만들기
            NotificationChannel channel= new NotificationChannel(channelID,channelName,NotificationManager.IMPORTANCE_DEFAULT);

            //알림매니저에게 채널 객체의 생성을 요청
            notificationManager.createNotificationChannel(channel);

            //알림건축가 객체 생성
            builder=new NotificationCompat.Builder(this, channelID);


        }else{
            //알림 건축가 객체 생성
            builder= new NotificationCompat.Builder(this, null);
        }

        //건축가에게 원하는 알림의 설정작업
        builder.setSmallIcon(android.R.drawable.ic_menu_view);

        //상태바를 드래그하여 아래로 내리면 보이는
        //알림창(확장 상태바)의 설정
        builder.setContentTitle("무인택배 앱");//알림창 제목
        builder.setContentText("비밀번호 입력 오류");//알림창 내용
        //알림창의 큰 이미지
        Bitmap bm= BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
        builder.setLargeIcon(bm);//매개변수가 Bitmap을 줘야한다.

        //건축가에게 알림 객체 생성하도록
        Notification notification=builder.build();

        //알림매니저에게 알림(Notify) 요청
        notificationManager.notify(1, notification);

        //알림 요청시에 사용한 번호를 알림제거 할 수 있음.
        //notificationManager.cancel(1);
    }
}