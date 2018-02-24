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
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), MessageView.Callback {

    private var dialogAdapter: DialogAdapter? = null
    private lateinit var dialogs: RecyclerView

    override fun getDialogID(): Int {
        return dialog_id
    }

    var dialog_id: Int = 0

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

                val vkDialogs = (response!!.parsedModel as VKApiGetDialogResponse).items
                val users: Array<String> = Array(vkDialogs.size, { i -> vkDialogs[i].message.user_id.toString() })

                completeDialogs(users, vkDialogs)
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

    fun updateDialogList(data: ArrayList<VKDialog>) {
        if( dialogAdapter == null ) {
            dialogAdapter = DialogAdapter(data,
                object: DialogAdapter.ItemClickListener {
                    override fun onListItemClick(item: VKDialog) {
                        openDialogActivity(item)
                    }

            })
            dialogs.adapter = dialogAdapter
        } else {
            dialogAdapter?.data = data
            dialogAdapter?.notifyDataSetChanged()
        }
    }

    fun completeDialogs(ids: Array<String>, dialogs: VKList<VKApiDialog>) {
        // Get user names by id from dialogs
        val users = VKRequest("users.get", VKParameters.from(VKApiConst.USER_IDS, ids.joinToString()))
        val listener: VKRequest.VKRequestListener = object: VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)

                val ja: JSONArray = JSONObject(response!!.responseString).getJSONArray("response")

                val output = ArrayList<VKDialog>()
                for( d: VKApiDialog in dialogs ) {
                    val index: Int = dialogs.indexOf(d)
                    val obj = ja.getJSONObject(index)

                    if( d.message.user_id == obj.getInt("id") )
                        output.add(VKDialog(
                                obj.getInt("id"),
                                obj.getString("last_name"),
                                obj.getString("first_name"),
                                d.message.date,
                                d.message.body,
                                ( d.unread != 0 ),
                                d.message.out                      ))
                    else
                        Log.d("VKSdk", "Dialog error: %s".format(obj.getInt("id")))
                }

                updateDialogList(output)

                Log.d("VKSdk", "Users onComplete: %s".format(output.size))
            }

            override fun onError(error: VKError?) {
                super.onError(error)
                Log.d("VKSdk", "Users onError: %s".format(error.toString()))
            }

            override fun onProgress(progressType: VKRequest.VKProgressType?, bytesLoaded: Long, bytesTotal: Long) {
                super.onProgress(progressType, bytesLoaded, bytesTotal)
                Log.d("VKSdk", "Users onProgress: %s".format(progressType.toString()))
            }
        }

        users.executeWithListener(listener)
    }

    fun getVKMessages(id: Int) {
        // Get messages for dialog by id
        val history = VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id))
        val listener: VKRequest.VKRequestListener = object: VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)

                val messages_response = VKApiGetMessagesResponse(response!!.json)

                Log.d("VKSdk", "History: %s".format(messages_response.count))
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

    fun openDialogActivity(dialog: VKDialog) {
        Log.d("VKSdk", "selected dialog: %s (%s)".format(dialog.getName(), dialog.id.toString()))

        dialog_id = dialog.id

        if (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_two, MessageView())
                    .commit()
        } else {
            getVKMessages(dialog.id)
            val intent = Intent(applicationContext, MessageActivity::class.java)
            intent.putExtra("user_id", dialog.id)
            startActivity(intent)
        }
    }
}
