package com.ex.kode.kodeex4

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        var web_view: WebView = web
        var plant: String = intent.getStringExtra("planet")
        var url: String = intent.getStringExtra("url")

        if( url.length > 0 ) {
            web_view.webViewClient = WebViewClient()
            web_view.loadUrl(url)
        } else
            Toast.makeText(applicationContext, R.string.url_error, Toast.LENGTH_SHORT).show()
    }
}
