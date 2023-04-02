package com.example.fitnesstracker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main5.*

class MainActivity5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        val mButton = findViewById<ImageView>(R.id.imageViewKcall) as ImageView
        mButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5 , MainActivity2::class.java))
            finish()
        }
        val sButton = findViewById<ImageView>(R.id.imageViewDistance) as ImageView
        sButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5, MainActivity3::class.java))
            finish()
        }
        val tButton = findViewById<ImageView>(R.id.imageViewChronometr) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5, MainActivity4::class.java))
            finish()
        }
        val kButton = findViewById<ImageView>(R.id.imageViewSteps) as ImageView
        kButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5, MainActivity::class.java))
            finish()
        }

        btn.setOnClickListener {
            val regex = Regex("^\\d+(\\.\\d+)?$")
            if (height.text.isNullOrEmpty() && weight.text.isNullOrEmpty())  {
                Toast.makeText(this, "Введите данные веса и роста", Toast.LENGTH_SHORT).show()
            } else {
                if (regex.matches(height.text) && regex.matches(weight.text)) {
                    val h = (height.text).toString().toFloat() /100
                    val w = weight.text.toString().toFloat()
                    val res = w/(h*h)
                    result.text = "%.2f".format(res)
                } else {
                    Toast.makeText(this, "Корректно введите данные веса и роста", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}