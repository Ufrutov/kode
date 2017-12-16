package com.ex.kode.kodeex5

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.task_item.view.*
import java.text.SimpleDateFormat

class ListAdapter(var data:Array<Task>, var ctx: Context): RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.task_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var task: Task = data[position]

        holder.text.text = task.text
        holder.date.text = "%s: %s".format(
                if( task.active ) ctx.resources.getString(R.string.added)
                    else ctx.resources.getString(R.string.closed),
                parseDate(task.date))
        holder.status.setImageBitmap(
                BitmapFactory.decodeResource(
                        ctx.resources,
                        if( task.active ) R.drawable.check else R.drawable.update
                )
        )

        holder.status.setOnClickListener(View.OnClickListener {
            if( ctx is MainActivity )
                (ctx as MainActivity).toggleTask(task.id, !task.active)
        })

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            if( ctx is MainActivity )
                (ctx as MainActivity).showUpdateForm(task)
            return@OnLongClickListener true
        })
    }

    class ViewHolder(var view: View): RecyclerView.ViewHolder(view) {
        var status: ImageView = view.task_status
        var text: TextView = view.task_text
        var date: TextView = view.task_date
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDate(dt: String): String {
        val f = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return f.format(dt.toLong()*1000)
    }
}