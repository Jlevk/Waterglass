package by.Jlevk.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import by.Jlevk.DataModel
import by.Jlevk.R
import by.Jlevk.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private  val binding get() = _binding!!
    private val dataModel: DataModel by activityViewModels()

    var weight = 0
    var height = 0
    var index = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var weightValue: TextView = binding.weightValue
        var heightValue: TextView = binding.height
        var numWeight: NumberPicker = binding.numberPicker
        var numHeight: NumberPicker = binding.numberPicker2
        var showIndex: TextView = binding.index

        numWeight.minValue = 1
        numWeight.maxValue = 400
        numWeight.wrapSelectorWheel = false

        numHeight.minValue = 100
        numHeight.maxValue = 250
        numHeight.wrapSelectorWheel = false

        dataModel.weightValue.observe(activity as LifecycleOwner) {

            weight = it
            weightValue.text = "Your weight: $weight kg"
        }
        dataModel.heightValue.observe(activity as LifecycleOwner) {

            height = it
            heightValue.text = "Your height: $height cm"
        }
        dataModel.index.observe(activity as LifecycleOwner) {

            index = it.toDouble()
            showIndex.text = index.toInt().toString()
            when(index.toInt()){
                1-> showIndex.setTextColor(R.color.veryLow)
                16-> showIndex.setTextColor(R.color.low)
                19-> showIndex.setTextColor(R.color.normal)
                25-> showIndex.setTextColor(R.color.high)
                30-> showIndex.setTextColor(R.color.veryHigh)
                35-> showIndex.setTextColor(R.color.soHigh)
                40-> showIndex.setTextColor(R.color.stopEat)
            }

        }


        numWeight.setOnValueChangedListener { picker, oldVal, newVal ->

            weight = newVal
            weightValue.text = "Your weight: $weight kg"

            dataModel.weightValue.value = weight
            index = weight/((height*0.01)*(height*0.01))
            showIndex.text = index.toInt().toString()
            dataModel.index.value = index.toInt()

        }
        numHeight.setOnValueChangedListener { picker, oldVal, newVal ->

            height = newVal
            heightValue.text = "Your height: $height cm"

            dataModel.heightValue.value = height
            index = weight/((height*0.01)*(height*0.01))
            showIndex.text = index.toInt().toString()
            dataModel.index.value = index.toInt()

        }
    }
}


