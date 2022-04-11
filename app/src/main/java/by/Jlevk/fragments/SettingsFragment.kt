package by.Jlevk.fragments

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
import by.Jlevk.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private  val binding get() = _binding!!
    private val dataModel: DataModel by activityViewModels()

    var weight = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var weightValue: TextView = binding.weightValue
        var num: NumberPicker = binding.numberPicker

        num.minValue = 1
        num.maxValue = 400
        num.wrapSelectorWheel = false

        dataModel.weightValue.observe(activity as LifecycleOwner) {

            weight = it
            weightValue.text = "Your weight: $weight кг"
        }

        num.setOnValueChangedListener { picker, oldVal, newVal ->

            weight = newVal
            weightValue.text = "Your weight: $weight кг"

            dataModel.weightValue.value = weight

        }

    }
}


