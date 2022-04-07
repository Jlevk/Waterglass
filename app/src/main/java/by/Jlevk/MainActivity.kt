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
import android.widget.TextView
import androidx.activity.viewModels
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
    var dayDrinked = 0

    private var magnetic = FloatArray(9)
    private var gravity = FloatArray(9)

    private var accelerometer = FloatArray(3)
    private var magnfield = FloatArray(3)
    private var values = FloatArray(3)

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
            saveWater(dayDrinked)

        }
        dataModel.percent.observe(this) {

            dayProgress = it
            savePercent(dayProgress)

        }

        weight=pref?.getInt("weight",0)!!
        dayDrinked=pref?.getInt("water",0)!!
        dayProgress=pref?.getInt("percent",0)!!

        dataModel.weightValue.value = weight
        dataModel.progress.value = dayDrinked
        dataModel.percent.value = dayProgress

        val manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accel = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnet = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val sListener = object : SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {
                when(event?.sensor?.type){
                    Sensor.TYPE_ACCELEROMETER->accelerometer = event.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD->magnfield = event.values.clone()
                }
                SensorManager.getRotationMatrix(gravity, magnetic, accelerometer, magnfield)
                var outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem(gravity,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    outGravity
                )

                SensorManager.getOrientation(outGravity, values)

                progressBar = findViewById(R.id.water)
                var y = values[1] *  57.29577951308
                var z = values[0] *  57.29577951308
                var x = (values[2] * (-57.29577951308)).toFloat()
                if (y>76) x=0f
                if (x>-13 && x<13) {
                    binding.textView.text = x.toString()
                    progressBar?.rotation = x
                }
            }


            override fun onAccuracyChanged(event: Sensor?, p1: Int) {

            }
        }
        manager.registerListener(sListener, magnet, SensorManager.SENSOR_DELAY_FASTEST)
        manager.registerListener(sListener, accel, SensorManager.SENSOR_DELAY_FASTEST)

    }

    fun next(view: View){

        dayDrinked = 0
        dayProgress = 0
        saveWater(dayDrinked)
        savePercent(dayProgress)
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