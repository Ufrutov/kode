package com.ex.kode.kodeex3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class InputRectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_rect)

        var point: CheckBox = findViewById(R.id.point)
        point.setOnClickListener(View.OnClickListener {
            switchInput()
        })

        var draw : Button = findViewById(R.id.draw)
        draw.setOnClickListener(View.OnClickListener {
            if (point.isChecked) {
                var p1x: EditText = findViewById(R.id.point_1_x)
                var p1y: EditText = findViewById(R.id.point_1_y)
                var p2x: EditText = findViewById(R.id.point_2_x)
                var p2y: EditText = findViewById(R.id.point_2_y)
                if( p1x.text.toString().length > 0 &&
                        p1y.text.toString().length > 0 &&
                        p2x.text.toString().length > 0 &&
                        p2y.text.toString().length > 0 ) {
                    if( p1x.text.toString() != p2x.text.toString() &&
                           p1y.text.toString() != p2y.text.toString() ) {
                        var p3x: Int = (p1x.text.toString()).toInt()
                        var p3y: Int = (p2y.text.toString()).toInt()
                        var s1: Int = Math.abs((p1y.text.toString()).toInt() - p3y)
                        var s2: Int = Math.abs((p2x.text.toString()).toInt() - p3x)
                        var figure = Rectangle(s1, s2)
                        var calc_check: CheckBox = findViewById(R.id.calc)
                        var finish = Intent(applicationContext, FinishActivity::class.java)

                        finish.putExtra("figure", getString(R.string.rect))
                        finish.putExtra("info", figure.get_info())
                        finish.putExtra("calc", if (calc_check.isChecked) figure.calc() else "")

                        startActivity(finish)
                    } else {
                        Toast.makeText(applicationContext, R.string.rect_error, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, R.string.input_error, Toast.LENGTH_SHORT).show()
                }
            } else {
                var s1: EditText = findViewById(R.id.side_1)
                var s2: EditText = findViewById(R.id.side_2)
                if (s1.text.toString().length > 0 &&
                        s2.text.toString().length > 0) {
                    var figure = Rectangle((s1.text.toString()).toInt(), (s2.text.toString()).toInt())
                    var calc_check: CheckBox = findViewById(R.id.calc)
                    var finish = Intent(applicationContext, FinishActivity::class.java)

                    finish.putExtra("figure", getString(R.string.rect))
                    finish.putExtra("info", figure.get_info())
                    finish.putExtra("calc", if (calc_check.isChecked) figure.calc() else "")

                    startActivity(finish)
                } else {
                    Toast.makeText(applicationContext, R.string.input_error, Toast.LENGTH_SHORT).show()
                }
            }
        })

        var back: Button = findViewById(R.id.back)
        back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    fun switchInput() {
        var point: CheckBox = findViewById(R.id.point)
        var point_input: LinearLayout = findViewById(R.id.point_input)
        var sides_input: LinearLayout = findViewById(R.id.side_input)

        if( point.isChecked ) {
            point_input.visibility = View.VISIBLE
            sides_input.visibility = View.INVISIBLE
        } else {
            point_input.visibility = View.INVISIBLE
            sides_input.visibility = View.VISIBLE
        }
    }
}
