package com.ex.kode.kodeex8

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiGetMessagesResponse
import com.vk.sdk.api.model.VKApiMessage
import com.vk.sdk.api.model.VKList
import kotlinx.android.synthetic.main.fragment_blank2.*

class MessageView : Fragment() {

    private var messagesAdapter: MessageAdapter? = null
    private var messagesList: RecyclerView? = null

    var callback: Callback? = null

    interface Callback {
        fun getDialogID(): Int
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = context as Callback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        messagesList = message_list
        messagesList?.layoutManager = LinearLayoutManager(context)

        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_blank2, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        getVKMessages(callback?.getDialogID())
    }

    fun getVKMessages(id: Int?) {
        // Get messages for dialog by id
        val history = VKRequest("messages.getHistory", VKParameters.from(VKApiConst.USER_ID, id))
        val listener: VKRequest.VKRequestListener = object: VKRequest.VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)

                val messages_response = VKApiGetMessagesResponse(response!!.json)

                updateMessages(messages_response.items)
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

    fun updateMessages(messages: VKList<VKApiMessage>) {
        if( messagesAdapter == null ) {
            if( messagesList == null ) {
                messagesList = message_list
                messagesList?.layoutManager = LinearLayoutManager(context)
            }

            messagesAdapter = MessageAdapter(messages)
            messagesList?.adapter = messagesAdapter
        } else
            messagesAdapter?.data = messages

        messagesAdapter?.notifyDataSetChanged()
    }
}
