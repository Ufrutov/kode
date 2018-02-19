package com.ex.kode.kodeex8

import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiDialog
import com.vk.sdk.api.model.VKApiGetDialogResponse
import com.vk.sdk.api.model.VKApiGetMessagesResponse
import com.vk.sdk.api.model.VKList
import kotlinx.android.synthetic.main.fragment_blank.*

class MainActivity : AppCompatActivity(), MessageView.Callback {

    private var dialogAdapter: DialogAdapter? = null
    private lateinit var dialogs: RecyclerView

    override fun getDialogID(): Int {
        return dialog_id
    }

    var dialog_id: Int = 0

//    override fun OnClick(s: String) {
//        if (resources.configuration.orientation
//                == Configuration.ORIENTATION_LANDSCAPE) {
//            supportFragmentManager.beginTransaction()
//                    .add(R.id.frame, MessageView())
//                    .commit()
//        } else {
//            var intent = Intent(this, MessageActivity::class.java)
//            intent.putExtra("string", s)
//            startActivity(intent)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Login to VKSdk
        VKSdk.login(this, VKScope.MESSAGES)

        dialogs = dialog_list
        dialogs.layoutManager = LinearLayoutManager(applicationContext)
    }

    // Login result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if( !VKSdk.onActivityResult(requestCode, resultCode, data, object: VKCallback<VKAccessToken> {
            override fun onResult(res: VKAccessToken?) {
                getVKDialogs()
            }
            override fun onError(error: VKError?) {
                Log.d("VKSdk", "onError")
                // Handle login error
            }
        } ) ) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun getVKDialogs() {
        // Get dialogs
        val dialogs = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 20, VKApiConst.PREVIEW_LENGTH, 30))
        val listener: VKRequest.VKRequestListener = object: VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)

                val VKdialogs = (response!!.parsedModel as VKApiGetDialogResponse).items

                updateDialogList(VKdialogs)
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                Log.d("VKSdk", "Dialogs onError: %s".format(error.toString()))
            }

            override fun onProgress(progressType: VKRequest.VKProgressType?, bytesLoaded: Long, bytesTotal: Long) {
                super.onProgress(progressType, bytesLoaded, bytesTotal)
                Log.d("VKSdk", "Dialogs onProgress: %s".format(progressType.toString()))
            }
        }

        dialogs.executeWithListener(listener)
    }

    fun updateDialogList(data: VKList<VKApiDialog>) {
        if( dialogAdapter == null ) {
            dialogAdapter = DialogAdapter(data,
                object: DialogAdapter.ItemClickListener {
                    override fun onListItemClick(item: VKApiDialog) {
                        Log.d("VKSdk", "onListItemClick: %s".format(item.id))
                        openDialogActivity(item)
                    }
            })
            dialogs.adapter = dialogAdapter
        } else {
            dialogAdapter?.data = data
            dialogAdapter?.notifyDataSetChanged()
        }
    }

    fun getVKMessages(id: Int) {
        // Get messages for dialog by id
        val history = VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id))
        val listener: VKRequest.VKRequestListener = object: VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)

                val messages_response = VKApiGetMessagesResponse(response!!.json)

                Log.d("VKSdk", "History: %s".format(messages_response.count))
//                val VKdialogs = (response!!.parsedModel as VKApiGetDialogResponse).items
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                Log.d("VKSdk", "History onError: %s".format(error.toString()))
            }

            override fun onProgress(progressType: VKRequest.VKProgressType?, bytesLoaded: Long, bytesTotal: Long) {
                super.onProgress(progressType, bytesLoaded, bytesTotal)
                Log.d("VKSdk", "History onProgress: %s".format(progressType.toString()))
            }
        }

        history.executeWithListener(listener)
    }

    fun openDialogActivity(dialog: VKApiDialog) {
        Log.d("VKSdk", "selected dialog: %s".format(dialog.message.toString()))

        dialog_id = dialog.message.user_id

        if (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_two, MessageView())
                    .commit()
        } else {
            getVKMessages(dialog.message.user_id)
            val intent = Intent(applicationContext, MessageActivity::class.java)
            intent.putExtra("user_id", dialog.message.user_id)
            startActivity(intent)
        }
    }
}
