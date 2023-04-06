package com.example.fitnesstracker

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity6 : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor

    private var lastUpdate: Long = 0
    private var lastX = 0.0f
    private var lastY = 0.0f
    private var lastZ = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        val mButton = findViewById<ImageView>(R.id.imageViewKcall) as ImageView
        mButton.setOnClickListener {
            startActivity(Intent(this@MainActivity6, MainActivity2::class.java))
            finish()
        }
        val sButton = findViewById<ImageView>(R.id.imageViewDistance) as ImageView
        sButton.setOnClickListener {
            startActivity(Intent(this@MainActivity6, MainActivity3::class.java))
            finish()
        }
        val tButton = findViewById<ImageView>(R.id.imageViewChronometr) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@MainActivity6, MainActivity4::class.java))
            finish()
        }
        val iButton = findViewById<ImageView>(R.id.imageViewSteps) as ImageView
        iButton.setOnClickListener {
            startActivity(Intent(this@MainActivity6, MainActivity::class.java))
            finish()
        }
        val kButton = findViewById<ImageView>(R.id.imageViewbmi) as ImageView
        kButton.setOnClickListener {
            startActivity(Intent(this@MainActivity6, MainActivity5::class.java))
            finish()
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        lastUpdate = System.currentTimeMillis()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastUpdate
            if (timeDifference > 100) {
                lastUpdate = currentTime

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                var tv_speed = findViewById<TextView>(R.id.tv_speed)

                val speed = calculateSpeed(x, y, z)
                tv_speed.text = "%.2f m/s".format(speed)
            }
        }
    }

    private fun calculateSpeed(x: Float, y: Float, z: Float): Double {
        val acceleration = Math.sqrt(Math.pow((x - lastX).toDouble(), 2.0) + Math.pow((y - lastY).toDouble(), 2.0) + Math.pow((z - lastZ).toDouble(), 2.0))
        val delta = acceleration / SensorManager.GRAVITY_EARTH
        lastX = x
        lastY = y
        lastZ = z
        return delta * 9.81
    }
}