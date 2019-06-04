package com.tribyssapps.refreshmentinvestment

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle

import com.tribyssapps.refreshmentinvestment.activities.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}
