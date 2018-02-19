package com.ex.kode.kodeex8

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vk.sdk.api.model.VKApiDialog
import com.vk.sdk.api.model.VKApiMessage
import com.vk.sdk.api.model.VKList
import kotlinx.android.synthetic.main.dialog_list_item.view.*

class DialogAdapter(var data: VKList<VKApiDialog>, val itemListener: ItemClickListener):
        RecyclerView.Adapter<DialogAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onListItemClick(item: VKApiDialog)
    }

    companion object {
        var adapterClickListener: ItemClickListener? = null
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterClickListener = itemListener
        val entry: VKApiDialog = data[position]

        holder.user.text = entry.message.user_id.toString()
        holder.date.text = entry.message.date.toString()
        holder.body.text = entry.message.body

//        entry.unread

        holder.itemView.setOnClickListener(View.OnClickListener {
            Log.d("VKSdk", "View.OnClickListener")
            if (adapterClickListener != null) {
                adapterClickListener?.onListItemClick(entry)

            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.dialog_list_item, parent, false))

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var user: TextView = view.dialog_user
        var date: TextView = view.dialog_date
        var body: TextView = view.dialog_body
    }
}