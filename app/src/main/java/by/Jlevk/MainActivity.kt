package by.Jlevk

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import by.Jlevk.databinding.ActivityMainBinding
import by.Jlevk.fragments.SettingsFragment
import by.Jlevk.fragments.WaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val dataModel: DataModel by viewModels()

    private val waterFragment = WaterFragment()
    private val settingsFragment = SettingsFragment()

    var tvResult: TextView? = null

    var weight = 0
    var dayProgress = 0
    var dayDrinked = 0

    var pref: SharedPreferences? = null

private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, WaterFragment())
            .commit()

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_water -> replaceFragment(waterFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
            }
            true
        }

        pref = getSharedPreferences("TABLE", MODE_PRIVATE)
        dataModel.weightValue.observe(this) {

            weight = it
            binding.textView.text = weight.toString()
            saveData(weight)

        }
        dataModel.progress.observe(this) {

            dayDrinked= it
            binding.textView.text = weight.toString()
            saveWater(dayDrinked)

        }
        dataModel.percent.observe(this) {

            dayProgress = it
            binding.textView.text = weight.toString()
            savePercent(dayProgress)

        }

        weight=pref?.getInt("weight",0)!!
        dayDrinked=pref?.getInt("water",0)!!
        dayProgress=pref?.getInt("percent",0)!!

        dataModel.weightValue.value = weight
        dataModel.progress.value = dayDrinked
        dataModel.percent.value = dayProgress

        binding.textView.text = weight.toString()

    }

        /*
        private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    fun ClickAdd(view: View){
        //повышение счетчика
        weight++
        tvResult = findViewById(R.id.tvResult)
        tvResult?.text =  weight.toString()
        saveData(weight)

    }
         */
    fun next(view: View){

            dayDrinked = 0
            dayProgress = 0
            saveData(dayDrinked)
            saveData(dayProgress)
            dataModel.weightValue.value = weight
            dataModel.progress.value = dayDrinked
            dataModel.percent.value = dayProgress
    }

    fun saveData(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt("weight", res)
        editor?.apply()

    }
    fun saveWater(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt("water", res)
        editor?.apply()

    }
    fun savePercent(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt("percent", res)
        editor?.apply()

    }

        fun replaceFragment(fragment: Fragment) {
            //изменение экранов
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }



}