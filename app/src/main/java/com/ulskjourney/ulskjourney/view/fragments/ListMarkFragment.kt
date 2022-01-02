package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.ListMarkFragmentBinding
import com.ulskjourney.ulskjourney.model.database.MarkStorage
import com.ulskjourney.ulskjourney.model.firebase.MarkFirebase
import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.view.listeners.AdapterMarkList

class ListMarkFragment : Fragment(R.layout.list_mark_fragment) {
    private lateinit var listMarkBinding: ListMarkFragmentBinding
    private var listMarks = ArrayList<Mark>()
    private var adapter = AdapterMarkList(listMarks)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listMarkBinding = ListMarkFragmentBinding.bind(view)
        activity?.title = "Список меток"
        loadListMarks()
        fullList()
        listMarkBinding.returnToMap.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun fullList() {
        val recyclerView = listMarkBinding.recycleMarkList
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        adapter = AdapterMarkList(listMarks)
        recyclerView.adapter = adapter
        adapter.onItemClick = { mark ->
            val detailMarkFragment = DetailMarkFragment.newInstance(mark.id)
            parentFragmentManager.beginTransaction()
                    .addToBackStack(detailMarkFragment.tag)
                    .replace(R.id.auth_activity, detailMarkFragment)
                    .commit()
        }
        adapter.onItemClickDel = { mark ->
            deleteMark(mark.id)
        }
    }

    private fun loadListMarks() {
        var markStorage = activity?.applicationContext?.let { MarkStorage(it) }
        markStorage?.open()
        var listMark = markStorage?.getFullList()
        if (listMark == null) {
            Toast.makeText(activity?.applicationContext, "Добавьте метки", Toast.LENGTH_SHORT)
                    .show()
        } else listMarks = listMark as ArrayList<Mark>
        markStorage?.close()
    }

    private fun deleteMark(idMark: Int) {
        var markStorage = activity?.applicationContext?.let { MarkStorage(it) }
        markStorage?.open()
        var listMark = markStorage?.getFullList()
        if (listMark == null) {
            Toast.makeText(activity?.applicationContext, "Добавьте метки", Toast.LENGTH_SHORT)
                    .show()
        } else {
            listMark.drop(idMark)
            listMarks = listMark as ArrayList<Mark>
            markStorage?.delete(idMark)
            fullList()
            val markFirebaseLogic: MarkFirebase = MarkFirebase()
            activity?.applicationContext?.let { markFirebaseLogic.syncUsers(it) }
        }
        markStorage?.close()
    }
}