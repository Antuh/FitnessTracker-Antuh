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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class MainActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var currentSteps = 0
    var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mButton = findViewById<ImageView>(R.id.imageViewKcall) as ImageView
        mButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
            finish()
        }
        val sButton = findViewById<ImageView>(R.id.imageViewDistance) as ImageView
        sButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity3::class.java))
            finish()
        }
        val tButton = findViewById<ImageView>(R.id.imageViewChronometr) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity4::class.java))
            finish()
        }
        val kButton = findViewById<ImageView>(R.id.imageViewMore) as ImageView
        kButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity7::class.java))
            finish()
        }

        loadData()
        resetSteps()
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
        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)

        if (running) {
            totalSteps = event!!.values[0]
            currentSteps =
                totalSteps.toInt() - previousTotalSteps.toInt()
            tv_stepsTaken.text = ("$currentSteps")


            var kcal = currentSteps * 0.05;
            var distance = currentSteps * 0.0007;
            var kcalform = (String.format("%.2f", kcal))
            var distform = (String.format("%.3f", distance))

            val notificationManager = NotificationManagerCompat.from(this)
            val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("Активность")
                .setContentText("Шаги: $currentSteps   Каллории: $kcalform   Дистанция: $distform")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
            notificationManager.notify(5, builder.build())
        }
    }

    fun resetSteps() {
        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
        tv_stepsTaken.setOnClickListener {
            Toast.makeText(this, "Длительное нажатие для сброса шагов", Toast.LENGTH_SHORT).show()
        }

        tv_stepsTaken.setOnLongClickListener {

            previousTotalSteps = totalSteps
            tv_stepsTaken.text = 0.toString()
            saveData()

            true
        }
    }

    private fun saveData() {

        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
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

/*
val handler = Handler(Looper.getMainLooper())
val updateTimeTask = object: Runnable {
    override fun run() {
        val currentTime = LocalTime.now()
        if (currentTime.hour == 0 && currentTime.minute == 0) {
            performAction()
        }
        handler.postDelayed(this, 1000)
    }
}
handler.post(updateTimeTask)
}

fun performAction() {
    previousTotalSteps = totalSteps
            tv_stepsTaken.text = 0.toString()
            saveData()
}*/