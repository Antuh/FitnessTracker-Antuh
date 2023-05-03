package com.example.fitnesstracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main5.*

class MainActivity5 : AppCompatActivity() {
    var backPressedTime: Long = 0

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
        val iButton = findViewById<ImageView>(R.id.imageViewMore) as ImageView
        iButton.setOnClickListener {
            startActivity(Intent(this@MainActivity5, MainActivity7::class.java))
            finish()
        }

        calculate_btn.setOnClickListener {
            val regex = Regex("^\\d+(\\.\\d+)?$")
            if (etHeight.text.isNotEmpty() && etWeight.text.isNotEmpty() && regex.matches(etHeight.text) && regex.matches(etWeight.text)) {
                val height = (etHeight.text.toString()).toInt()
                val weight = (etWeight.text.toString()).toInt()

                val BMI = calculateBMI(height, weight)

                bmi.text = BMI.toString()
                bmi.visibility = View.VISIBLE

                if (BMI < 18.5) {
                    status.text = "Недостаток веса"
                } else if (BMI >= 18.5 && BMI < 24.9) {
                    status.text = "Нормальный вес"
                } else if (BMI >= 24.9 && BMI < 30) {
                    status.text = "Избыточный вес"
                } else if (BMI >= 30) {
                    status.text = "Ожирение"
                }

                bmi_tv.visibility = View.VISIBLE
                status.visibility = View.VISIBLE

                ReCalculate.visibility = View.VISIBLE
                calculate_btn.visibility = View.GONE

            }
            else {
                Toast.makeText(this, "Пожалуйста, введите действительный рост и вес", Toast.LENGTH_SHORT).show()
            }
        }

        ReCalculate.setOnClickListener {
            ResetEverything()
        }

    }
    private fun ResetEverything() {

        calculate_btn.visibility = View.VISIBLE
        ReCalculate.visibility = View.GONE

        etHeight.text.clear()
        etWeight.text.clear()
        status.text = " "
        bmi.text = " "
        bmi_tv.visibility = View.GONE
    }
    private fun calculateBMI(height: Int, weight: Int): Float {

        val Height_in_metre = height.toFloat() / 100
        val BMI = weight.toFloat() / (Height_in_metre * Height_in_metre)

        return BMI
    }
    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Нажмите два раза, чтобы выйти из приложения", Toast.LENGTH_LONG).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
