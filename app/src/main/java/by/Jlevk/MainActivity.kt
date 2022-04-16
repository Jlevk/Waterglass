package by.Jlevk

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.getColor
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import by.Jlevk.WaterApp.Companion.CHANNEL_ID
import by.Jlevk.databinding.ActivityMainBinding
import by.Jlevk.fragments.SettingsFragment
import by.Jlevk.fragments.WaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors.getColor
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {

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

    lateinit var dataHelper: DataHelper

    private val timer = Timer()

    private lateinit var binding: ActivityMainBinding

    var builder = NotificationCompat.Builder(this, CHANNEL_ID)
    var drink: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dataHelper = DataHelper(applicationContext)

        drink= findViewById(R.id.drink)
        drink?.setOnClickListener{ startStopAction() }

        if(dataHelper.timerCounting())
        {
            startTimer()
        }
        else
        {
            stopTimer()
            if(dataHelper.startTime() != null && dataHelper.stopTime() != null)
            {
                val time = Date().time - calcRestartTime().time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)

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

    }

    private fun not(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_water)
            .setContentTitle("Water Glass")
            .setContentText("Time to drink a little bit water!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

    }
    private inner class TimeTask: TimerTask()
    {
        override fun run()
        {
            if(dataHelper.timerCounting())
            {
                val time = Date().time - dataHelper.startTime()!!.time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }
    }


    private fun resetAction()
    {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timeTV.text = timeStringFromLong(0)
    }

    private fun stopTimer()
    {
        dataHelper.setTimerCounting(false)

    }

    private fun startTimer()
    {
        dataHelper.setTimerCounting(true)

    }

    private fun startStopAction()
    {
        if(dataHelper.timerCounting())
        {
            dataHelper.setStopTime(Date())
            stopTimer()
        }
        else
        {
            if(dataHelper.stopTime() != null)
            {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            }
            else
            {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun calcRestartTime(): Date
    {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        if (seconds.toInt() == 10) {
            not()
            notifyCall()
        }

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String
    {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
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
    fun notifyCall(){
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            val notificationId = 1
            notify(notificationId, builder.build())
        }
    }

}