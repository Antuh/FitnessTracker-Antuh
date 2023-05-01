package com.example.fitnesstracker

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity7 : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var currentSteps = 0
    var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)

        val lButton = findViewById<CardView>(R.id.BMIcalculator) as CardView
        lButton.setOnClickListener {
            startActivity(Intent(this@MainActivity7, MainActivity5::class.java))
            finish()
        }
        val qButton = findViewById<CardView>(R.id.PieChart) as CardView
        qButton.setOnClickListener {
            startActivity(Intent(this@MainActivity7, PieCharts::class.java))
            finish()
            val intent = Intent(this, PieCharts::class.java)
            intent.putExtra("steps",currentSteps)
            startActivity(intent)
            finish()
        }
        val gButton = findViewById<CardView>(R.id.Speed) as CardView
        gButton.setOnClickListener {
            startActivity(Intent(this@MainActivity7, MainActivity6::class.java))
            finish()
        }
        val mButton = findViewById<ImageView>(R.id.imageViewKcall) as ImageView
        mButton.setOnClickListener {
            startActivity(Intent(this@MainActivity7, MainActivity2::class.java))
            finish()
        }
        val sButton = findViewById<ImageView>(R.id.imageViewDistance) as ImageView
        sButton.setOnClickListener {
            startActivity(Intent(this@MainActivity7, MainActivity3::class.java))
            finish()
        }
        val tButton = findViewById<ImageView>(R.id.imageViewChronometr) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@MainActivity7, MainActivity4::class.java))
            finish()
        }
        val yButton = findViewById<ImageView>(R.id.imageViewSteps) as ImageView
        yButton.setOnClickListener {
            startActivity(Intent(this@MainActivity7, MainActivity::class.java))
            finish()
        }
        loadData()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(
                this,
                "Данное приложение не поддерживается на вашем девайсе",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (running) {
            totalSteps = event!!.values[0]
            currentSteps =
                totalSteps.toInt() - previousTotalSteps.toInt()
        }
    }


    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)

        Log.d("MainActivity", "$savedNumber")

        previousTotalSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
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