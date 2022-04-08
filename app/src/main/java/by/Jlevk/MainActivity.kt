package by.Jlevk

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.Jlevk.databinding.ActivityMainBinding
import by.Jlevk.fragments.SettingsFragment
import by.Jlevk.fragments.WaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val dataModel: DataModel by viewModels()
    private val waterFragment = WaterFragment()
    private val settingsFragment = SettingsFragment()
    var pref: SharedPreferences? = null
    var progressBar: ProgressBar? = null

    var weight = 0
    var dayProgress = 0
    var dayDrunk = 0
    var glass = 0

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

            dayDrunk= it
            saveWater(dayDrunk)

        }
        dataModel.percent.observe(this) {

            dayProgress = it
            savePercent(dayProgress)

        }
        dataModel.glass.observe(this) {

            glass = it
            saveGlass(glass)

        }

        weight=pref?.getInt("weight",0)!!
        dayDrunk=pref?.getInt("water",0)!!
        dayProgress=pref?.getInt("percent",0)!!
        glass=pref?.getInt("glass",0)!!

        dataModel.weightValue.value = weight
        dataModel.progress.value = dayDrunk
        dataModel.percent.value = dayProgress
        dataModel.glass.value = glass

        val manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val sListener = object : SensorEventListener {
            override fun onSensorChanged(sEvent: SensorEvent?) {
                val value = sEvent?.values
                progressBar = findViewById(R.id.water)
                var s = value?.get(0)
                if(value?.get(2)!! <-30 ) s = 10f
                if (s != null && s>-15 && s<25) {
                    progressBar?.rotation = (-s+10)
                }
            }
            override fun onAccuracyChanged(sEvent: Sensor?, p1: Int) {

            }
        }
        manager.registerListener(sListener, sensor, SensorManager.SENSOR_DELAY_GAME)
    }
    private fun createAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter a glass size")
        builder.setMessage("Glass size")
        builder.setNeutralButton("100 ml") {dialogInterface, i ->
            glass = 100
            saveGlass(glass)
            dataModel.glass.value = glass
        }
        builder.setNegativeButton("250 ml") {dialogInterface, i ->
            glass = 250
            saveGlass(glass)
            dataModel.glass.value = glass
        }
        builder.setPositiveButton("300 ml") {dialogInterface, i ->
            glass = 300
            saveGlass(glass)
            dataModel.glass.value = glass
        }
        builder.show()
    }

    fun size(view: View){
        createAlert()
    }

    fun next(view: View){

        dayDrunk = 0
        dayProgress = 0
        saveWater(dayDrunk)
        savePercent(dayProgress)
        dataModel.progress.value = dayDrunk
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

    fun saveGlass(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt("glass", res)
        editor?.apply()

    }

    fun replaceFragment(fragment: Fragment) {
        //изменение экранов
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

}