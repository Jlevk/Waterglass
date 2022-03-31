package by.Jlevk.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.Jlevk.DataModel
import by.Jlevk.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    var pref: SharedPreferences? = null

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

        weightValue.text = "Ваша масса: $weight кг"

        num.minValue = 0
        num.maxValue = 400
        num.wrapSelectorWheel = false

        var pref : SharedPreferences?= activity?.getPreferences(MODE_PRIVATE)

        weight=pref?.getInt("weight", 0)!!

        num.setOnValueChangedListener { picker, oldVal, newVal ->

            weight = newVal
            weightValue.text = "Ваша масса: $weight кг"

            dataModel.weightValue.value = weight

        }
        saveData(weight)

    }

    fun saveData(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt("weight", res)
        editor?.apply()

    }

}


