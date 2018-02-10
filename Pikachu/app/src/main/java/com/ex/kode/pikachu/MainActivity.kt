package com.ex.kode.pikachu

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.DataPart
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var LOAD_GALLERY_IMAGE = 1
    private var LOAD_STYLE_IMAGE = 2
    private var BASE_IMAGE : String? = null
    private var STYLE_IMAGE : String? = null
    private var OUTPUT_IMAGE : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BASE_IMAGE = null

        // Load image from gallery
        findViewById<ImageButton>(R.id.button_image)
                .setOnClickListener { load_image() }

        // Colorize command
        findViewById<Button>(R.id.button_colorize)
                .setOnClickListener { colorize_image() }

        // Neuralize command
        findViewById<Button>(R.id.button_neurolize)
                .setOnClickListener { neurolize_image() }

        // Save image
        findViewById<Button>(R.id.button_save)
                .setOnClickListener { save_image() }

        findViewById<SeekBar>(R.id.seek).setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                output_image.alpha = progress.toFloat()/100
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    fun load_image() {
        set_loading(true)
        val intent: Intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, LOAD_GALLERY_IMAGE)
    }

    // Load image from Gallery to main_image elemnt
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        set_loading(true)
        if (requestCode == LOAD_GALLERY_IMAGE && resultCode == RESULT_OK && null != data) {
            val img_path = data.getData()
            Picasso.with(applicationContext).load(img_path).into(main_image)

            output_image.visibility = View.GONE
            seek.visibility = View.GONE
            button_save.visibility = View.GONE
            style_image.visibility = View.GONE

            set_loading(false)
            BASE_IMAGE = EXFile.getPath(applicationContext, img_path)
        } else {
            set_loading(false)
        }

        if (requestCode == LOAD_STYLE_IMAGE && resultCode == RESULT_OK && null != data) {
            val style_img_path = data.getData()
            STYLE_IMAGE = EXFile.getPath(applicationContext, style_img_path)
            style_image.visibility = View.VISIBLE
            Picasso.with(applicationContext).load(style_img_path).into(style_image)

            neurolize_request()
        } else {
            set_loading(false)
        }
    }

    fun colorize_image() {
        if( BASE_IMAGE != null ) {
            set_loading(true)
            val URL: String = "https://api.deepai.org/api/colorizer"
            FuelManager.instance.baseHeaders = mapOf("Api-Key" to resources.getString(R.string.api_key))
            Fuel.upload(URL).source{request, url -> File(BASE_IMAGE)}.name{"image"}.responseString { request, response, result ->
                when(result) {
                    is Result.Success -> { edit_image_response(true, result.get(), response) }
                    is Result.Failure -> { edit_image_response(false, result.get(), response) }
                }
            }
        } else {
            Toast.makeText(applicationContext, resources.getString(R.string.no_image), Toast.LENGTH_SHORT).show()
        }
    }

    fun edit_image_response(succes: Boolean, result: String, response: Response) {
        if( !succes ) {
            set_loading(false)
            Toast.makeText(applicationContext, resources.getString(R.string.error_load), Toast.LENGTH_SHORT).show()
        } else {
            update_output( Gson().fromJson(result, ImgResource::class.java) )
        }
    }

    fun neurolize_image() {
        if( BASE_IMAGE != null ) {
            set_loading(true)
            Toast.makeText(applicationContext, resources.getString(R.string.styling), Toast.LENGTH_LONG).show()
            val intent: Intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, LOAD_STYLE_IMAGE)
        } else {
            Toast.makeText(applicationContext, resources.getString(R.string.no_image), Toast.LENGTH_SHORT).show()
        }
    }

    fun neurolize_request() {
        val URL: String = "https://api.deepai.org/api/neural-style"
        FuelManager.instance.baseHeaders = mapOf("Api-Key" to resources.getString(R.string.api_key))
        Fuel.upload(URL).dataParts { request, url -> listOf(
                DataPart(File(BASE_IMAGE), "content"),
                DataPart(File(STYLE_IMAGE), "style")
        ) }.responseString { request, response, result ->
            when(result) {
                is Result.Success -> { edit_image_response(true, result.get(), response) }
                is Result.Failure -> { edit_image_response(false, result.get(), response) }
            }
        }
    }

    fun update_output(img: ImgResource) {
        OUTPUT_IMAGE = img.getURL()
        Picasso.with(applicationContext).load(img.getURL()).into(output_image, object: Callback {
            override fun onSuccess() { update_callback() }
            override fun onError() { update_callback() }
        })
    }

    fun update_callback() {
        set_loading(false)
        output_image.visibility = View.VISIBLE
        button_save.visibility = View.VISIBLE
        seek.visibility = View.VISIBLE
        seek.progress = 255
    }

    fun save_image() {
        saver.visibility = View.VISIBLE
        val names = OUTPUT_IMAGE.split("/")
        val name = names[names.size - 1]
        Picasso.with(applicationContext).load(OUTPUT_IMAGE).into(ImgSave(applicationContext, name))
        saver.visibility = View.GONE
    }

    fun set_loading(show: Boolean) {
        loader.visibility = if (show) View.VISIBLE else View.GONE
    }
}
