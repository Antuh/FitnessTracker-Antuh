package com.example.fitnesstracker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main5.*

class MainActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        val mButton = findViewById<ImageView>(R.id.imageViewKcall) as ImageView
        mButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5, MainActivity2::class.java))
        }
        val sButton = findViewById<ImageView>(R.id.imageViewDistance) as ImageView
        sButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5, MainActivity3::class.java))
        }
        val tButton = findViewById<ImageView>(R.id.imageViewChronometr) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5, MainActivity4::class.java))
        }
        val kButton = findViewById<ImageView>(R.id.imageViewSteps) as ImageView
        kButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5, MainActivity::class.java))
        }

        btn.setOnClickListener {
            val h = (height.text).toString().toFloat() /100
            val w = weight.text.toString().toFloat()
            val res = w/(h*h)
            result.text = "%.2f".format(res)


        }
    }
}