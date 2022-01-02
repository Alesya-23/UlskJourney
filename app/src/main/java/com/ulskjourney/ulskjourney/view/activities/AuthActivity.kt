package com.ulskjourney.ulskjourney.view.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.model.firebase.MarkFirebase
import com.ulskjourney.ulskjourney.model.firebase.UserFirebase
import com.ulskjourney.ulskjourney.utils.FirebasePostService
import com.ulskjourney.ulskjourney.view.fragments.MapFragment
import com.ulskjourney.ulskjourney.view.fragments.SignInFragment

class AuthActivity : AppCompatActivity() {
    private var mService: FirebasePostService = FirebasePostService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        if (savedInstanceState == null) {
            var signInFragment = SignInFragment()
            supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.auth_activity, signInFragment).commit()
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to Service
        mService = mService.getBinder().getService()
        Intent(this, FirebasePostService::class.java).also { intent ->
            bindService(intent, mService.getConnection(), Context.BIND_AUTO_CREATE)
        }
        startService(Intent(this, FirebasePostService::class.java))
    }

    override fun onStop() {
        super.onStop()
        unbindService(mService.getConnection())
        mService.unBind()
    }

    fun getFirebasePostService(): FirebasePostService {
        return mService
    }

    override fun onDestroy() {
        super.onDestroy()
        val userFirebaseLogic: UserFirebase = UserFirebase()
        userFirebaseLogic.syncUsers(this)

        val markFirebaseLogic: MarkFirebase = MarkFirebase()
        markFirebaseLogic.syncUsers(this)
    }
}