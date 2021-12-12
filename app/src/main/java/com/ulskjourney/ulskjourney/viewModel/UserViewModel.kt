package com.ulskjourney.ulskjourney.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var idUser: MutableLiveData<String> = MutableLiveData<String>()

    fun setIdUser(key: String) {
        idUser.value = key
    }

    fun getItem() = idUser.value
}