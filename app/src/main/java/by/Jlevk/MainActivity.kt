package by.Jlevk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.Jlevk.fragments.SettingsFragment
import by.Jlevk.fragments.WaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val waterFragment = WaterFragment()
    private val settingsFragment = SettingsFragment()
/*
    var weight = 0

    var pref: SharedPreferences? = null
    var tvResult: TextView? = null
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

/*
        pref = getSharedPreferences("TABLE", MODE_PRIVATE)
        weight=pref?.getInt(" weight",0)!!
        tvResult = findViewById(R.id.tvResult)
        tvResult?.text =  weight.toString(

 */

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

    fun saveData(res: Int){
        //сохранение в файле
        val editor = pref?.edit()
        editor?.putInt(" weight", res)
        editor?.apply()

    }

    fun deleteAll(){
        val editor = pref?.edit()
        editor?.clear()
        editor?.apply()
        weight=0
        tvResult = findViewById(R.id.tvResult)
        tvResult?.text =  weight.toString()

    }

    fun ClickClear(view: View){
        deleteAll()

    }

     */

    fun replaceFragment(fragment: Fragment) {
        //изменение экранов
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

}