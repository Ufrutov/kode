package com.ex.kode.kodeex6

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.GridLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    var pattern: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        var caption = search_caption
        var list = list_search
        var files: Array<ImageObj> = arrayOf()
        list.layoutManager = GridLayoutManager(applicationContext, 3)
        list.adapter = ListAdapter(files, applicationContext)

        search_button.setOnClickListener(View.OnClickListener {
            var search = search_text.text.toString()
            if( search.length > 0 ) {
                if( pattern != search ) {
                    var root = Environment.getExternalStorageDirectory()
                    files = AdapterHelper.getImages(root, search)

                    list.adapter.notifyDataSetChanged()

                    if(files.size > 0) {
                        caption.text = resources.getString(R.string.search_found).format(files.size)
                    } else {
                        caption.text = resources.getString(R.string.search_nothing)
                    }
                }
            }
        })
    }
}
