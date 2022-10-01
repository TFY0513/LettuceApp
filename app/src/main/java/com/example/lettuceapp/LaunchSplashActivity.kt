package com.example.lettuceapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import com.example.lettuceapp.ui.registerlogin.LoginActivity
import java.lang.Exception

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class LaunchSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
//            val intent = Intent(this, MainActivity::class.java)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1200)
    }
}