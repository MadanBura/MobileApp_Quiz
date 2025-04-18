package com.ex.quizapplication.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ex.quizapplication.R
import com.ex.quizapplication.databinding.FragmentTotalResultBinding
import com.ex.quizapplication.model.PreviousResult
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class TotalResultFragment : Fragment() {

    private lateinit var binding : FragmentTotalResultBinding
    private var previousResultList = java.util.ArrayList<PreviousResult>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            previousResultList = it.getParcelableArrayList("previousResult") ?: arrayListOf()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTotalResultBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPieChart(previousResultList, binding.pieChart)
        setupBarChart(previousResultList, binding.barChart)
        setupLineChart(previousResultList, binding.lineChart)
    }

    fun setupPieChart(results: List<PreviousResult>, pieChart: PieChart) {
        val avgScoresByQuiz = results.groupBy { it.quizName }
            .mapValues { entry ->
                val scores = entry.value.map { it.score }
                scores.average().toFloat()
            }

        val entries = ArrayList<PieEntry>()
        avgScoresByQuiz.forEach { (quizName, avgScore) ->
            entries.add(PieEntry(avgScore, quizName)) // ✅ Keep label for legend
        }

        val dataSet = PieDataSet(entries, "Average Score Per Quiz")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 14f

        val data = PieData(dataSet)
        data.setValueTextSize(14f)
        data.setValueTextColor(android.graphics.Color.BLACK)

        pieChart.data = data
        pieChart.setDrawEntryLabels(false) // ✅ This hides labels on slices only
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = true   // ✅ Ensure legend is enabled
        pieChart.animateY(1000)

        val legend = pieChart.legend
        legend.isEnabled = true
        legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)


        pieChart.invalidate()
    }



    fun setupBarChart(results: List<PreviousResult>, barChart: BarChart) {
        val avgScoresByQuiz = results.groupBy { it.quizName }
            .mapValues { entry ->
                val scores = entry.value.map { it.score }
                scores.average().toFloat()
            }

        val entries = ArrayList<BarEntry>()
        val labels = mutableListOf<String>()

        avgScoresByQuiz.entries.forEachIndexed { index, entry ->
            entries.add(BarEntry(index.toFloat(), entry.value))
            labels.add(entry.key)
        }

        val dataSet = BarDataSet(entries, "Average Score")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.valueTextSize = 12f

        val data = BarData(dataSet)
        barChart.data = data
        barChart.setFitBars(true) // Important to make sure the bars are properly sized
        barChart.description.isEnabled = false
        barChart.animateY(1000)

        // X-Axis Configuration
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -90f // Rotate the X-axis labels for better visibility if needed

        // Y-Axis Configuration
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.axisMinimum = 0f

        // **Legend Configuration** (Vertical and outside the chart area)
        val legend = barChart.legend
        legend.isEnabled = true
        legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false) // Ensure it's outside the chart area

        val legendEntries = avgScoresByQuiz.entries.mapIndexed { index, entry ->
            com.github.mikephil.charting.components.LegendEntry(
                entry.key, // The name of the quiz
                com.github.mikephil.charting.components.Legend.LegendForm.SQUARE, // The shape of the legend entry (square)
                10f, // The size of the square
                2f,  // The border width of the square (not really needed for a square)
                null, // No dash effect (set to null)
                com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS[index % ColorTemplate.COLORFUL_COLORS.size] // The color of the square
            ) // Customize the size of the legend
        }
        legend.setCustom(legendEntries)
        barChart.invalidate() // Refresh the chart
    }

    fun setupLineChart(results: List<PreviousResult>, lineChart: LineChart) {
        // Grouping by quizName and calculating the average score for each quiz
        val avgScoresByQuiz = results.groupBy { it.quizName }
            .mapValues { entry ->
                val scores = entry.value.map { it.score }
                scores.average().toFloat()
            }

        val entries = ArrayList<Entry>()
        val labels = mutableListOf<String>()

        // Creating line chart entries for each quiz's average score
        avgScoresByQuiz.entries.forEachIndexed { index, entry ->
            entries.add(Entry(index.toFloat(), entry.value))
            labels.add(entry.key)
        }

        // Create a DataSet for LineChart
        val dataSet = LineDataSet(entries, "Average Score")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList() // Set colors for lines
        dataSet.valueTextSize = 12f // Text size for the average score value
        dataSet.setDrawValues(true) // Draw values on the line graph
        dataSet.setDrawCircles(true) // Draw circles on data points
        dataSet.setCircleColor(ColorTemplate.COLORFUL_COLORS[0]) // Circle color

        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.setTouchEnabled(true) // Enable touch interaction
        lineChart.description.isEnabled = false // Hide the description

        // X-Axis Configuration
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -90f

        // Y-Axis Configuration
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.axisMinimum = 0f

        // **Legend Configuration**
        val legend = lineChart.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false) // Ensure it's outside the chart area

        // Set the custom legend entries with quiz names and corresponding colors
        val legendEntries = avgScoresByQuiz.entries.mapIndexed { index, entry ->
            com.github.mikephil.charting.components.LegendEntry(
                entry.key, // The name of the quiz
                Legend.LegendForm.LINE, // The shape of the legend entry (line)
                10f, // The size of the line
                2f,  // The border width of the line (optional)
                null, // No dash effect (set to null)
                ColorTemplate.COLORFUL_COLORS[index % ColorTemplate.COLORFUL_COLORS.size] // Color for each line
            )
        }
        legend.setCustom(legendEntries)

        // Refresh the chart
        lineChart.invalidate() // Redraw the chart to show the changes
    }

}