package com.ex.kode.kodeex6

import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_full.*
import java.io.File

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullActivity : AppCompatActivity() {
    lateinit var imagePath: String
    lateinit var imageName: String

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_image.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
        fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_full)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        // Set image to Fullscreen Activity
        imagePath = intent.getStringExtra("image")
        fullscreen_image.setImageBitmap(BitmapFactory.decodeFile(imagePath))

        imageName = intent.getStringExtra("name")
        fullscreen_image_name.text = imageName

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_image.setOnClickListener { toggle() }

        delete_button.setOnClickListener(View.OnClickListener {
            var confirm = AlertDialog.Builder(this).create()
            confirm.setTitle(R.string.app_title)
            confirm.setMessage(resources.getString(R.string.alert_delete))
            confirm.setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    resources.getString(R.string.confirm),
                    {
                        _, _ -> onConfirm()
                    })
            confirm.setButton(
                    AlertDialog.BUTTON_NEGATIVE,
                    resources.getString(R.string.cancel),
                    {
                        _, _ -> onDecline()
                    }
            )
            confirm.show()
        })

        share_button.setOnClickListener(View.OnClickListener {
            var share = Intent(Intent.ACTION_SEND)
            share.putExtra(Intent.EXTRA_STREAM, imagePath)
            share.setType("image/%s".format(ImageObj.getExt(imagePath)))
            startActivity(share)
        })

        fullscreen_image_name.setOnClickListener(View.OnClickListener {
            var dialog = AlertDialog.Builder(this).create()
            dialog.setTitle(R.string.app_title)
            dialog.setMessage(resources.getString(R.string.dialog_rename))

            var inflater = layoutInflater
            var input: View = inflater.inflate(R.layout.dialog_rename, null)

            var edit: EditText = input.findViewById(R.id.image_edit_name)
            edit.setText(imageName)

            dialog.setView(input)
            dialog.setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    resources.getString(R.string.confirm),
                    {
                        _, _ -> confirmRename(edit)
                    }
            )
            dialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE,
                    resources.getString(R.string.cancel),
                    {
                        _, _ -> onDecline()
                    }
            )
            dialog.show()
        })

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        delete_button.setOnTouchListener(mDelayHideTouchListener)
    }

    fun onConfirm() {
        if( ImageObj.deleteFile(imagePath) ) {
            // Dark side code
            if( applicationContext is MainActivity )
                (applicationContext as MainActivity).updateLists()

            this.finish()
        } else
            Toast.makeText(applicationContext, resources.getString(R.string.file_error), Toast.LENGTH_SHORT).show()
    }

    fun onDecline() {}

    fun confirmRename(edit: EditText) {
        var new_name = edit.text.toString()

        if( new_name.length > 0 ) {
            if( ImageObj.checkFile(new_name) ) {
                if( imageName != new_name ) {
                    if( ImageObj.renameFile(imagePath, imageName, new_name) )
                        edit.setText(new_name)
                }
            } else
                Toast.makeText(applicationContext, resources.getString(R.string.file_ext_error), Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(applicationContext, resources.getString(R.string.file_rename_error), Toast.LENGTH_SHORT).show()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreen_image.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }
}
