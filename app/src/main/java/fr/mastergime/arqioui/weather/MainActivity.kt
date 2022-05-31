package fr.mastergime.arqioui.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.mastergime.arqioui.weather.databinding.ActivityMainBinding
import fr.mastergime.arqioui.weather.fragments.CurrentWeatherLocationFragment
import fr.mastergime.arqioui.weather.fragments.SearchCityWeatherFragment


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(CurrentWeatherLocationFragment())
        binding.bottomNavigationView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.current_weather -> {
                    loadFragment(CurrentWeatherLocationFragment())
                }
                R.id.search_city -> {
                    loadFragment(SearchCityWeatherFragment())
                }
            }
        }

    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}