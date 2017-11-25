package com.ex.kode.kode_ex_1

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeColor("OnCreate")
    }

    override fun onPause() {
        changeColor("OnPause")
        super.onPause()
    }

    override fun onResume() {
        changeColor("OnResume")
        super.onResume()
    }

    override fun onStart() {
        changeColor("OnStart")
        super.onStart()
    }

    override fun onStop() {
        changeColor("OnStop")
        super.onStop()
    }

    override fun onRestart() {
        changeColor("OnRestart")
        super.onRestart()
    }

    fun changeColor(Event: String) {
//        Access color resources via event name
//        var color = getResources().getIdentifier(String.format("color%s", Event),"color", getPackageName())
//        main_activity.setBackgroundColor(Color.parseColor(color.toString()))

        var color = "#ffffff"

        when(Event) {
            "OnPause" -> color = "#b6dfe6"
            "OnResume" -> color = "#b3e2be"
            "OnStart" -> color = "#f1c7c7"
            "OnStop" -> color = "#ff8888"
            "OnRestart" -> color = "#edc2a1"
        }

        main_activity.setBackgroundColor(Color.parseColor(color))

        Toast.makeText(applicationContext, Event, Toast.LENGTH_SHORT).show()

        last_state.setText(String.format("Last app state: %s", Event))
    }
}
