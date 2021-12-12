package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.ListMarkFragmentBinding
import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.view.activities.AuthActivity
import com.ulskjourney.ulskjourney.view.listeners.AdapterMarkList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListMarkFragment : Fragment(R.layout.list_mark_fragment) {
    private lateinit var listMarkBinding: ListMarkFragmentBinding
    private var listMarks = ArrayList<Mark>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listMarkBinding = ListMarkFragmentBinding.bind(view)
        activity?.title = "Список меток"
        loadListMarks()
        fullList()
    }

    private fun fullList() {
        val recyclerView = listMarkBinding.recycleMarkList
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        var adapter = AdapterMarkList(listMarks)
        recyclerView.adapter = adapter
        adapter.onItemClick = { mark ->
            Toast.makeText(
                activity?.applicationContext,
                "Элемент${mark.id};",
                Toast.LENGTH_SHORT
            ).show()
            val detailMarkFragment = DetailMarkFragment.newInstance(mark.id)
            parentFragmentManager.beginTransaction()
                .addToBackStack(detailMarkFragment.tag)
                .replace(R.id.auth_activity, detailMarkFragment)
                .commit()
        }
    }


    private fun loadListMarks() {
        CoroutineScope(Dispatchers.Default).launch {
            listMarks = (activity as AuthActivity).getFirebasePostService().getListMarks()
        }
    }
}