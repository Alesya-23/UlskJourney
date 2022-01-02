package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.SignUpFragmentBinding
import com.ulskjourney.ulskjourney.model.database.UserStorage
import com.ulskjourney.ulskjourney.model.models.User

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
        } else Toast.makeText(activity?.applicationContext, "Не все данные введены корректно", Toast.LENGTH_SHORT).show()
    }

    private fun registerUser() {
        //add case into database
        var userStorage = activity?.applicationContext?.let { UserStorage(it) }
        userStorage?.open()
        val listUser = userStorage?.getFullList()
        if (listUser != null) {
            val userOur = listUser.find { it?.login == login && it.password == password }
            if (userOur != null) {
                isEdit = true
                Toast.makeText(activity?.applicationContext, "Пользователь уже есть в системе", Toast.LENGTH_SHORT).show()
            }
        }
        if (!isEdit) {
            userStorage?.insert(User(0, login, password, name))
            userStorage?.close()
            Toast.makeText(
                    activity?.applicationContext,
                    "Регистрация прошла усешно",
                    Toast.LENGTH_LONG
            ).show()
            requireActivity().supportFragmentManager.popBackStack()
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