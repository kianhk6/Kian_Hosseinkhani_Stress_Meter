package com.example.kian_hosseinkhani_stress_meter.ui.visualization

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kian_hosseinkhani_stress_meter.databinding.FragmentVisualizationBinding
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import java.io.File
import android.os.Environment
import android.graphics.Color
import android.util.TypedValue
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.Axis


class VisualizationFragment : Fragment() {

    private var _binding: FragmentVisualizationBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisualizationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Ensure you have permissions for reading external storage.
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(directory, "stress_timestamp.csv")

        if(file.exists()) {
            val lines = file.readLines()
            val values = mutableListOf<PointValue>()
            for((index, line) in lines.withIndex()) {
                val parts = line.split(",")
                val date = parts[0]
                val time = parts[1]
                val stress = parts[3].toFloat()

                values.add(PointValue(index.toFloat(), stress))

                // add data to table
                val row = TableRow(context)

                val indexCell = TextView(context)
                val dateCell = TextView(context)
                val timeCell = TextView(context)
                val stressCell = TextView(context)

                // +1 to start index from 1
                indexCell.text = (index + 1).toString()
                dateCell.text = date
                timeCell.text = time
                stressCell.text = stress.toString()

                // Apply padding to cells for uniform appearance
                val cellPadding = 5.toDp(context)
                indexCell.setPadding(cellPadding, cellPadding, cellPadding, cellPadding)
                dateCell.setPadding(cellPadding, cellPadding, cellPadding, cellPadding)
                timeCell.setPadding(cellPadding, cellPadding, cellPadding, cellPadding)
                stressCell.setPadding(cellPadding, cellPadding, cellPadding, cellPadding)

                row.addView(indexCell)
                row.addView(dateCell)
                row.addView(timeCell)
                row.addView(stressCell)

                binding.dataTable.addView(row)

                // Inserting divider line
                val divider = View(context)
                divider.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1)
                divider.setBackgroundColor(Color.BLACK)
                binding.dataTable.addView(divider)

            }

            val line = Line(values)
                .setColor(Color.BLUE)
                .setCubic(true)
                .setFilled(true)

            val data = LineChartData()
            data.lines = listOf(line)

            val axisX = Axis().setName("Instances").setMaxLabelChars(10)
            val axisY = Axis().setName("Stress Level")
            data.axisXBottom = axisX
            data.axisYLeft = axisY

            // set up the chart
            binding.chart.isZoomEnabled = true
            binding.chart.isScrollEnabled = true
            binding.chart.zoomType = ZoomType.HORIZONTAL_AND_VERTICAL
            binding.chart.isInteractive = true
            binding.chart.lineChartData = data
        }

        return root
    }

    fun Int.toDp(context: Context?): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context?.resources?.displayMetrics
        ).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
