package com.ex.kode.kodeex2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var caption_input: EditText

    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var caption_input: EditText = findViewById(R.id.img_caption)

        // Button handler
        var startBtn: Button = findViewById(R.id.img_proceed)
        startBtn.setOnClickListener(View.OnClickListener {

            var caption: String = caption_input.text.toString()

            if (caption.length < 1)
                Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
            else {
                // Start next activity with phone camera

                var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                if (takePictureIntent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            var photo: Bitmap = data.getExtras().get("data") as Bitmap

            if( photo == null )
                Toast.makeText(applicationContext, "NULL Photo", Toast.LENGTH_SHORT).show()

            var intent = Intent(applicationContext, CameraActivity::class.java)

            var caption_input: EditText = findViewById(R.id.img_caption)
            var caption: String = caption_input.text.toString()

            intent.putExtra("caption", caption)
            intent.putExtra("photo", photo)

            startActivity(intent)
        } else
            Toast.makeText(applicationContext, "Camera response error", Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()

        var caption_input: EditText = findViewById(R.id.img_caption)
        caption_input.setText("")
    }
}
