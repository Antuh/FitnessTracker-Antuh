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

    // Добавлен SensorEventListener в класс MainActivity
    // Реализовать все элементы в классе MainActivity
    // после добавления SensorEventListener
    // мы присвоили sensorManger значение nullable
    private var sensorManager: SensorManager? = null

    // Создание переменной, которая будет давать статус выполнения
    // и изначально присвоено логическое значение false
    private var running = false

    // Создание переменной, которая будет подсчитывать общее количество шагов
    // и ему было присвоено значение 0 с плавающей точкой
    private var totalSteps = 0f

    // Создание переменной, которая подсчитывает предыдущую сумму
    // шаги, и ему также было присвоено значение 0 с плавающей точкой
    private var previousTotalSteps = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //initialize
        val mButton = findViewById<ImageView>(R.id.imageView3) as ImageView
        //handle onClick
        mButton.setOnClickListener {
            //intent to start NewActivity
            startActivity(Intent(this@MainActivity2, MainActivity::class.java))
        }

        loadData()
        resetSteps()

        // Добавление контекста диспетчера датчиков SENSOR_SERVICE aas
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true

        // Возвращает количество шагов, предпринятых пользователем с момента последней перезагрузки при активации
        // Для этого датчика требуется разрешение android.permission.ACTIVITY_RECOGNITION.
        // Не забываем добавить следующее разрешение в AndroidManifest.xml присутствует в папке манифеста приложения.
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


        if (stepSensor == null) {
            // Это выдаст пользователю всплывающее сообщение, если в устройстве нет датчика
            Toast.makeText(
                this,
                "Данное приложение не поддерживается на вашем девайсе",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // Скорость, подходящая для пользовательского интерфейса
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {

        // Вызов TextView, который мы создали в activity_main.xml
        // по идентификатору, присвоенному этому текстовому представлению
        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)

        if (running) {
            totalSteps = event!!.values[0]

            // Текущие шаги рассчитываются путем учета разницы между общими шагами
            // и предыдущие шаги
            val currentSteps =
                totalSteps.toInt() * 0.01//сдесь расчет каллорий и т.д.

            // It will show the current steps to the user
            tv_stepsTaken.text = ("$currentSteps")
        }
    }

    fun resetSteps() {
        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
        tv_stepsTaken.setOnClickListener {
            // Это выдаст всплывающее сообщение, если пользователь захочет сбросить шаги
            Toast.makeText(this, "Длительное нажатие для сброса шагов", Toast.LENGTH_SHORT).show()
        }

        tv_stepsTaken.setOnLongClickListener {

            previousTotalSteps = totalSteps

            // Когда пользователь нажмет длительное нажатие на экран,
            // шаги будут сброшены на 0
            tv_stepsTaken.text = 0.toString()

            // Это сохранит данные
            saveData()

            true
        }
    }

    private fun saveData() {

        // Общие настройки позволят нам сохранить
        // и извлекать данные в виде пары ключ-значение.
        // В этой функции мы сохраним данные
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {

        // В этой функции мы будем извлекать данные
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)

        // Log.d используется для целей отладки
        Log.d("MainActivity", "$savedNumber")

        previousTotalSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Нам не нужно ничего писать в этой функции для этого приложения
    }
}