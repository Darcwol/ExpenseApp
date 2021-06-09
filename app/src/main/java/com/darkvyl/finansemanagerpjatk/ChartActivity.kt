package com.darkvyl.finansemanagerpjatk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.darkvyl.finansemanagerpjatk.databinding.ActivityChartBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class ChartActivity : AppCompatActivity() {
    private val binding by lazy { ActivityChartBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val entries = mutableListOf<Entry>()
        Shared.expanseList.sortBy { it.date }
        var i = 0
        Shared.expanseList.groupBy { it.date }.forEach { (_, expense) ->
            i++
            val daily = expense.sumByDouble { ex -> ex.cost }
            entries.add(Entry(i.toFloat(), daily.toFloat()))
        }
        val dataSet = LineDataSet(entries, "Label")
        val lineData = LineData(dataSet)
        binding.chart.data = lineData
        binding.chart.invalidate()
    }
}