package by.Jlevk.fragments

import android.animation.ObjectAnimator
import  android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import by.Jlevk.DataModel
import by.Jlevk.databinding.FragmentWaterBinding

class WaterFragment : Fragment() {

    private var _binding: FragmentWaterBinding? = null
    private val binding get() = _binding ?: throw RuntimeException("Binding call after view is destroyed")
    private val dataModel: DataModel by activityViewModels()

    var weight = 0
    var dayDrunk = 0
    var dayProgress = 0
    var progressAnim = 0
    var glass = 250
    var dayWater = 0

    var progressBar: ProgressBar? = null

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

        progressBar = binding.water
        var percent: TextView = binding.percent
        var water: TextView = binding.dayWater
        var button: Button = binding.drink

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

            dayWater = (weight * 31)

            if (dayDrunk == 0) {

                dayDrunk = glass
                dataModel.progress.value = dayDrunk
            }
            else {

                dayDrunk += glass
                dataModel.progress.value = dayDrunk
            }
            dayProgress = (dayDrunk*100 / dayWater)
            dataModel.percent.value = dayProgress

            water.text = "Drunk water: $dayDrunk ml"
            percent.text = "Progress: $dayProgress %"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


