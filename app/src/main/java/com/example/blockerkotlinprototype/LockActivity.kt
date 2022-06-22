package com.example.blockerkotlinprototype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LockActivity : AppCompatActivity() {

    //Final result: displays this lock screen activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
    }
}