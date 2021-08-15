package com.hada.fridgeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView detail_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detail_title = findViewById(R.id.detail_title);

        Intent getintent = getIntent();

        detail_title.setText(getintent.getExtras().getString("title"));
    }
}