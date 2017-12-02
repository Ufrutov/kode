package com.ex.kode.kodeex2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var caption_input: EditText

    lateinit var img_path: String

    val REQUEST_IMAGE_CAPTURE = 1

    val FILE_PROVIDER = "com.ex.kode.kodeex2.fileprovider"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var caption_input: EditText = findViewById(R.id.img_caption)

        var size_check: CheckBox = findViewById(R.id.photo_large)

        // Button handler
        var startBtn: Button = findViewById(R.id.img_proceed)
        startBtn.setOnClickListener(View.OnClickListener {

            var caption: String = caption_input.text.toString()

            if (caption.length < 1)
                Toast.makeText(applicationContext, R.string.error, Toast.LENGTH_SHORT).show()
            else {
                // Start next activity with phone camera
                // Switch to thubnail mode or full-size photo

                var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                var intent = Intent(applicationContext, CameraActivity::class.java)

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    intent.putExtra("mode", if ( size_check.isChecked() ) "full" else "thumb")

                    if( size_check.isChecked() ) {
                        var photo_file = createImageFile()

                        var photoURI: Uri? = FileProvider.getUriForFile(this, FILE_PROVIDER, photo_file)

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    } else
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
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
        } else {
            var caption_input: EditText = findViewById(R.id.img_caption)
            caption_input.setText(intent.getStringExtra("caption"))

            Toast.makeText(applicationContext, "Camera response error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()

        var caption_input: EditText = findViewById(R.id.img_caption)
        caption_input.setText("")
    }

    fun createImageFile(): File? {
        val time : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        var newimg = String.format("JPEG_%s_", time)

        var dir_path = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        var image = File.createTempFile(newimg, ".jpg", dir_path)

        img_path = image.getAbsolutePath()

        return image
    }
}
