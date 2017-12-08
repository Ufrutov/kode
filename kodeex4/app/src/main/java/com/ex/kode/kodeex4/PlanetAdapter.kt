package com.ex.kode.kodeex4

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item.view.*

class PlanetAdapter(var data:Array<Planet>, val ctx: Context): RecyclerView.Adapter<PlanetAdapter.ViewHolder>() {
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var planet: Planet = data[position]
        holder.name.text = planet.name.capitalize()
        holder.dist.text = "Distance to the Sun: %s".format(planet.dist)

        holder.population.setImageBitmap(planet.population_img)
        holder.planet.setImageBitmap(planet.img)

        holder.itemView.setOnClickListener(View.OnClickListener {
//            go to the dark side
            var intent: Intent = Intent(ctx, InfoActivity::class.java)
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
            intent.setType("text/plain")

            intent.putExtra("planet", planet.name.capitalize())
            intent.putExtra("url", planet.url)

            ctx.startActivity(intent)
        })
    }

    class ViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        var planet: ImageView = view.planet
        var name: TextView = view.name
        var dist: TextView = view.dist
        var population: ImageView = view.population
    }
}