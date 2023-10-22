package com.example.kian_hosseinkhani_stress_meter.ui.home

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.kian_hosseinkhani_stress_meter.R
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageDialogFragment(private val imageRes: Int, private val stressLevel: Int) : DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.image_dialog, container, false)

        val imageView: ImageView = view.findViewById(R.id.dialogImageView)
        imageView.setImageResource(imageRes)

        val cancelButton: Button = view.findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener { dismiss() }

        val submitButton: Button = view.findViewById(R.id.btnSubmit)
        // Handle submission here, for now, we'll just dismiss the dialog
        submitButton.setOnClickListener {
            saveToCSV()
            requireActivity().finish()
        }

        return view
    }
    private fun saveToCSV() {
        // Readable timestamp format
        val readableTimeStamp = SimpleDateFormat("yyyy/MM/dd, HH:mm:ss", Locale.getDefault()).format(Date())
        // Numeric timestamp format for time comparisons
        val numericTimeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

        val stressData = "$readableTimeStamp, $numericTimeStamp, $stressLevel\n"


        // Save to the public "Download" directory: to easily access it for debugging
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (!directory.exists()) {
            directory.mkdirs()  // use mkdirs() instead of mkdir() to create any necessary parent directories
        }
        if(directory.exists()){
            println(directory.absolutePath)
        }

        val file = File(directory, "stress_timestamp.csv")
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            if(file.exists()){
                println(file.absolutePath)
            }
            val writer = FileWriter(file, true)
            writer.append(stressData)
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialogStyle
    }
}
