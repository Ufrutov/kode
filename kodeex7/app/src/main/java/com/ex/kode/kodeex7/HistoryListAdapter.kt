package com.ex.kode.kodeex7

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.history_list_item.view.*

class HistoryListAdapter(var data:Array<History>, val ctx: Context, val itemListener: ItemClickListener):
        RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

    companion object {
        var adapterClickListener: ItemClickListener? = null
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.history_list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterClickListener = itemListener
        val entry: History = data[position]

        var status = ""

        when( entry.status ) {
            "true" -> status = ctx.resources.getString(R.string.status_success)
            "false" -> status = ctx.resources.getString(R.string.status_fail)
            else -> status = ctx.resources.getString(R.string.status_unknown)
        }

        holder.name.text = entry.name.capitalize()
        holder.note.text = "%s, status: %s".format(
                Pill.parseDate(entry.date, "E, HH:mm:ss, dd.MM.yyyy"), status)

        holder.itemView.setOnClickListener(View.OnClickListener {
            if (adapterClickListener != null)
                adapterClickListener?.onListItemClick(entry)
        })
    }

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.history_item_name
        var note: TextView = view.history_item_note
    }

    interface ItemClickListener {
        fun onListItemClick(item: History)
    }
}