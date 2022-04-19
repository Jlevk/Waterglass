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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.*
import by.Jlevk.databinding.ActivityMainBinding
import by.Jlevk.fragments.SettingsFragment
import by.Jlevk.fragments.WaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity: AppCompatActivity()
{
    private val dataModel: DataModel by viewModels()
    private val waterFragment = WaterFragment()
    private val settingsFragment = SettingsFragment()
    var pref: SharedPreferences? = null
    var progressBar: ProgressBar? = null

    var weight = 0
    var height = 0
    var index = 0
    var dayProgress = 0
    var dayDrunk = 0
    var glass = 250

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
        myWorkManager()

        pref = getSharedPreferences("TABLE", MODE_PRIVATE)
        dataModel.weightValue.observe(this) {

            weight = it
            saveWeight(weight)

        }
        dataModel.heightValue.observe(this) {

            height = it
            saveHeight(height)

        }
        dataModel.index.observe(this) {

            index = it
            saveIndex(index)

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
        height=pref?.getInt("height",0)!!
        index=pref?.getInt("index",0)!!
        dayDrunk=pref?.getInt("water",0)!!
        dayProgress=pref?.getInt("percent",0)!!
        glass = if(glass==0)
            pref?.getInt("glass",0)!!
        else 250


        dataModel.weightValue.value = weight
        dataModel.heightValue.value = height
        dataModel.index.value = index
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
        //creating a plan for a future notifications
        myWorkManager()


    }

    private fun myWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

        val myRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            60,
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        //minimum interval is 15min

        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                "my_id",
                ExistingPeriodicWorkPolicy.KEEP,
                myRequest
            )
    }


    fun next(view: View){

        dayDrunk = 0
        dayProgress = 0
        saveWater(dayDrunk)
        savePercent(dayProgress)
        dataModel.progress.value = dayDrunk
        dataModel.percent.value = dayProgress
    }

    fun saveWeight(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt("weight", res)
        editor?.apply()

    }
    fun saveHeight(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt("height", res)
        editor?.apply()

    }
    fun saveIndex(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt("index", res)
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















