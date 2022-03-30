package by.Jlevk.fragments

import android.content.Context
import android.content.SharedPreferences
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

class WaterFragment : Fragment(){

    private var _binding: FragmentWaterBinding? = null
    private  val binding get() = _binding!!
    private val dataModel: DataModel by activityViewModels()

    var weight = 0

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

        dataModel.weightValue.observe(activity as LifecycleOwner) {

            weight = it
            binding.dayWater.text = weight.toString()

        }

        var percent: TextView = binding.percent
        var weightValue: TextView = binding.dayWater

        var dayProgress = 0
        var dayDrinked = 0

        weight = 45

        var dayWater = (weight*35)

        var button: Button = binding.drink
        button.setOnClickListener() {

            if(dayDrinked == 0 )
                dayDrinked = 250

            else{
                dayDrinked += 250}

            dayProgress = (dayDrinked/dayWater)*100

            weightValue.text = "Выпито: $dayDrinked мл"

            percent.text = "Прогресс: $dayProgress %"

        }

    }

}


