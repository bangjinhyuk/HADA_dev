package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEt, passwordEt;
    private Button loginBtn;
    private TextView noAccountTv;
    private String TAG = "LoginActivity";
    private CheckBox center_checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        noAccountTv = findViewById(R.id.noAccountTv);
        center_checkBox = findViewById(R.id.center_checkBox);

        SharedPreferences sharedPreferences = getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, emailEt.getText().toString());
                Log.d(TAG, passwordEt.getText().toString());

                mAuth.signInWithEmailAndPassword(emailEt.getText().toString(), passwordEt.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmailAndPassword:success");
                                    Toast.makeText(getApplicationContext(), "Authentication succeed.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        if(center_checkBox.isChecked()) editor.putInt("mode",1);
                                        else editor.putInt("mode",0);
                                        editor.putString("email",user.getEmail());
                                        editor.apply();
                                        Intent intent = new Intent(getApplicationContext(),MainList.class);
                                        Log.d(TAG, "sharedPreferences:getEmail"+sharedPreferences.getString("email",""));
                                        startActivity(intent);
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmailAndPassword:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        noAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}