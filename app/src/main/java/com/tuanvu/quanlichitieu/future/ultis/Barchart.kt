package com.tuanvu.quanlichitieu.future.ultis

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View


class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var width: Float = 0f
    private var height: Float = 0f

    data class DataChart(val income: Float = 0f, var spending: Float = 0f, var title: String = "")

    private var dataList: MutableList<DataChart> = mutableListOf()
        set(value) {
            field = value
        }

    fun submitData(data: MutableList<DataChart>) {
        listColumn.clear()
        this.dataList = data;
        initMaxMin(data)
        initColumnData(dataList)
        invalidate()
    }

    var listColumn = mutableListOf<Pair<RectF, RectF>>()
    var bottom = 0.85f
    private fun initColumnData(field: MutableList<DataChart>) {
        listColumn.clear()

        if (field.isEmpty()) return

        val startOffset = width * 0.1f
        val endOffset = width * 0.97f
        val totalWidth = endOffset - startOffset

        val spacingFactor = 0.02f
        val pairSpacing = totalWidth * spacingFactor
        val numberOfPairs = field.size - 1
        val totalSpacing = pairSpacing * numberOfPairs
        val barWidth = (totalWidth - totalSpacing) / (field.size * 2)
        val scaleFactor = ((height * 0.95f) * bottom / maxValue)

        field.forEachIndexed { index, dataChart ->
            val baseLeft = startOffset + index * (2 * barWidth + pairSpacing)
            val leftIncome = baseLeft
            val rightIncome = leftIncome + barWidth
            val barHeightIncome = dataChart.income * scaleFactor
            val incomeRect = RectF(leftIncome, height * bottom - barHeightIncome, rightIncome, height * bottom)
            val leftSpending = rightIncome
            val rightSpending = leftSpending + barWidth
            val barHeightSpending = dataChart.spending * scaleFactor
            val spendingRect = RectF(leftSpending, height * bottom - barHeightSpending, rightSpending, height * bottom)

            listColumn.add(Pair(incomeRect, spendingRect))
        }

        Log.d("BarChartView", "Initialized columns with spacing: $listColumn")
    }

    private var maxValue = 0f
    private var forValue = 0f
    private var threeValue = 0f
    private var secondValue = 0f
    private var minValue = 0f
    private fun initMaxMin(field: MutableList<DataChart>) {
        if (field.isNotEmpty()) {
            val maxIncome = field.maxOf { it.income }
            val maxSpend = field.maxOf { it.spending }
            maxValue = 200.000f//if (maxIncome > maxSpend) maxIncome else maxSpend
            secondValue = 40.000f//if (maxIncome > maxSpend) maxIncome else maxSpend
            threeValue = 80.000f//if (maxIncome > maxSpend) maxIncome else maxSpend
            forValue = 160.000f//if (maxIncome > maxSpend) maxIncome else maxSpend
        } else {
            maxValue = 0f
            secondValue = 0f
            threeValue = 0f
            forValue = 0f
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.width = w.toFloat()
        this.height = h.toFloat()
        Log.d("BarChartView", "Size changed: width=$width, height=$height")
        submitData(dataList)
        invalidate()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dataList.isEmpty()) return
        drawValues(canvas)
        drawGrid(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val paintText = Paint().apply {
            color = Color.BLACK
            textSize = dpToPx(14f)
            textAlign = Paint.Align.CENTER
        }
        val lineX = width * 0.08f
        val textX = lineX - dpToPx(12f)
        canvas.drawLine(
            lineX,
            height * 0.01f,
            lineX,
            height * bottom,
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.LTGRAY
                strokeWidth = dpToPx(1.5f)
            })
        canvas.drawLine(
            width*0.078f,
            (height * bottom)+dpToPx(1f),
            width*0.98f,
            (height * bottom)+dpToPx(1f),
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.LTGRAY
                strokeWidth = dpToPx(1.5f)
            })

        val textY1 = (height * bottom)
        val textY2 = (height * 0.05f)
        val textY3 = (height - (height / 3))
        val textY4 = (height - (height / 2))
        val textY5 = (height * 0.3f)

        canvas.drawText(minValue.toInt().toString(), textX, textY1, paintText)
        canvas.drawText(secondValue.toInt().toString(), textX, textY3, paintText)
        canvas.drawText(threeValue.toInt().toString(), textX, textY4, paintText)
        canvas.drawText(forValue.toInt().toString(), textX, textY5, paintText)
        canvas.drawText(maxValue.toInt().toString(), textX, textY2, paintText)
    }
    fun dpToPx(dp: Float): Float {
        val density = context.resources.displayMetrics.density
        return dp * density
    }

    private fun drawValues(canvas: Canvas) {
        if (listColumn.isEmpty()) return

        val paintIncome = Paint().apply {
            color = Color.GREEN
            style = Paint.Style.FILL
        }

        val paintSpending = Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
        }

        val paintText = Paint().apply {
            color = Color.BLACK
            textSize = dpToPx(14f)
            textAlign = Paint.Align.CENTER
        }

        listColumn.forEachIndexed { index, (incomeRect, spendingRect) ->
            canvas.drawRect(incomeRect, paintIncome)
            canvas.drawRect(spendingRect, paintSpending)
            val titleX = (incomeRect.left + spendingRect.right) / 2
            val titleY = height * bottom + dpToPx(20f)
            canvas.drawText(dataList[index].title, titleX, titleY, paintText)
        }
    }
}