package com.tuanvu.quanlichitieu.future.activity

import com.tuanvu.quanlichitieu.R
import com.tuanvu.quanlichitieu.base.BaseActivity
import com.tuanvu.quanlichitieu.databinding.ActivityStatiticsBinding
import com.tuanvu.quanlichitieu.future.ultis.ChartData


class StaticsActivity : BaseActivity<ActivityStatiticsBinding>() {
    override fun getViewBinding(): ActivityStatiticsBinding {
        return ActivityStatiticsBinding.inflate(layoutInflater)
    }

    override fun createView() {
        var data: MutableList<ChartData?> = ArrayList()
        data.add(ChartData("First", 35f)) //ARGS-> (display text, percentage)
        data.add(ChartData("Fourth", 65f))
        binding.pieChart.setChartData(data)
        binding.pieChart.setCenterCircleColor(resources.getColor(R.color.clr_primary_orange))
        binding.pieChart.setAboutTextSize(30f)

//chart data with specified colors

////chart data with specified colors
//        val simpleChart = findViewById<View>(R.id.simple_chart) as SimpleChart
//        data = ArrayList()
//
////ARGS-> (displayText, percentage, textColor, backgroundColor)
//
////ARGS-> (displayText, percentage, textColor, backgroundColor)
//        data.add(ChartData("First", 35f, Color.WHITE, Color.parseColor("#0091EA")))
//        data.add(ChartData("Second", 25f, Color.WHITE, Color.parseColor("#33691E")))
//        data.add(ChartData("Third", 30f, Color.DKGRAY, Color.parseColor("#F57F17")))
//        data.add(ChartData("Fourth", 10f, Color.DKGRAY, Color.parseColor("#FFD600")))
//        simpleChart.chartData = data
    }
}