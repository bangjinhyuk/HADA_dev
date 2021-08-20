package com.hada.fridgeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ModifyPopup extends Activity {
    private EditText modify_text;
    private Button modify_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modify_popup);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        modify_text = findViewById(R.id.modify_text);
        modify_ok = findViewById(R.id.modify_ok);

        modify_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!modify_text.getText().toString().trim().equals("")){
                    if (modify_text.getText().toString().length()>20) Toast.makeText(v.getContext(),"20자 이내로 적어주세요",Toast.LENGTH_LONG).show();
                    else{
                        //TODO: 서버에 이름 넣기
                        Intent intent = new Intent(getApplicationContext(),DetailActivity.class)
                                .putExtra("modify_name",modify_text.getText().toString());
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }else Toast.makeText(v.getContext(),"바꾸실 이름을 먼저 입력해주세요!",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}