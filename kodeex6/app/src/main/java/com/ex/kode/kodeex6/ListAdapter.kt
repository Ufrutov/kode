package com.ex.kode.kodeex6

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.list_item.view.*
import java.io.File

class ListAdapter(var data:Array<ImageObj>, var ctx: Context): RecyclerView.Adapter<ListViewHolder>() {
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder =
            ListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item,
                    parent, false))

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(data[position], holder.view)

        var image: ImageObj = data[position]

        if( image.select )
            holder.view.background = ContextCompat.getDrawable(ctx, R.drawable.selected_item)

        try {
            var options = BitmapFactory.Options()
            options.outWidth = 120
            options.outHeight = 120

            holder.img_el.setImageBitmap(BitmapFactory.decodeFile(image.path, options))
        } catch (e: Exception) {
            Log.w("[E] ListAdapter:", "error on file decoding: %s".format(image.path))
        }
    }
}

class ListViewHolder(var view: View): RecyclerView.ViewHolder(view) {
    interface Callbacks {
        fun onTap(img: ImageObj, view: View)
        fun onLongTap(img: ImageObj, view: View)
    }

    private lateinit var callback: Callbacks

    var img_el: ImageView = view.image_el

    fun bind(img: ImageObj, view: View) {
        callback = itemView.context as Callbacks

        view.setOnClickListener(View.OnClickListener {
            callback.onTap(img, view)
        })

        view.setOnLongClickListener(View.OnLongClickListener {
            img.select = !img.select
            callback.onLongTap(img, view)
            return@OnLongClickListener true
        })
    }
}

class AdapterHelper() {
    companion object {
        fun getImages(root: File, search: String?): Array<ImageObj> {
            var files_array: ArrayList<File> = ArrayList<File>()
            getFiles(root, search, files_array)

            if( files_array.size > 0 ) {
                return Array(files_array.size, { i ->
                    ImageObj(
                            files_array[i].absolutePath,
                            files_array[i].name,
                            false,
                            root)
                })
            } else {
                Log.w("[E] AdapterHelper:", "files error".format(files_array.size))
                return arrayOf<ImageObj>()
            }
        }

        fun getFiles(file: File, search: String?, files: ArrayList<File>) {
            for (f:File in file.listFiles()) {
                if( f.isDirectory ) {
                    getFiles(f, search, files)
                } else {
                    if( ImageObj.checkFile(f.absolutePath) ) {
                        if( search != null ) {
                            if( file.name.indexOf(search) != -1 )
                                files.add(f)
                        } else
                            files.add(f)
                    }
                }
            }
        }
    }
}