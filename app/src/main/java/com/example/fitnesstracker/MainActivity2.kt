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

class MainActivity2 : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var runningKcal = false
    private var totalKcal = 0f
    private var previousTotalKcal = 0f
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val sButton = findViewById<ImageView>(R.id.imageViewDistance) as ImageView
        sButton.setOnClickListener {
            startActivity(Intent(this@MainActivity2, MainActivity3::class.java))
            finish()
        }
        val hButton = findViewById<ImageView>(R.id.imageViewSteps) as ImageView
        hButton.setOnClickListener {
            startActivity(Intent(this@MainActivity2, MainActivity::class.java))
            finish()
        }
        val tButton = findViewById<ImageView>(R.id.imageViewChronometr2) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@MainActivity2, MainActivity4::class.java))
            finish()
        }
        val kButton = findViewById<ImageView>(R.id.imageViewbmi2) as ImageView
        kButton.setOnClickListener {
            startActivity(Intent(this@MainActivity2, MainActivity5::class.java))
            finish()
        }
        loadDataKcal()
        resetKcal()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        runningKcal = true
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
        var tv_kcalTaken = findViewById<TextView>(R.id.tv_kcalTaken)

        if (runningKcal) {
            totalKcal = event!!.values[0]
            val currentKcal =
                (totalKcal.toInt() - previousTotalKcal.toInt())* 0.05
            tv_kcalTaken.text = (String.format("%.2f",currentKcal))
        }
    }

    fun resetKcal() {

        var tv_kcalTaken = findViewById<TextView>(R.id.tv_kcalTaken)
        tv_kcalTaken.setOnClickListener {
            Toast.makeText(this, "Длительное нажатие для сброса каллорий", Toast.LENGTH_SHORT).show()
        }

        tv_kcalTaken.setOnLongClickListener {

            previousTotalKcal = totalKcal
            tv_kcalTaken.text = 0.toString()
            saveData()

            true
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs2", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("key2", previousTotalKcal)
        editor.apply()
    }

    private fun loadDataKcal() {
        val sharedPreferences = getSharedPreferences("myPrefs2", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key2", 0f)

        Log.d("MainActivity2", "$savedNumber")

        previousTotalKcal = savedNumber
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
