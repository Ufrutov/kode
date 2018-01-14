package com.ex.kode.kodeex7

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val history = History.getHistory(applicationContext, -1)

        if( history.size == 0 )
            empty_history.visibility = View.VISIBLE
        else
            empty_history.visibility = View.GONE

        val list = history_list
        list.layoutManager = LinearLayoutManager(applicationContext)
        val adapter = HistoryListAdapter(Array(history.size, { i -> history.get(i) }), this,
            object: HistoryListAdapter.ItemClickListener {
                override fun onListItemClick(item: History) {
                    openPillInfo(item)
                }
        })
        list.adapter = adapter
    }

    fun openPillInfo(entry: History) {
        if( entry.pill != -1 ) {
            val intent: Intent = Intent(applicationContext, EditPillActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            intent.type = "text/plain"
            intent.putExtra("pill", entry.pill.toString())
            startActivity(intent)
        } else {
            Toast.makeText(applicationContext,
                resources.getString(R.string.pill_error),
                Toast.LENGTH_LONG).show()
        }
    }
}
