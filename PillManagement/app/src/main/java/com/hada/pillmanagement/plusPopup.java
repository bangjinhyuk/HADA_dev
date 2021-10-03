package com.hada.pillmanagement;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class plusPopup extends Activity {

    private Button monday,tuesday,wednesday,thursday,friday,saturday,sunday,ampm,set,lastdate;
    private EditText name, hour, min, case_num;
    private boolean [] click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plus_popup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        ampm = findViewById(R.id.ampm);
        name = findViewById(R.id.name);
        hour = findViewById(R.id.hour);
        min = findViewById(R.id.min);
        set = findViewById(R.id.plus_set);
        lastdate = findViewById(R.id.lastdate);
        case_num = findViewById(R.id.case_num);

        click = new boolean[8];

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[0]){
                    setTextColor(monday,0);
                    monday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[0] = false;
                }else{
                    setTextColor(monday,1);
                    monday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[0] = true;
                }
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[1]){
                    setTextColor(tuesday,0);
                    tuesday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[1] = false;
                }else{
                    setTextColor(tuesday,1);
                    tuesday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[1] = true;
                }
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[2]){
                    setTextColor(wednesday,0);
                    wednesday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[2] = false;
                }else{
                    setTextColor(wednesday,1);
                    wednesday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[2] = true;
                }
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[3]){
                    setTextColor(thursday,0);
                    thursday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[3] = false;
                }else{
                    setTextColor(thursday,1);
                    thursday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[3] = true;
                }
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[4]){
                    setTextColor(friday,0);
                    friday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[4] = false;
                }else{
                    setTextColor(friday,1);
                    friday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[4] = true;
                }
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[5]){
                    setTextColor(saturday,0);
                    saturday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[5] = false;
                }else{
                    setTextColor(saturday,1);
                    saturday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[5] = true;
                }
            }
        });

        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click[6]){
                    setTextColor(sunday,0);
                    sunday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_layout));
                    click[6] = false;
                }else{
                    setTextColor(sunday,1);
                    sunday.setBackground(ContextCompat.getDrawable(v.getContext(),R.drawable.day_clicked_layout));
                    click[6] = true;
                }
            }
        });

        ampm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(click[7]);
                if(click[7]){
                    ampm.setText("오전");
                    click[7] = false;
                }else{
                    ampm.setText("오후");
                    click[7] = true;
                }
            }
        });

        lastdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(v.getContext(), mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();

            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                String day = "";
                for(int i=0;i<7;i++){
                    if(click[i]) day = day+i+"-";
                }
                if(hour.getText().toString().equals("")||
                        min.getText().toString().equals("")||
                        name.getText().toString().equals("")||
                        lastdate.getText().toString().equals("종료일 설정")||
                        case_num.getText().toString().equals("")){
                    Toast.makeText(v.getContext(),"전부 올바르게 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                int setHour = Integer.parseInt(hour.getText().toString());
                int setMin = Integer.parseInt(min.getText().toString());
                if(click[7]) setHour+=12;

                intent.putExtra("name",name.getText().toString())
                        .putExtra("day",day)
                        .putExtra("setHour",setHour)
                        .putExtra("setMin",setMin)
                        .putExtra("lastdate",lastdate.getText().toString())
                        .putExtra("caseNum",case_num.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    public void setTextColor(Button button, int i){
        if(i==0){
            button.setTextColor(Color.BLACK);
        }else{
            button.setTextColor(Color.WHITE);
        }
    }
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    Button lastdate = findViewById(R.id.lastdate);
                    lastdate.setText(String.format("%d.%d.%d", yy,mm+1,dd));
                }
            };

}