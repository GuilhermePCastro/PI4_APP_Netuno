package com.example.netuno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.netuno.activitys.MainActivity
import com.example.netuno.databinding.ActivityMainBinding
import com.example.netuno.databinding.ActivitySplashBinding
import com.example.netuno.fragments.HomeFragment

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handle = Handler()
        handle.postDelayed(Runnable { chamaHome() }, 1500)
    }

    fun chamaHome(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}