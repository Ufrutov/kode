package com.ex.kode.kodeex8

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ex.kode.kodeex8.R.id.message_list

class MessageActivity : AppCompatActivity(), MessageView.Callback {

    override fun getDialogID(): Int {
        return dialog_id
    }

    var dialog_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dialog_id = intent.getIntExtra("user_id", 0)

        setContentView(R.layout.activity_main2)
    }

    fun callFragment() {
        MessageView().getVKMessages(dialog_id)
    }
}
