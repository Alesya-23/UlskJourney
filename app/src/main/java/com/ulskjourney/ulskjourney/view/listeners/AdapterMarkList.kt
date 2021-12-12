package com.ulskjourney.ulskjourney.view.listeners

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.view.fragments.DetailMarkFragment

class AdapterMarkList(private val marks: ArrayList<Mark>) :
    RecyclerView.Adapter<AdapterMarkList.MyViewHolder>() {
    var onItemClick: ((Mark) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var largeTextView: TextView? = null

        init {
            largeTextView = itemView.findViewById(R.id.textViewLarge)
            itemView.setOnClickListener {
                onItemClick?.invoke(marks[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.largeTextView?.text = marks[position].name
    }

    override fun getItemCount() = marks.size
}