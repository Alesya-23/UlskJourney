package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.ListMarkFragmentBinding

class ListMarkFragment : Fragment(R.layout.list_mark_fragment) {
    private lateinit var listMarkBinding: ListMarkFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listMarkBinding = ListMarkFragmentBinding.bind(view)
        activity?.title = "Список меток"
    }
}