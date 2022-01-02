package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.DataUserFragmentBinding
import com.ulskjourney.ulskjourney.model.database.UserStorage
import com.ulskjourney.ulskjourney.model.models.User

private const val ID_ARGUMENT = "ID_ARGUMENT"

class ProfileFragment : Fragment(R.layout.data_user_fragment) {
    private lateinit var profileFragmentBinding: DataUserFragmentBinding
    private val userDetailIdArgument by lazy {
        requireArguments().getInt(ID_ARGUMENT, -1)
    }
    var userId = 0
    var userDetail: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileFragmentBinding = DataUserFragmentBinding.bind(view)
        getUser(userDetailIdArgument)
        buttonSave()
        activity?.title = "Профиль"
    }

    companion object {
        fun newInstance(userID: Int) = ProfileFragment().apply {
            this.arguments = bundleOf(
                    ID_ARGUMENT to userID
            )
            userId = userID
        }
    }

    private fun getUser(id: Int) {
        val userStorage = activity?.applicationContext?.let { UserStorage(it) }
        userStorage?.open()
        val user = userStorage?.getElement(id)
        if (user == null) {
            Toast.makeText(activity?.applicationContext, "Ошибка бд", Toast.LENGTH_SHORT).show()
        } else fillMark(user)
    }

    private fun fillMark(user: User) {
        with(profileFragmentBinding) {
            editTextName.setText(user.name)
            editTextLogin.setText(user.login)
            passwordEditText.setText(user.password)
        }
    }

    private fun buttonSave() {
        profileFragmentBinding.buttonSave.setOnClickListener {
            //изменить данные
            updateUserData(userId)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun updateUserData(id: Int) {
        val userName = profileFragmentBinding.editTextName.text.toString()
        val userLogin = profileFragmentBinding.editTextLogin.text.toString()
        val userPass = profileFragmentBinding.passwordEditText.text.toString()
        userDetail = User(userId, userLogin, userPass, userName)
        val userStorage = activity?.applicationContext?.let { UserStorage(it) }
        userStorage?.open()
        val user = userStorage?.getElement(id)
        if (user == null) {
            Toast.makeText(activity?.applicationContext, "Ошибка бд", Toast.LENGTH_SHORT).show()
        } else userDetail?.let { userStorage.update(it) }
    }
}