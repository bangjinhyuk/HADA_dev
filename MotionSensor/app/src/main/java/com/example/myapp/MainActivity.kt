package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()

//        // activity_main의 일반 사용자 로그인 버튼을 누르면 activity_login으로 이동
//        UserLoginB.setOnClickListener {
//            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//        }
//
//        // activity_main의 센터 사용자 로그인 버튼을 누르면 activity_login으로 이동
//        // 일반 사용자/센터 사용자 구분 로그인 아직 안함
//        CenterLoginB.setOnClickListener {
//            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
//        }
//
//        // activity_main의 회원가입 버튼을 누르면 activity_register로 이동
//        RegisterB.setOnClickListener {
//            startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
//        }
//
//        // activity_main의 메인화면(임시)을 누르면 activity_main_list로 이동
//        mainlist.setOnClickListener {
//            startActivity(Intent(this@MainActivity, MainList::class.java))
//        }
    }
}