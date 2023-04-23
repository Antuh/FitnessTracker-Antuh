package com.example.fitnesstracker

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity3 : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var runningdistance = false
    private var totalDistance = 0f
    private var previousTotalDistance = 0f
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        val mButton = findViewById<ImageView>(R.id.imageViewKcall) as ImageView
        mButton.setOnClickListener {
            startActivity(Intent(this@MainActivity3, MainActivity2::class.java))
            finish()
        }
        val hButton = findViewById<ImageView>(R.id.imageViewSteps) as ImageView
        hButton.setOnClickListener {
            startActivity(Intent(this@MainActivity3, MainActivity::class.java))
            finish()
        }
        val tButton = findViewById<ImageView>(R.id.imageViewChronometr3) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@MainActivity3, MainActivity4::class.java))
            finish()
        }
        val kButton = findViewById<ImageView>(R.id.imageViewbmi3) as ImageView
        kButton.setOnClickListener {
            startActivity(Intent(this@MainActivity3, MainActivity5::class.java))
            finish()
        }
        loadDataDistance()
        resetDistance()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        runningdistance = true
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
        var tv_distanceTaken = findViewById<TextView>(R.id.tv_distanceTaken)

        if (runningdistance) {
            totalDistance = event!!.values[0]
            val currentDistance =
                (totalDistance.toInt() - previousTotalDistance.toInt())*0.0007
            tv_distanceTaken.text = (String.format("%.3f",currentDistance))
        }
    }

    fun resetDistance() {
        var tv_distanceTaken = findViewById<TextView>(R.id.tv_distanceTaken)
        tv_distanceTaken.setOnClickListener {
            Toast.makeText(this, "Длительное нажатие для сброса дистанции", Toast.LENGTH_SHORT).show()
        }
        tv_distanceTaken.setOnLongClickListener {

            previousTotalDistance = totalDistance
            tv_distanceTaken.text = 0.toString()
            saveData()
            true
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs3", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key3", previousTotalDistance)
        editor.apply()
    }

    private fun loadDataDistance() {

        val sharedPreferences = getSharedPreferences("myPrefs3", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key3", 0f)

        Log.d("MainActivity3", "$savedNumber")

        previousTotalDistance = savedNumber
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
