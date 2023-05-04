package com.example.fitnesstracker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.fitnesstracker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    var backPressedTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED)
        {
            //onStepCounterPermissionGranted()
        } else
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), RQ_PERMISSION_FOR_STEPCOUNTER_CODE)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.imageViewKcall.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
            finish()
        }
        binding.imageViewDistance.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity3::class.java))
            finish()
        }
        binding.imageViewChronometr.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity4::class.java))
            finish()
        }
        binding.imageViewMore.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity7::class.java))
            finish()
        }
        loadData()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val view = binding.root
        setContentView(view)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            RQ_PERMISSION_FOR_STEPCOUNTER_CODE ->
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //onStepCounterPermissionGranted()
                } else
                {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION))
                    {
                        askUserForOpeningUpSettings()
                    }
                }
            }
        }
    }
    private fun askUserForOpeningUpSettings()
    {
        val sppSettingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
        if (packageManager.resolveActivity(sppSettingsIntent, PackageManager.MATCH_DEFAULT_ONLY) == null)
        {
            Toast.makeText(this, "В разрешениях отказано навсегда", Toast.LENGTH_SHORT).show()
        } else
        {
            AlertDialog.Builder(this)
                .setTitle("В разрешении отказано")
                .setMessage(
                    "Вам было отказано в разрешениях на постоянной основе." +
                            " Вы можете изменить это в настройках приложения.\n\n" +
                            " Хотите открыть настройки приложения?"
                )
                .setPositiveButton("Открыть") { _, _ ->
                    startActivity(sppSettingsIntent)
                }
                .create()
                .show()
        }
    }
    /*private fun onStepCounterPermissionGranted()
    {
        Toast.makeText(this, "Получено разрешение датчика", Toast.LENGTH_LONG).show()
    }*/

    private companion object
    {
        const val RQ_PERMISSION_FOR_STEPCOUNTER_CODE = 1
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null)
        {
            Toast.makeText(this, "Данное приложение не поддерживается на вашем девайсе", Toast.LENGTH_SHORT).show()
        } else
        {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)

        if (running) {
            totalSteps = event!!.values[0]
             val currentSteps =
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
        binding.tvStepsTaken.setOnClickListener {
            Toast.makeText(this, "Длительное нажатие для сброса шагов", Toast.LENGTH_SHORT).show()
        }
        binding.tvStepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            binding.tvStepsTaken.text = 0.toString()
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