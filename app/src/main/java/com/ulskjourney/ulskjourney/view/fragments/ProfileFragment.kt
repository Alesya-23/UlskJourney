package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.DataUserFragmentBinding

class ProfileFragment : Fragment(R.layout.data_user_fragment) {
    private lateinit var profileFragmentBinding : DataUserFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileFragmentBinding = DataUserFragmentBinding.bind(view)
        activity?.title = "Профиль"
    }
}