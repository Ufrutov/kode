package com.ex.kode.kodeex8

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vk.sdk.api.model.VKApiMessage
import com.vk.sdk.api.model.VKList
import kotlinx.android.synthetic.main.message_list_item.view.*

class MessageAdapter(var data: VKList<VKApiMessage>):
        RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry: VKApiMessage = data[position]
        Log.d("VKSdk", "onBindViewHolder date: %s".format(entry.date))
        holder.time.text = entry.date.toString()
        holder.body.text = entry.body
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_list_item, parent, false))

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var time: TextView = view.message_time
        var body: TextView = view.message_body
    }
}