package com.ex.kode.kodeex7

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_take_pill.*

class TakePillActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_pill)

        val pill_id = intent.getStringExtra("pill")
        val mode = intent.getStringExtra("mode")

        if( pill_id != null ) {
            val pill = Pill.getPill(pill_id, applicationContext)

            if( pill != null ) {
                fillPill(pill, mode)

                // (+) Later action
                take_later.setOnClickListener{_ -> takePill(pill, mode, "unknown")}
                take_pill.setOnClickListener{_ -> takePill(pill, mode, "true")}
                take_cancel.setOnClickListener{_ -> takePill(pill, mode, "false")}
            } else {
                Toast.makeText(applicationContext,
                    resources.getString(R.string.pill_error),
                    Toast.LENGTH_LONG).show()
                finish()
            }
        } else {
            Toast.makeText(applicationContext,
                    resources.getString(R.string.pill_error),
                    Toast.LENGTH_LONG).show()
            finish()
        }
    }

    fun fillPill(pill: Pill, mode: String) {
        // (+) Fill Pill image

        take_pill_caption.text = if( mode == "take" )
            resources.getString(R.string.time_to_pill) else
            resources.getString(R.string.time_to_meal)

        take_pill_name.text = pill.name
        take_pill_desc.text = pill.desc
        take_pill_note.text = pill.notes

        if( mode != "take" ) {
            take_cancel.visibility = View.GONE
            take_later.visibility = View.GONE
        }
    }

    fun takePill(pill: Pill, mode: String, status: String) {
        if( mode == "take" ) {
            val new_take = History(-1, pill.id, pill.name, Pill.getDate(false), status)

            if (new_take.addNewTake(applicationContext, pill, status) != null) {
                finish()
            } else {
                Toast.makeText(applicationContext,
                        resources.getString(R.string.pill_error),
                        Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext,
                    resources.getString(R.string.nice_meal),
                    Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
