package com.ex.kode.kodeex6

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_load.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class LoadActivity : AppCompatActivity() {

    var img_url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)

        var btn = btn_load
        btn.setOnClickListener(View.OnClickListener {
            var input = input_url

            if( input.text.toString().length > 0 ) {
                if( img_url != input.text.toString() ) {
                    Toast.makeText(applicationContext, resources.getString(R.string.start_download), Toast.LENGTH_SHORT).show()
                    LoadFile(input.text.toString(), image_loaded).execute(input.text.toString(), null, null)
                } else {
                    Toast.makeText(applicationContext, resources.getString(R.string.url_exist), Toast.LENGTH_SHORT).show()
                    input.setText("")
                }
            } else {
                Toast.makeText(applicationContext, resources.getString(R.string.url_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    inner class LoadFile(var uri: String, var el: ImageView): AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg p0: String?): Bitmap {
            var ext = uri.split(".")[uri.split(".").size-1]
            var ts = (System.currentTimeMillis() / 1000).toString();

            var file = File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS), "%s.%s".format(ts, ext))

            file.createNewFile()
            var fos = FileOutputStream(file)

            img_url = uri

            var url = URL(p0[0])
            fos.write(url.readBytes())
            fos.close()

            return BitmapFactory.decodeFile(file.absolutePath)
        }

        override fun onPostExecute(result: Bitmap?) {
    //        super.onPostExecute(result)
            el.setImageBitmap(result)
        }
    }
}
