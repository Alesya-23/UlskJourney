package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.SignInFragmentBinding
import com.ulskjourney.ulskjourney.model.database.UserStorage
import com.ulskjourney.ulskjourney.model.models.User
import com.ulskjourney.ulskjourney.viewModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInFragment : Fragment(R.layout.sign_in_fragment) {
    private lateinit var signInFragmentBinding: SignInFragmentBinding
    private var login: String = ""
    private var password: String = ""
    private var isUserExist: Boolean = false
    private var isDataInCorrect: Boolean = false
    private lateinit var user: User
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInFragmentBinding = SignInFragmentBinding.bind(view)
        actionButtons()
        FirebaseAuth.getInstance().signOut()
    }

    private fun signIn() {
        val check = checkUser()
        if (check) {
            Toast.makeText(
                    activity?.applicationContext,
                    "Вход",
                    Toast.LENGTH_SHORT
            ).show()
            userViewModel.setIdUser(user.id)
            val mapFragment = MapFragment()
            parentFragmentManager
                    .beginTransaction()
                    .addToBackStack(mapFragment.toString())
                    .replace(R.id.auth_activity, mapFragment)
                    .commit()
        } else {
            Toast.makeText(
                    activity?.applicationContext,
                    "Не завершена загрузка данных. Войдите повторно.",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkFillFields(): Boolean {
        if (password.isEmpty() && login.isEmpty()) {
            signInFragmentBinding.incorrectData.text = ""
            signInFragmentBinding.incorrectData.text = "Введите пароль и логин"
            return false
        }
        if (login.isEmpty()) {
            signInFragmentBinding.incorrectData.text = ""
            signInFragmentBinding.incorrectData.text = "Введите логин"
            return false
        }
        if (password.isEmpty()) {
            signInFragmentBinding.incorrectData.text = ""
            signInFragmentBinding.incorrectData.text = "Введите пароль"
            return false
        }
        return true
    }

    private fun checkDataSignIn(loginCheck: String, passwordCheck: String): Boolean {
        if (login == loginCheck && password == passwordCheck) {
            isDataInCorrect = true
            return true
        } else if (login == loginCheck && password != passwordCheck
                || login != loginCheck && password == passwordCheck) {
            Toast.makeText(
                    activity?.applicationContext,
                    "Проверьте корректность данных",
                    Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return false
    }

    private fun checkUser(): Boolean {
        val userStorage = activity?.applicationContext?.let { UserStorage(it) }
        userStorage?.open()
        val listUser = userStorage?.getFullList()
        if (listUser != null) {
            val userOur = listUser.find { it?.login == login && it.password == password }
            if (userOur != null) {
                user = User(userOur.id, userOur.login, userOur.password, userOur.name)
                isDataInCorrect = checkDataSignIn(user.login, user.password)
                isUserExist = true
            }
        }
        userStorage?.close()
        if (isUserExist && isDataInCorrect) {
            return true
        }
        return false
    }

    private fun actionButtons() {
        with(signInFragmentBinding) {
            signInButton.setOnClickListener {
                with(signInFragmentBinding) {
                    login = loginEditText.text.toString()
                    password = passwordEditText.text.toString()
                }
                if (checkFillFields()) {
                    checkUser()
                    signIn()
                }
            }
            signUpButton.setOnClickListener {
                val signUpFragment = SignUpFragment()
                parentFragmentManager
                        .beginTransaction()
                        .addToBackStack(signUpFragment.toString())
                        .replace(R.id.auth_activity, signUpFragment)
                        .commit()
            }
        }
    }
}