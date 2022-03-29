package by.Jlevk.fragments

import  android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import by.Jlevk.DataModel
import by.Jlevk.databinding.FragmentWaterBinding

class WaterFragment : Fragment(){

    private var _binding: FragmentWaterBinding? = null
    private  val binding get() = _binding!!
    private val dataModel: DataModel by activityViewModels()

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
        dataModel.message.observe(activity as LifecycleOwner) {
            binding.dayWater.text = it.toString()
        }

    }


}


