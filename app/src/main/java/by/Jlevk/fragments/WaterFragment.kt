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
    var dayDrinked = 0
    var dayProgress = 0
    var progressAnim = 0
    var glass = 250

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

            dayDrinked= it
            water.text = "Drunk water: $dayDrinked ml"

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
            var dayWater = (weight * 31)

            if (dayDrinked == 0) {

                dayDrinked = glass
                dataModel.progress.value = dayDrinked
            }
            else {

                dayDrinked += glass
                dataModel.progress.value = dayDrinked
            }
                dayProgress = (dayDrinked*100 / dayWater)
                dataModel.percent.value = dayProgress

                water.text = "Drunk water: $dayDrinked ml"
                percent.text = "Progress: $dayProgress %"

        }
        //добавить визуализацию прогресса
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




