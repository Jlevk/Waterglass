package by.Jlevk.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import  android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import by.Jlevk.R
import by.Jlevk.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private  val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

        val pref = requireContext().getSharedPreferences("TABLE", MODE_PRIVATE)
        var tvResult: TextView? = null
        var weight = 0

        var button = binding.button

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var text: TextView? = binding.tvResult
        var num: NumberPicker = binding.numberPicker

        num.minValue = 0
        num.maxValue = 400
        num.wrapSelectorWheel = false

        num.setOnValueChangedListener { picker, oldVal, newVal ->
            text?.text = "Выбранное значение: $newVal"


        }


    }


}
