package by.Jlevk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import by.Jlevk.fragments.SettingsFragment
import by.Jlevk.fragments.WaterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val waterFragment = WaterFragment()
    private val settingsFragment = SettingsFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(settingsFragment)

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.ic_water -> replaceFragment(waterFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment){
        if(fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}