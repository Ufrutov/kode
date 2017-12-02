package com.ex.kode.kodeex3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var proceed : Button = findViewById(R.id.proceed)
        proceed.setOnClickListener(View.OnClickListener {
            var fig_spin : Spinner = findViewById(R.id.figure)
            var figure = fig_spin.selectedItem.toString()

            selectFigure(figure)
        })
    }

    fun selectFigure(figure : String) {
        var intent = Intent(applicationContext, InputCircleActivity::class.java)

        when(figure) {
            getString(R.string.rect) -> intent = Intent(applicationContext, InputRectActivity::class.java)
            getString(R.string.tri) -> intent = Intent(applicationContext, InputTriActivity::class.java)
        }

        startActivity(intent)
    }
}
