package com.ex.kode.kodeex2

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.IOException

class CameraActivity : AppCompatActivity() {

    lateinit var img_caption: TextView
    lateinit var photo_view: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        img_caption = findViewById(R.id.caption)

        var text_caption = intent.getStringExtra("caption")

        if( text_caption.length > 0 )
            img_caption.text = text_caption
        else {
            img_caption.text = "Caption Error"
            Toast.makeText(applicationContext, "Photo caption error", Toast.LENGTH_SHORT).show()
        }

        photo_view = findViewById(R.id.img)

        var mode = intent.getStringExtra("mode")

        try {

            if( mode == "thumb" ) {
                var photo = intent.getParcelableExtra<Bitmap>("photo") as Bitmap
                photo_view.setImageBitmap(photo)
            } else {
                var photo = intent.getStringExtra("photo")
                Toast.makeText(applicationContext, "Large photo success", Toast.LENGTH_SHORT).show()
            }

        } catch(e: IOException) {
            Toast.makeText(applicationContext, "Photo file error", Toast.LENGTH_SHORT).show()
        }

//        Return to Main activity
        var back_btn: Button = findViewById(R.id.btn_back)

        back_btn.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
}
