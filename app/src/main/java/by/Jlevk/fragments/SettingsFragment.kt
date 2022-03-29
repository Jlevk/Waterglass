package by.Jlevk.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.Jlevk.DataModel
import by.Jlevk.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private  val binding get() = _binding!!

    private val dataModel: DataModel by activityViewModels()

    protected var weight = 0
    protected var dayWater = 0
    protected var dayProgress = 0
    protected var drinked = 0
    protected var dayDrinked = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        var weightValue: TextView = binding.weightValue
        var num: NumberPicker = binding.numberPicker

        weightValue.text = "Ваша масса: $weight кг"

        num.minValue = 0
        num.maxValue = 400
        num.wrapSelectorWheel = false

        num.setOnValueChangedListener { picker, oldVal, newVal ->

            weight = newVal
            weightValue.text = "Ваша масса: $weight кг"

            saveData(weight)
            dataModel.message.value = weight

        }
        dayWater = (weight/0.035).toInt()
        dayProgress = dayWater - dayDrinked


        var button: Button = binding.button
        button.setOnClickListener() {

            weightValue.text = "Ваша масса: $weight кг"

        }


    }

    private fun saveData(res: Int){
        //сохранение в файле
        val pref = requireContext().getSharedPreferences("TABLE", MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.putInt(" weight", res)
        editor?.apply()

    }


}


