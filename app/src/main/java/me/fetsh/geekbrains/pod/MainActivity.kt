package me.fetsh.geekbrains.pod

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import me.fetsh.geekbrains.pod.databinding.MainActivityBinding
import kotlin.random.Random


const val PREF_THEME = "Pref theme"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: MainActivityBinding
    private var currentTheme: Int = R.style.Theme_Pod_Default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(pickedThemeFromPreferences())

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_giphy_random, R.id.nav_about
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val menu = navView.menu
        val menuItem = menu.findItem(R.id.nav_change_theme)
        val actionView = menuItem.actionView
        actionView.setOnClickListener {
            getSharedPreferences(applicationContext.packageName, Context.MODE_PRIVATE)
                .edit()
                .putInt(PREF_THEME, getRandomTheme())
                .apply()
            recreate()
        }
    }

    private fun getRandomTheme(): Int {
        val themes = listOf(
            R.style.Theme_Pod_Default, R.style.Theme_Pod_Green, R.style.Theme_Pod_Orange, R.style.Theme_Pod_Blue
        )
        var randomTheme = themes[Random.nextInt(0, themes.size)]
        while (randomTheme == currentTheme) {
            randomTheme = themes[Random.nextInt(0, themes.size)]
        }
        return randomTheme
    }

    private fun pickedThemeFromPreferences(): Int {
        currentTheme = getSharedPreferences(applicationContext.packageName, Context.MODE_PRIVATE)
            .getInt(PREF_THEME, R.style.Theme_Pod_Default)
        return currentTheme
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
fun Activity.closeKeyboard() {
    currentFocus?.let {
        val manager = it.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}