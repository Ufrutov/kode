package com.ex.kode.kodeex3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class FinishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        var fig_name: TextView = findViewById(R.id.fig_name)
        fig_name.text = intent.getStringExtra("figure")

        var fig_info: TextView = findViewById(R.id.fig_info)
        fig_info.text = intent.getStringExtra("info")

        var calc: String = intent.getStringExtra("calc")
        var calc_label: TextView = findViewById(R.id.calc_label)
        var calc_info: TextView = findViewById(R.id.calc_info)
        if( calc.length > 0 ) {
            calc_label.visibility = View.VISIBLE
            calc_info.visibility = View.VISIBLE
            calc_info.text = calc
        } else {
            calc_label.visibility = View.INVISIBLE
            calc_info.visibility = View.INVISIBLE
        }

        var back: Button = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}
