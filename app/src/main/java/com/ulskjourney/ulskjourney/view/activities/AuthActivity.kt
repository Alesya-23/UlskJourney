package com.ulskjourney.ulskjourney.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.view.fragments.MapFragment
import com.ulskjourney.ulskjourney.view.fragments.SignInFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        if (savedInstanceState == null) {
            var signInFragment = SignInFragment()
            supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.auth_activity, signInFragment).commit()
//            val mapFragment = MapFragment()
//            supportFragmentManager
//                .beginTransaction()
//                .addToBackStack(mapFragment.toString())
//                .replace(R.id.auth_activity, mapFragment)
//                .commit()
        }

    }
}