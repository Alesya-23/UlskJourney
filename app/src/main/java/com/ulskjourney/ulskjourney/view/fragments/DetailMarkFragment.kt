package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.DetailMarkFragmentBinding
import com.ulskjourney.ulskjourney.model.database.MarkStorage
import com.ulskjourney.ulskjourney.model.models.Mark

private const val ID_ARGUMENT = "ID_ARGUMENT"

class DetailMarkFragment : Fragment(R.layout.detail_mark_fragment) {
    private lateinit var detailMarkFragmentBinding: DetailMarkFragmentBinding
    private val markDetailIdArgument by lazy {
        requireArguments().getInt(ID_ARGUMENT, -1)
    }
    var markId = 0
    var userId = 0
    var mark: Mark? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailMarkFragmentBinding = DetailMarkFragmentBinding.bind(view)
        getMark(markDetailIdArgument)
        buttonSave()
    }

    companion object {
        fun newInstance(markId: Int) = DetailMarkFragment().apply {
            this.arguments = bundleOf(
                    ID_ARGUMENT to markId
            )
            this.markId = markId
        }
    }

    private fun getMark(id: Int) {
        var markStorage = activity?.applicationContext?.let { MarkStorage(it) }
        markStorage?.open()
        val mark = markStorage?.getElement(id)
        if (mark == null) {
            Toast.makeText(activity?.applicationContext, "Ошибка бд", Toast.LENGTH_SHORT)
                    .show()
        }
        if (mark != null) {
            fillMark(mark)
            userId = mark.id
        }
        markStorage?.close()
    }

    private fun fillMark(mark: Mark) {
        with(detailMarkFragmentBinding) {
            nameMarkTextView.setText(mark.name)
            latitudeTextView.setText(mark.latitude.toString())
            longitudeTextView.setText(mark.longitude.toString())
            descriptionMarkTextView.setText(mark.description)
        }
    }

    private fun buttonSave() {
        detailMarkFragmentBinding.buttonSave.setOnClickListener {
            //изменить данные
            updateUserData(markId)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun updateUserData(id: Int) {
        val markName = detailMarkFragmentBinding.nameMarkTextView.text.toString()
        val latitude = detailMarkFragmentBinding.latitudeTextView.text.toString()
        val longitude = detailMarkFragmentBinding.longitudeTextView.text.toString()
        val description = detailMarkFragmentBinding.descriptionMarkTextView.text.toString()
        mark = Mark(markId, markName, latitude.toDouble(), longitude.toDouble(), description, userId)
        val markStorage = activity?.applicationContext?.let { MarkStorage(it) }
        markStorage?.open()
        val user = markStorage?.getElement(id)
        if (user == null) {
            Toast.makeText(activity?.applicationContext, "Ошибка бд", Toast.LENGTH_SHORT).show()
        } else mark?.let { markStorage.update(it) }
    }
}