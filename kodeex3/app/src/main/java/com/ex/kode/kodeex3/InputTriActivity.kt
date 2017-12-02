package com.ex.kode.kodeex3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast

class InputTriActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_tri)

        var s1 : EditText = findViewById(R.id.side_1)
        var s2 : EditText = findViewById(R.id.side_2)
        var s3 : EditText = findViewById(R.id.side_3)

        var draw : Button = findViewById(R.id.draw)
        draw.setOnClickListener(View.OnClickListener {
            if (s1.text.toString().length > 0 &&
                s2.text.toString().length > 0 &&
                s3.text.toString().length > 0) {
                if( checkTriangle((s1.text.toString()).toInt(), (s2.text.toString()).toInt(), (s3.text.toString()).toInt()) ) {
                    var figure = Triangle((s1.text.toString()).toInt(), (s2.text.toString()).toInt(), (s3.text.toString()).toInt())
                    var calc_check: CheckBox = findViewById(R.id.calc)
                    var finish = Intent(applicationContext, FinishActivity::class.java)

                    finish.putExtra("figure", getString(R.string.tri))
                    finish.putExtra("info", figure.get_info())
                    finish.putExtra("calc", if (calc_check.isChecked) figure.calc() else "")

                    startActivity(finish)
                } else {
                    Toast.makeText(applicationContext, R.string.tri_error, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, R.string.input_error, Toast.LENGTH_SHORT).show()
            }
        })

        var back: Button = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    fun checkTriangle(s1: Int, s2: Int, s3: Int): Boolean {
//        a < b + c,  a > b – c;  b < a + c,  b > a – c;  c < a + b,  c > a – b
        var r: Boolean = true

        if( s1 >= s2 + s3 || s1 <= s2 - s3 )
            r = false
        if( s2 >= s1 + s3 || s2 <= s1 - s3 )
            r = false
        if( s3 >= s2 + s1 || s3 <= s2 - s1 )
            r = false

        return r
    }
}
