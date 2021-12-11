package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.SignUpFragmentBinding

class SignUpFragment : Fragment(R.layout.sign_up_fragment) {
    private lateinit var signUpFragmentBinding: SignUpFragmentBinding
    private var name: String = ""
    private var login: String = ""
    private var password: String = ""
    private var repeatPassword: String = ""
    private var isEdit: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpFragmentBinding = SignUpFragmentBinding.bind(view)
        actionSignUpButton()
    }

    private fun signUp() {
        with(signUpFragmentBinding) {
            name = loginSignUpEditText.text.toString()
            login = emailSignUp.text.toString()
            password = passwordSignUpEditText.text.toString()
            repeatPassword = passwordSignUpRepeatEditText.text.toString()
        }
        if (checkFillFields()) {
            //register user in system
            registerUser()
            //button action and go to map fragment
            val signInFragment = SignInFragment()
            parentFragmentManager
                    .beginTransaction()
                    .addToBackStack(signInFragment.toString())
                    .replace(R.id.auth_activity, signInFragment)
                    .commit()
        } else Toast.makeText(activity?.applicationContext, "Не все данные введены корректно", Toast.LENGTH_LONG).show()
    }

    private fun registerUser() {
        //add case into database
        val map: HashMap<String, Any> =
                hashMapOf(
                        "id" to 1,
                        "name" to name,
                        "login" to login,
                        "password" to password,
                )
        FirebaseAuth.getInstance().signOut()
        //проверка есть ли такой ребёнок в бд
        val rootRef = FirebaseDatabase.getInstance().reference.child("map").child("users")
                .child(login)
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(login).exists()) {
                    isEdit = true
                }
            }
        })
        if (!isEdit) {
            FirebaseDatabase.getInstance().reference.child("map").child("users")
                    .child(login).setValue(map)
            val toast: Toast = Toast.makeText(
                    activity?.applicationContext,
                    "Регистрация прошла усешно",
                    Toast.LENGTH_LONG
            )
            return toast.show()

        }
    }

    private fun checkFillFields(): Boolean {
        if (name.isEmpty()) {
            return false
        }
        if (login.isEmpty()) {
            return false
        }
        if (password.isEmpty()) {
            return false
        }
        if (repeatPassword.isEmpty()) {
            return false
        }
        return true
    }

    private fun actionSignUpButton() {
        signUpFragmentBinding.signUpButton.setOnClickListener {
            signUp()
        }
    }
}