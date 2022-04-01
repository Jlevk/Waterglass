package by.Jlevk.fragments

import  android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
    var dayProgress = 0
    var dayDrinked = 0

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

        var percent: TextView = binding.percent
        var water: TextView = binding.dayWater
        var button: Button = binding.drink

        dataModel.progress.observe(activity as LifecycleOwner) {

            dayDrinked= it
            water.text = "Выпито: $dayDrinked мл"

        }
        dataModel.percent.observe(activity as LifecycleOwner) {

            dayProgress = it
            percent.text = "Прогресс: $dayProgress %"

        }
        dataModel.weightValue.observe(activity as LifecycleOwner) {

            weight = it
        }

        button.setOnClickListener {
                var dayWater = (weight * 35)

                if (dayDrinked == 0) {
                    dayDrinked = 250
                    dataModel.progress.value = dayDrinked
                } else {
                    dayDrinked += 250
                    dataModel.progress.value = dayDrinked
                }

                dayProgress = (dayDrinked / dayWater) * 100
                dataModel.percent.value = dayProgress

                water.text = "Выпито: $dayDrinked мл"

                percent.text = "Прогресс: $dayProgress %"

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




