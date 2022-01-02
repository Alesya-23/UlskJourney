package com.ulskjourney.ulskjourney

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.ulskjourney.ulskjourney.view.activities.AuthActivity
import com.ulskjourney.ulskjourney.view.fragments.MapFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
            val intent: Intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }, 3000)

    }
}