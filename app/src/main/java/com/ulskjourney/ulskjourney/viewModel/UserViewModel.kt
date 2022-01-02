package com.ulskjourney.ulskjourney.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    var idUser: MutableLiveData<Int> = MutableLiveData<Int>()

    fun setIdUser(key: Int) {
        idUser.value = key
    }

    fun getItem() = idUser.value
}