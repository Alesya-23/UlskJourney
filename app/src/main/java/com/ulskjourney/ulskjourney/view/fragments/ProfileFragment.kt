package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.DataUserFragmentBinding
import com.ulskjourney.ulskjourney.model.models.User
import com.ulskjourney.ulskjourney.view.activities.AuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ID_ARGUMENT = "ID_ARGUMENT"

class ProfileFragment : Fragment(R.layout.data_user_fragment) {
    private lateinit var profileFragmentBinding: DataUserFragmentBinding
    private val userDetailIdArgument by lazy {
        requireArguments().getString(ID_ARGUMENT, "-1")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileFragmentBinding = DataUserFragmentBinding.bind(view)
        getUser(userDetailIdArgument)
        buttonSave()
        activity?.title = "Профиль"
    }

    companion object {
        fun newInstance(userId: String) = ProfileFragment().apply {
            this.arguments = bundleOf(
                ID_ARGUMENT to userId
            )
        }
    }

    private fun getUser(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = (activity as AuthActivity).getFirebasePostService().getUser(id)
            withContext(Dispatchers.Main) {
                fillMark(user)
            }
        }
    }

    private fun fillMark(user : User) {
        with(profileFragmentBinding) {
            editTextName.setText(user.name)
            editTextLogin.setText(user.login)
            passwordEditText.setText(user.password)
        }
    }

    private fun buttonSave(){
        profileFragmentBinding.buttonSave.setOnClickListener {
        //изменить данные
        }
    }
}