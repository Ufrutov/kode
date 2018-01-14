package com.ex.kode.kodeex7

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var listAdapter: PillsListAdapter? = null
    private lateinit var pill_list: RecyclerView
    private var manager: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        pill_list = findViewById(R.id.pills_list)
        pill_list.layoutManager = LinearLayoutManager(applicationContext)
        updateList()

        fab.setOnClickListener { _ -> openNewPillActivity() }

        launchPillManager()
    }

    fun openNewPillActivity() {
        val intent: Intent = Intent(applicationContext, NewPillActivity::class.java)
        startActivity(intent)
    }

    fun openPillActivity(pill: Pill) {
        val intent: Intent = Intent(applicationContext, EditPillActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.type = "text/plain"
        intent.putExtra("pill", pill.id.toString())
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_history -> openPillsHistory()
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openPillsHistory(): Boolean {
        val intent: Intent = Intent(applicationContext, HistoryActivity::class.java)
        startActivity(intent)

        return true
    }

    fun updateList() {
        val pills: ArrayList<Pill> = Pill.getPills(applicationContext)

        if( pills.size == 0 )
            empty_text.visibility = View.VISIBLE
        else
            empty_text.visibility = View.GONE

        if( listAdapter == null ) {
            listAdapter = PillsListAdapter(Array(pills.size, { i -> pills.get(i) }), this,
                object: PillsListAdapter.ItemClickListener {
                    override fun onListItemClick(item: Pill) {
                        openPillActivity(item)
                    }
            })
            pill_list.adapter = listAdapter
        } else {
            listAdapter?.data = Array(pills.size, { i -> pills.get(i) })
            listAdapter?.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun launchPillManager() {
        if( !manager ) {
            manager = true
            val calendar = Calendar.getInstance()

            val alarm: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(applicationContext, PillManager::class.java)
            intent.setAction("com.blogspot.shudiptotrafder.lifeschedular");

            val pending: PendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

            alarm.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_HOUR,
                    pending)
        }
    }
}
