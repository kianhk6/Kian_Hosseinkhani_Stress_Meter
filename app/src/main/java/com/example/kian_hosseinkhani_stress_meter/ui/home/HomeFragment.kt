package com.example.kian_hosseinkhani_stress_meter.ui.home

import ImageAdapter
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.kian_hosseinkhani_stress_meter.R
import com.example.kian_hosseinkhani_stress_meter.R.drawable
import com.example.kian_hosseinkhani_stress_meter.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Counter to track the current set of images
    private var currentSet = 0

    // Three sets of images
    private val imageSets = arrayOf(
        intArrayOf(
            drawable.zero1, drawable.one1, drawable.two1, drawable.three1, drawable.four1,
            drawable.five1, drawable.six1, drawable.seven, drawable.eight1, drawable.nine1,
            drawable.ten1, drawable.eleven1, drawable.tweleve1, drawable.thirteen1, drawable.fourteen1,
            drawable.fifteen1
        ),
        intArrayOf(
            drawable.zero2, drawable.one2, drawable.two2, drawable.three2, drawable.four2,
            drawable.five2, drawable.six2, drawable.seven2, drawable.eight2, drawable.nine2,
            drawable.ten2, drawable.eleven2, drawable.tweleve2, drawable.thirteen2, drawable.fourteen2,
            drawable.fifteen2
        ),
        intArrayOf(
            drawable.zero3, drawable.one3, drawable.two3, drawable.three4, drawable.four3,
            drawable.five3, drawable.six3, drawable.seven3, drawable.eight3, drawable.nine3,
            drawable.ten3, drawable.eleven3, drawable.tweleve3, drawable.thirteen3, drawable.fourteen3,
            drawable.fifteen3
        )

    )
    companion object {
        // variable to track if we entering home for the first time
        // this is for the vibrator logic
        var isFirstOpen = true
    }

    private var vibrator: Vibrator? = null
    private var isVibrating = false

    private var mediaPlayer: MediaPlayer? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 200, 100)

        // Check if the device has a vibrator
        if(isFirstOpen){
            if (vibrator!!.hasVibrator()) {
                vibrator!!.vibrate(pattern, 1)
                isVibrating = true

                // Initialize and start MediaPlayer
                mediaPlayer = MediaPlayer.create(context, R.raw.sound)
                mediaPlayer?.isLooping = true // To loop the sound like the vibration
                mediaPlayer?.start()
            }
        }

        val moreImagesButton: Button = binding.moreImagesButton
        val gridView: GridView = binding.gridView

        // Initialize with the first set of images
        val adapter = ImageAdapter(requireContext(), imageSets[currentSet])
        gridView.adapter = adapter

        moreImagesButton.setOnClickListener {
            // Update the current set index
            currentSet = (currentSet + 1) % imageSets.size

            // Update the adapter with the new set of images
            (gridView.adapter as ImageAdapter).updateImages(imageSets[currentSet])
        }

        gridView.setOnItemClickListener { _, _, position, _ ->
            val imageResource = imageSets[currentSet][position]
            val dialogFragment = ImageDialogFragment(imageResource, position)
            dialogFragment.show(childFragmentManager, "ImageDialog")

            // Stop vibration once an image is selected
            if (isVibrating) {
                isFirstOpen = false
                vibrator!!.cancel()
                isVibrating = false

                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }

        return root
    }
    override fun onPause() {
        super.onPause()
        isFirstOpen = false

        if (vibrator != null) {
            vibrator!!.cancel()
        }

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
