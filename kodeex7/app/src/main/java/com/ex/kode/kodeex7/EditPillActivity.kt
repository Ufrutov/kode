package com.ex.kode.kodeex7

import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pill.*
import java.util.*

class EditPillActivity: NewPillActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pill)

        val pill_id = intent.getStringExtra("pill")

        if( pill_id != null ) {
            val pill = Pill.getPill(pill_id, applicationContext)
            if( pill !== null ) {
                fillUpdateForm(pill)

                val date: Calendar = Calendar.getInstance()
                val c_y = date.get(Calendar.YEAR).toInt()
                val c_m = date.get(Calendar.MONTH).toInt()
                val c_d = date.get(Calendar.DAY_OF_MONTH).toInt()

                findViewById<EditText>(R.id.pill_start_input).setOnClickListener(View.OnClickListener {
                    val dp = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                        _, year, month, day ->
                        findViewById<EditText>(R.id.pill_start_input).setText("%s.%s.%s".format("%02d".format(day), "%02d".format(month+1), year))
                    }, c_y, c_m, c_d)
                    dp.show()
                })

                findViewById<EditText>(R.id.pill_time_input).setOnClickListener(View.OnClickListener {
                    val tp = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                        _, h, m ->
                        findViewById<EditText>(R.id.pill_time_input).setText("%s:%s".format("%02d".format(h), "%02d".format(m)))
                    }, 12,0, true)
                    tp.show()
                })

                pill_update_actions.visibility = View.VISIBLE

                pill_delete.setOnClickListener(View.OnClickListener {
                    confirmDelete(pill)
                })

                pill_update.setOnClickListener(View.OnClickListener {
                    updatePill(pill)
                })

                // (!) Pill test > launch pill activity
                test_pill.setOnClickListener(View.OnClickListener {
                    takePill(pill, "take")
                })
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

    fun fillUpdateForm(pill: Pill) {
        this.fillForm()

        pill_name_input.setText(pill.name)
        pill_desc_input.setText(pill.desc)
        pill_note_input.setText(pill.notes)

        // Fill Pill schedule form form schedule data

        val schedule: HashMap<String, String> = pill.getSchedule()

        findViewById<EditText>(R.id.pill_start_input).setText(schedule.get("start"))
        findViewById<EditText>(R.id.pill_time_input).setText(schedule.get("time"))

        var meal = resources.getString(R.string.meal_none)
        when(schedule.get("meal")) {
            "after" -> meal = resources.getString(R.string.eat_after)
            "before" -> meal = resources.getString(R.string.eat_before)
        }
        val meal_values = resources.getStringArray(R.array.pill_meal_related_values)
        findViewById<Spinner>(R.id.pill_meal_input).setSelection(meal_values.indexOf(meal))

        val delay = schedule.get("delay")
        val delay_values = resources.getStringArray(R.array.pill_meal_delay_values)
        findViewById<Spinner>(R.id.pill_meal_delay_input).setSelection(delay_values.indexOf(delay))
    }

    fun confirmDelete(pill: Pill) {
        val confirm = AlertDialog.Builder(this).create()
        confirm.setMessage(resources.getString(R.string.confirm_delete).format(pill.name))
        confirm.setButton(
                AlertDialog.BUTTON_POSITIVE,
                resources.getString(R.string.confirm_ok),
                {
                    _, _ -> onConfirmDelete(pill)
                })
        confirm.setButton(
                AlertDialog.BUTTON_NEGATIVE,
                resources.getString(R.string.confirm_cancel),
                {
                    _, _ -> null
                }
        )
        confirm.show()
    }

    fun onConfirmDelete(pill: Pill) {
        pill.deletePill(applicationContext, pill)
        finish()
    }

    fun updatePill(pill: Pill) {
        val update: Pill? = getPillForm()

        if( update != null ) {
            pill.updatePill(applicationContext, pill, update)
            finish()
        } else {
            Toast.makeText(applicationContext,
                    resources.getString(R.string.pill_error),
                    Toast.LENGTH_LONG).show()
        }
    }

    // (T) Test launch of TakePillActivity Activity
    fun takePill(pill: Pill, mode: String) {

        Handler().postDelayed({
            // Delayed notification
            // Take Pill Activity
            val intent: Intent = Intent(applicationContext, TakePillActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            intent.type = "text/plain"
            intent.putExtra("pill", pill.id.toString())
            intent.putExtra("mode", mode)

            // Take Pill Activity wrapper
            val resultIntent = PendingIntent.getActivity(
                    applicationContext,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
            )

            // Notification service
            val notification = NotificationCompat.Builder(this, "KODE_PILLS_01")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.pills)
                    .setContentTitle(resources.getString(R.string.app_title))
                    .setContentText(pill.name)
                    .setContentIntent(resultIntent)

            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.notify(1, notification.build())
        }, 5000)

        finish()
    }
}