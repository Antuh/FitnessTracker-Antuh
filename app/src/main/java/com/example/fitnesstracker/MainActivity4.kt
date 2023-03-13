package com.example.fitnesstracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        val mButton = findViewById<ImageView>(R.id.imageViewKcall) as ImageView
        mButton.setOnClickListener {
            startActivity(Intent(this@MainActivity4, MainActivity2::class.java))
        }
        val sButton = findViewById<ImageView>(R.id.imageViewDistance) as ImageView
        sButton.setOnClickListener {
            startActivity(Intent(this@MainActivity4, MainActivity3::class.java))
        }
        val tButton = findViewById<ImageView>(R.id.imageViewSteps) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@MainActivity4, MainActivity::class.java))
        }
        val kButton = findViewById<ImageView>(R.id.imageViewbmi4) as ImageView
        kButton.setOnClickListener {
            startActivity(Intent(this@MainActivity4, MainActivity5::class.java))
        }
        var chronometer: Chronometer = findViewById((R.id.chronometer2))
        var butstart: Button = findViewById(R.id.buttonstart)
        var stopbut: Button = findViewById(R.id.buttonpause)
        butstart.setOnClickListener{
            chronometer.start()
        }
        stopbut.setOnClickListener{
            chronometer.stop()
        }
    }

}
