package com.ulskjourney.ulskjourney.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.view.fragments.MapFragment

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (savedInstanceState == null) {
            val mapFragment = MapFragment()
            supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(mapFragment.toString())
                    .replace(R.id.auth_activity, mapFragment)
                    .commit()
        }
    }
}