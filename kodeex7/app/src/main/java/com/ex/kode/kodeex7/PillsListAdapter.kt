package com.ex.kode.kodeex7

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item.view.*

class PillsListAdapter(var data:Array<Pill>, val ctx: Context, val itemListener: ItemClickListener):
        RecyclerView.Adapter<PillsListAdapter.ViewHolder>() {
    companion object {
        var adapterClickListener: ItemClickListener? = null
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterClickListener = itemListener
        val pill: Pill = data[position]

        holder.name.text = pill.name.capitalize()

        val schedule = pill.getSchedule()

        holder.note.text = "Started at %s, take at %s".format(schedule.get("start"), schedule.get("time"))

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (adapterClickListener != null)
                adapterClickListener?.onListItemClick(pill)
        })
    }

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.pill_item_name
        var note: TextView = view.pill_item_note
    }

    interface ItemClickListener {
        fun onListItemClick(item: Pill)
    }
}