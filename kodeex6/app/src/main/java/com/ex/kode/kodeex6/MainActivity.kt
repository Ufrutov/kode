package com.ex.kode.kodeex6

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.list_caption_dcim.*
import kotlinx.android.synthetic.main.list_caption_download.*
import kotlinx.android.synthetic.main.list_caption_pictures.*

class MainActivity : AppCompatActivity(), ListViewHolder.Callbacks {

    private val picutres_root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    private val dcim_root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
    private val download_root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    lateinit var list: RecyclerView
    lateinit var list_2: RecyclerView
    lateinit var list_3: RecyclerView

    var opened: String = ""
    var selected: MutableList<ImageObj> = arrayListOf()

    lateinit var pictures_files: Array<ImageObj>
    lateinit var dcim_files: Array<ImageObj>
    lateinit var download_files: Array<ImageObj>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        pictures_files = AdapterHelper.getImages(picutres_root)
        list = list_pictures
        list.layoutManager = GridLayoutManager(applicationContext, 3)
        list.adapter = ListAdapter(pictures_files, applicationContext)

        dcim_files = AdapterHelper.getImages(dcim_root)
        list_2 = list_dcim
        list_2.layoutManager = GridLayoutManager(applicationContext, 3)
        list_2.adapter = ListAdapter(dcim_files, applicationContext)

        download_files = AdapterHelper.getImages(download_root)
        list_3 = list_download
        list_3.layoutManager = GridLayoutManager(applicationContext, 3)
        list_3.adapter = ListAdapter(download_files, applicationContext)

        // Handle toggle functional for list caption bars
        handleBars()
        updateLists()
        handleFab()

        fab.setOnClickListener { _ -> openCamera() }
        del.setOnClickListener { _ -> deleteIems() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(applicationContext, "onActivityResult", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if( item.itemId == R.id.action_load )
            openLoadURL()

        return when (item.itemId) {
            R.id.action_load -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onTap(img: ImageObj, view: View) {
        if( img.select ) {
            // Deselect item
            img.select = false
            view.background = ContextCompat.getDrawable(this, R.drawable.deselected_item)
            selected.remove(img)
        } else {
            var fullIntent = Intent(applicationContext, FullActivity::class.java)
            fullIntent.putExtra("image", img.path)
            fullIntent.putExtra("name", img.name)
            fullIntent.putExtra("dir", img.dir)
            startActivity(fullIntent)
        }
        handleFab()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onLongTap(img: ImageObj, view: View) {
        if( img.select ) {
            selected.add(img)

            view.background = ContextCompat.getDrawable(this, R.drawable.selected_item)
        } else {
            // Deselect item
            view.background = ContextCompat.getDrawable(this, R.drawable.deselected_item)
            selected.remove(img)
        }
        handleFab()
    }

    fun handleFab() {
        if( selected.size > 0 ) {
            fab.visibility = View.GONE
            del.visibility = View.VISIBLE
        } else {
            fab.visibility = View.VISIBLE
            del.visibility = View.GONE
        }
    }

    fun deleteIems() {
        if( selected.size > 0 ) {
            var confirm = AlertDialog.Builder(this).create()
            confirm.setTitle(R.string.app_title)
            confirm.setMessage("%s (%s)".format(resources.getString(R.string.alert_delete_multiple), selected.size))
            confirm.setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    resources.getString(R.string.confirm),
                    {
                        _, _ -> onDelConfirm()
                    })
            confirm.setButton(
                    AlertDialog.BUTTON_NEGATIVE,
                    resources.getString(R.string.cancel),
                    {
                        _, _ -> onDelDecline()
                    }
            )
            confirm.show()
        }
    }

    fun onDelConfirm() {
        for ( i in selected ) {
            ImageObj.deleteFile(i.path)
        }
        selected = arrayListOf()
        handleFab()
        updateLists()
    }

    fun onDelDecline() {}

    fun openCamera() {
        var intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

    fun openLoadURL() {
        var intent = Intent(applicationContext, LoadActivity::class.java)
        startActivity(intent)
    }

    fun handleBars() {
        var pictures_bar: ConstraintLayout = findViewById(R.id.list_caption_pictures)
        pictures_bar.setOnClickListener(View.OnClickListener {
            if( list.visibility == View.VISIBLE ) {
                list.visibility = View.GONE
                caption_pictures_img_up.visibility = View.GONE
                caption_pictures_img_down.visibility = View.VISIBLE
                opened.replace("1", "")
            } else {
                list.visibility = View.VISIBLE
                caption_pictures_img_up.visibility = View.VISIBLE
                caption_pictures_img_down.visibility = View.GONE
                opened += "1"
            }
        })

        var dcim_bar: ConstraintLayout = findViewById(R.id.list_caption_dcim)
        dcim_bar.setOnClickListener(View.OnClickListener {
            if( list_2.visibility == View.VISIBLE ) {
                list_2.visibility = View.GONE
                caption_dcim_img_up.visibility = View.GONE
                caption_dcim_img_down.visibility = View.VISIBLE
                opened.replace("2", "")

            } else {
                list_2.visibility = View.VISIBLE
                caption_dcim_img_up.visibility = View.VISIBLE
                caption_dcim_img_down.visibility = View.GONE
                opened += "2"
            }
        })

        var download_bar: ConstraintLayout = findViewById(R.id.list_caption_download)
        download_bar.setOnClickListener(View.OnClickListener {
            if( list_3.visibility == View.VISIBLE ) {
                list_3.visibility = View.GONE
                caption_download_img_up.visibility = View.GONE
                caption_download_img_down.visibility = View.VISIBLE
                opened.replace("3", "")
            } else {
                list_3.visibility = View.VISIBLE
                caption_download_img_up.visibility = View.VISIBLE
                caption_download_img_down.visibility = View.GONE
                opened += "3"
            }
        })

        if (opened.indexOf("1") != -1) {
            list.visibility = View.VISIBLE
            caption_pictures_img_up.visibility = View.VISIBLE
            caption_pictures_img_down.visibility = View.GONE
        }

        if (opened.indexOf("2") != -1) {
            list_2.visibility = View.VISIBLE
            caption_dcim_img_up.visibility = View.VISIBLE
            caption_dcim_img_down.visibility = View.GONE
        }

        if (opened.indexOf("3") != -1) {
            list_3.visibility = View.VISIBLE
            caption_download_img_up.visibility = View.VISIBLE
            caption_download_img_down.visibility = View.GONE
        }
    }

    fun updateBars() {
        var pictures_title = list_caption_pictures_title
        pictures_title.text = "%s (%s)".format(
                resources.getString(R.string.list_caption_pictures),
                pictures_files.size)

        var dcim_title = list_caption_dcim_title
        dcim_title.text = "%s (%s)".format(
                resources.getString(R.string.list_caption_dcim),
                dcim_files.size)

        var download_title = list_caption_download_title
        download_title.text = "%s (%s)".format(
                resources.getString(R.string.list_caption_download),
                download_files.size)
    }

    fun updateLists() {
        if( list.visibility == View.VISIBLE ) {
            pictures_files = AdapterHelper.getImages(picutres_root)
            list.adapter.notifyDataSetChanged()
        }

        if( list_2.visibility == View.VISIBLE ) {
            dcim_files = AdapterHelper.getImages(dcim_root)
            list_2.adapter.notifyDataSetChanged()
        }

        if( list_3.visibility == View.VISIBLE ) {
            download_files = AdapterHelper.getImages(download_root)
            list_3.adapter.notifyDataSetChanged()
        }

        updateBars()
    }

    override fun onRestoreInstanceState(saved: Bundle?) {
        if( saved != null )
            opened = saved.getString("opened")
        super.onRestoreInstanceState(saved)
    }

    override fun onSaveInstanceState(saved: Bundle?) {
        if( saved != null )
            saved.putString("opened", opened)
        super.onSaveInstanceState(saved)
    }
}
