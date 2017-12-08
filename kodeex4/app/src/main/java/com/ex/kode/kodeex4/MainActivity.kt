package com.ex.kode.kodeex4

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            actionBar.title = resources.getString(R.string.app_tilte)
        } catch (e: Exception) {

        }

        val ctx: Context = applicationContext

        var planets = ctx.resources.getStringArray(R.array.planet_names)
        var dist = ctx.resources.getStringArray(R.array.planet_to_sun)
        var url = ctx.resources.getStringArray(R.array.planet_url)
        var population = ctx.resources.getStringArray(R.array.planet_population)

        var data = Array(planets.size, { i ->
            Planet(
                    planets[i],
                    dist[i],
                    population[i].toBoolean(),
                    url[i],
                    BitmapFactory.decodeResource(resources, if(population[i].toBoolean()) R.drawable.human else R.drawable.alien),
//                    BitmapFactory.decodeResource(resources, ctx.resources.getIdentifier(planets[i], "drawable", ctx.packageName)))
//                    BitmapFactory.decodeResource(resources, ctx.resources.getIdentifier(planets[i], "drawable", "com.ex.kode.kodeex4")))
                    BitmapFactory.decodeResource(resources,
                            when(planets[i]) {
                                "mercury" -> R.drawable.mercury
                                "venus" -> R.drawable.venus
                                "earth" -> R.drawable.earth
                                "mars" -> R.drawable.mars
                                "jupiter" -> R.drawable.jupiter
                                "saturn" -> R.drawable.saturn
                                "uranus" -> R.drawable.uranus
                                "neptune" -> R.drawable.neptune
                                "pluto" -> R.drawable.pluto
                                else -> R.drawable.alien
                            }
                        ))
        })

        var list = list
        list.layoutManager = LinearLayoutManager(applicationContext)
        list.adapter = PlanetAdapter(data, ctx)
        list.adapter.notifyDataSetChanged()
    }
}
