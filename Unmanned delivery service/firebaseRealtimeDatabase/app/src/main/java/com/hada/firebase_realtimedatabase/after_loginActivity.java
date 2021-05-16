package com.hada.firebase_realtimedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        bt_no = (Button)findViewById(R.id.bt_no);

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef_storage = database.getReference("storage");
        myRef_password = database.getReference("password");
        myRef_alarm = database.getReference("alarm");

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
                        bt_no.setVisibility(View.VISIBLE);
                        bt_yes.setVisibility(View.VISIBLE);
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
                                    bt_no.setVisibility(View.VISIBLE);
                                    bt_yes.setVisibility(View.VISIBLE);
                                }else if(task.getResult().getValue().toString().equals("0")){
                                    tv_alarm.setVisibility(View.INVISIBLE);
                                    bt_no.setVisibility(View.INVISIBLE);
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
                        bt_no.setVisibility(View.VISIBLE);
                        bt_yes.setVisibility(View.VISIBLE);
                    }else if (value.toString().equals("0")){
                        tv_alarm.setVisibility(View.INVISIBLE);
                        bt_no.setVisibility(View.INVISIBLE);
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
                myRef_alarm.child(userid).setValue(1);
                tv_alarm.setVisibility(View.INVISIBLE);
                bt_no.setVisibility(View.INVISIBLE);
                bt_yes.setVisibility(View.INVISIBLE);
            }
        });

        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_alarm.setVisibility(View.INVISIBLE);
                bt_no.setVisibility(View.INVISIBLE);
                bt_yes.setVisibility(View.INVISIBLE);
            }
        });
    }
}