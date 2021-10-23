package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactInformation extends AppCompatActivity {
    private Button registerCall;
    private EditText editTextPhone, editTextPhone2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_information);
        SharedPreferences sharedPreferences = getSharedPreferences("phoneNum",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPhone2 = findViewById(R.id.editTextPhone2);
        registerCall = findViewById(R.id.registerCall);

        editTextPhone.setText(sharedPreferences.getString("num1",""));
        editTextPhone2.setText(sharedPreferences.getString("num2",""));
        editTextPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editTextPhone2.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        registerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("num1",editTextPhone.getText().toString());
                editor.putString("num2",editTextPhone2.getText().toString());
                editor.apply();
                finish();
            }
        });
    }
}