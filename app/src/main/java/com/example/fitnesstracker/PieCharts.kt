package com.example.fitnesstracker

import android.content.Intent
import android.graphics.Color
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class PieCharts : AppCompatActivity() {

    var tvSteps: TextView? = null
    var tvKcal: TextView? = null
    var tvDistance: TextView? = null
    var pieChart: PieChart? = null
    var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_charts)
        tvSteps = findViewById(R.id.tvSteps)
        tvKcal = findViewById(R.id.tvKcal)
        tvDistance = findViewById(R.id.tvDistance)
        pieChart = findViewById(R.id.piechart)
        setData()

        val mButton = findViewById<ImageView>(R.id.imageViewKcall3) as ImageView
        mButton.setOnClickListener {
            startActivity(Intent(this@PieCharts, MainActivity2::class.java))
            finish()
        }
        val sButton = findViewById<ImageView>(R.id.imageViewDistance4) as ImageView
        sButton.setOnClickListener {
            startActivity(Intent(this@PieCharts, MainActivity3::class.java))
            finish()
        }
        val tButton = findViewById<ImageView>(R.id.imageViewChronometr5) as ImageView
        tButton.setOnClickListener {
            startActivity(Intent(this@PieCharts, MainActivity4::class.java))
            finish()
        }
        val iButton = findViewById<ImageView>(R.id.imageViewSteps3) as ImageView
        iButton.setOnClickListener {
            startActivity(Intent(this@PieCharts, MainActivity::class.java))
            finish()
        }
        val kButton = findViewById<ImageView>(R.id.imageViewbmi6) as ImageView
        kButton.setOnClickListener {
            startActivity(Intent(this@PieCharts, MainActivity5::class.java))
            finish()
        }
    }

    private fun setData() {

        val arguments = intent.extras
        val steps = arguments!!["steps"].toString()
        val kcal = arguments!!["steps"].toString()
        val distance = arguments!!["steps"].toString()

        val kcalInt = kcal.toInt() * 0.05
        val distanceInt = distance.toInt() * 0.0007
        val kcalfloat = kcalInt.toFloat()
        val distancefloat = distanceInt.toFloat()


        tvSteps!!.text = (steps)
        tvKcal!!.text = (String.format("%.2f", kcalInt))
        tvDistance!!.text = (String.format("%.2f", distanceInt))

        pieChart!!.addPieSlice(
            PieModel(
                "Шаги", tvSteps!!.text.toString().toInt().toFloat(),
                Color.parseColor("#FFA726")
            )
        )
        pieChart!!.addPieSlice(
            PieModel(
                "Калории", kcalfloat,
                Color.parseColor("#66BB6A")
            )
        )
        pieChart!!.addPieSlice(
            PieModel(
                "Расстояние", distancefloat,
                Color.parseColor("#EF5350")
            )
        )
        pieChart!!.startAnimation()
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
