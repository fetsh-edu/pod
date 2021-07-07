package me.fetsh.geekbrains.pod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.fetsh.geekbrains.pod.ui.giphy.GiphyFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GiphyFragment.newInstance())
                .commitNow()
        }
    }
}