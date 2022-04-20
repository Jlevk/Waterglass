package by.Jlevk.fragments

import android.animation.ObjectAnimator
import android.graphics.Color
import  android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import by.Jlevk.DataModel
import by.Jlevk.R
import by.Jlevk.databinding.FragmentWaterBinding
import com.google.android.material.snackbar.Snackbar

class WaterFragment : Fragment() {

    private var _binding: FragmentWaterBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("Binding call after view is destroyed")
    private val dataModel: DataModel by activityViewModels()

    var weight = 0
    var height = 0
    var dayDrunk = 0
    var dayProgress = 0
    var progressAnim = 0
    var glass = 250
    var dayWater = 0

    var progressBar: ProgressBar? = null
    var tip: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWaterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tip = binding.tip

        when((0..10).random()){
            1-> tip?.setText(R.string.tip1)
            2-> tip?.setText(R.string.tip2)
            3-> tip?.setText(R.string.tip3)
            4-> tip?.setText(R.string.tip4)
            5-> tip?.setText(R.string.tip5)
            6-> tip?.setText(R.string.tip6)
            7-> tip?.setText(R.string.tip7)
            8-> tip?.setText(R.string.tip8)
            9-> tip?.setText(R.string.tip9)
        }

        progressBar = binding.water
        var percent: TextView = binding.percent
        var water: TextView = binding.dayWater
        var button: Button = binding.drink
        var size: Button = binding.size

        dataModel.progress.observe(activity as LifecycleOwner) {

            dayDrunk= it
            water.text = "Drunk water: $dayDrunk ml"

        }
        dataModel.glass.observe(activity as LifecycleOwner) {

            glass= it

        }
        dataModel.percent.observe(activity as LifecycleOwner) {

            dayProgress = it
            percent.text = "Progress: $dayProgress %"

            var esterEgg: ImageView = binding.goal
            var line: ImageView = binding.line

            if(dayProgress>80){
                esterEgg.isVisible = true
                line.isVisible = true
            }
            else{
                esterEgg.isVisible = false
                line.isVisible = false
            }

            progressAnim = dayProgress*10
            progressBar?.max = 1000
            ObjectAnimator.ofInt(progressBar, "progress", progressAnim)
                .setDuration(2000)
                .start()
        }

        dataModel.weightValue.observe(activity as LifecycleOwner) {

            weight = it
        }


        button.setOnClickListener {
            if(glass==0)glass = 250
            if (weight == 0) onSNACK(view)
            else {

                dayWater = (weight * 31)

                if (dayDrunk == 0) {

                    dayDrunk = glass
                    dataModel.progress.value = dayDrunk
                } else {

                    dayDrunk += glass
                    dataModel.progress.value = dayDrunk
                }
                dayProgress = (dayDrunk * 100 / dayWater)
                dataModel.percent.value = dayProgress

                water.text = "Drunk water: $dayDrunk ml"
                percent.text = "Progress: $dayProgress %"
            }
        }
        size.setOnClickListener {
            createAlert()

        }


    }
    fun glass(view: View){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, "You should pick glass size",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.DKGRAY)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.WHITE)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLACK)
        textView.textSize = 14f
        snackbar.show()
    }
    fun onSNACK(view: View){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, "Please go to settings and pick your weight and height",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.DKGRAY)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.WHITE)
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLACK)
        textView.textSize = 14f
        snackbar.show()
    }

    private fun createAlert(){
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Enter a glass size")
        builder?.setMessage("Glass size")
        builder?.setNeutralButton(" return to standard (250 ml)") {dialogInterface, i ->
            glass = 250
            dataModel.glass.value = glass
        }
        builder?.setNegativeButton("100 ml") {dialogInterface, i ->
            glass = 100
            dataModel.glass.value = glass
        }
        builder?.setPositiveButton("300 ml") {dialogInterface, i ->
            glass = 300
            dataModel.glass.value = glass
        }
        builder?.show()
    }

}


