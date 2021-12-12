package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ulskjourney.ulskjourney.MainActivity
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.DetailMarkFragmentBinding
import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.view.activities.AuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ID_ARGUMENT = "ID_ARGUMENT"

class DetailMarkFragment : Fragment(R.layout.detail_mark_fragment) {
    private lateinit var detailMarkFragmentBinding: DetailMarkFragmentBinding
    private val markDetailIdArgument by lazy {
        requireArguments().getInt(ID_ARGUMENT, -1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailMarkFragmentBinding = DetailMarkFragmentBinding.bind(view)
        getMark(markDetailIdArgument)
    }

    companion object {
        fun newInstance(markId: Int) = DetailMarkFragment().apply {
            this.arguments = bundleOf(
                ID_ARGUMENT to markId
            )
        }
    }

    private fun getMark(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val mark = (activity as AuthActivity).getFirebasePostService().getMark(id)
            withContext(Dispatchers.Main) {
                fillMark(mark)
            }
        }
    }

    private fun fillMark(mark: Mark) {
        with(detailMarkFragmentBinding) {
            nameMarkTextView.text = mark.name
            latitudeTextView.text = mark.latitude.toString()
            longitudeTextView.text = mark.longitude.toString()
            descriptionMarkTextView.text = mark.description
        }
    }
}