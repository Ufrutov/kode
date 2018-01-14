package com.ex.kode.kodeex7

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.util.Log
import java.util.*

class PillManager: BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        checkPills(ctx)
    }

    fun checkPills(ctx: Context?) {
        val ts = Pill.getDate(false).toLong()
        val pills: ArrayList<Pill> = Pill.getPills(ctx)

        pills.forEach { pill ->
            val sched: HashMap<String, String> = pill.getSchedule()
            val pill_ts: Long = Pill.getPillTS(sched.get("time").toString())

            if( (pill_ts - ts) > 0 && (pill_ts - ts) < 60*60) {
                val delay = pill_ts - ts

                if( sched.get("meal") != "no" ) {
                    var meal_delay = sched.get("m_delay").toString().split(" ")[0].toLong()*60
                    when( sched.get("meal") ) {
                        "before" -> meal_delay = delay - meal_delay
                        "after" -> meal_delay = delay + meal_delay
                    }
                    if( meal_delay > 0 )
                        firePill(ctx, pill, meal_delay, "meal")
                }

                firePill(ctx, pill, delay, "take")
            }
        }
    }

    fun firePill(ctx: Context?, pill: Pill, delay: Long, mode: String) {

        Handler().postDelayed({
            // Take Pill Activity
            val intent: Intent = Intent(ctx, TakePillActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.type = "text/plain"
            intent.putExtra("pill", pill.id.toString())
            intent.putExtra("mode", mode)

            // Take Pill Activity wrapper
            val resultIntent = PendingIntent.getActivity(
                    ctx,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
            )

            var text = if( mode == "take" )
                "Time to take %s pill".format(pill.name) else
                "Time to meal for %s pill".format(pill.name)

            // Notification service
            val notification = NotificationCompat.Builder(ctx as Context, "KODE_PILLS_01")
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.pills)
                    .setContentTitle(ctx.resources.getString(R.string.app_title))
                    .setContentText(text)
                    .setContentIntent(resultIntent)

            val manager: NotificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            manager.notify( 1, notification.build())
        }, delay*1000)

    }
}