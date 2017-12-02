package com.ex.kode.kodeex3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class InputCircleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_circle)

        var draw : Button = findViewById(R.id.draw)
        draw.setOnClickListener(View.OnClickListener {
            var radius_input : EditText = findViewById(R.id.radius)

            if( radius_input.text.toString().length > 0 ) {
                var figure = Circle((radius_input.text.toString()).toInt())
                var calc_check : CheckBox = findViewById(R.id.calc)
                var finish = Intent(applicationContext, FinishActivity::class.java)

                finish.putExtra("figure", getString(R.string.circ))
                finish.putExtra("info", figure.get_info())
                finish.putExtra("calc",  if(calc_check.isChecked) figure.calc() else "")

                startActivity(finish)
            } else {
                Toast.makeText(applicationContext, R.string.input_error, Toast.LENGTH_SHORT).show()
            }
        })

        var back: Button = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}
