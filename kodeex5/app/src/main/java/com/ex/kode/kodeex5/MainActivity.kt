package com.ex.kode.kodeex5

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.caption_active.*
import kotlinx.android.synthetic.main.caption_done.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.task_form.*

class MainActivity : AppCompatActivity() {
    var edit: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateList()

        val task_form: View = findViewById(R.id.include)
        var btn_new_task: FloatingActionButton = findViewById(R.id.btn_new_task)
        val btn_task: Button = task_button
        val btn_delete: Button = delete_button

        btn_new_task.setOnClickListener(View.OnClickListener {
            edit = -1
            btn_delete.visibility = View.GONE
            btn_task.text = resources.getString(R.string.add_task)
            task_form.visibility = View.VISIBLE

            var task_text_input: EditText = findViewById(R.id.task_text_input)
            task_text_input.setText("")

            var check_active: CheckBox = findViewById(R.id.active_check)
            check_active.isChecked = true
        })

        btn_task.setOnClickListener(View.OnClickListener {
            // Add new task handler
            if( btn_task.text == resources.getString(R.string.add_task) ) {
                if( addNewTask() != null ) {
                    // New task was succesfully added
                    task_form.visibility = View.GONE
                    var text_input = task_text_input
                    text_input.text = null

                    updateList()
                }
            } else {
                if( edit != -1 ) {
                    updateTask()
                }
            }
        })

        btn_delete.setOnClickListener(View.OnClickListener {
            removeTask()
        })

        var caption_active_el = caption_active
        caption_active_el.setOnClickListener(View.OnClickListener {
            toggleList("active_list")
        })

        var caption_done_el = caption_done
        caption_done_el.setOnClickListener(View.OnClickListener {
            toggleList("done_list")
        })
    }

    fun getDate(): String {
        return (System.currentTimeMillis()/1000).toString()
    }

    fun toggleList(mode: String) {
        if( mode == "active_list" ) {
            var active_list = list_1
            var active_caption_img = caption_active_img

            if (active_list.visibility == View.VISIBLE) {
                active_caption_img.setImageResource(R.drawable.down)
                active_list.visibility = View.GONE
            } else {
                active_caption_img.setImageResource(R.drawable.up)
                active_list.visibility = View.VISIBLE
            }
        }

        if( mode == "done_list" ) {
            var done_list = list_2
            var done_caption_img = caption_done_img

            if (done_list.visibility == View.VISIBLE) {
                done_caption_img.setImageResource(R.drawable.down)
                done_list.visibility = View.GONE
            } else {
                done_caption_img.setImageResource(R.drawable.up)
                done_list.visibility = View.VISIBLE
            }
        }
    }

    fun updateList() {
        var tasks: Array<ArrayList<Task>> = getTasks()

        var tasks_1 = Array(tasks.get(0).size, { i -> tasks.get(0).get(i) })

        var task_list_1 = list_1
        task_list_1.layoutManager = LinearLayoutManager(applicationContext)
        task_list_1.adapter = ListAdapter(tasks_1, this)
        task_list_1.adapter.notifyDataSetChanged()

        var tasks_2 = Array(tasks.get(1).size, { i -> tasks.get(1).get(i) })

        var task_list_2 = list_2
        task_list_2.layoutManager = LinearLayoutManager(applicationContext)
        task_list_2.adapter = ListAdapter(tasks_2, this)
        task_list_2.adapter.notifyDataSetChanged()

        updateCaption(task_list_1, task_list_2)
    }

    fun updateCaption(l1: RecyclerView, l2: RecyclerView) {
        var active_caption = caption_active_title
        active_caption.text = "%s: (%s)".format(resources.getString(R.string.tasks_active), l1.adapter.itemCount)

        var done_caption = caption_done_title
        done_caption.text = "%s: (%s)".format(resources.getString(R.string.tasks_done), l2.adapter.itemCount)
    }

    fun addNewTask(): Long? {
        var task_text_el = task_text_input
        var task_text: String = task_text_el.text.toString()

        if( task_text.length > 10 ) {
            var task_date: String = getDate()
            var check_active: CheckBox = findViewById(R.id.active_check)
            var active: Boolean = check_active.isChecked

            val db: SQLiteDatabase = DBHelper(applicationContext).writableDatabase
            val values = ContentValues()

            values.put(DBHelper.COL_TEXT, task_text)
            values.put(DBHelper.COL_ACTIVE, active)
            values.put(DBHelper.COL_DATE, task_date)

            val task_id = db.insert(DBHelper.SQL_TABLE, null, values)

            Toast.makeText(applicationContext, "Task added (id: %s, active: %s)".format(task_id, active), Toast.LENGTH_SHORT).show()

            return task_id
        } else {
            Toast.makeText(applicationContext, R.string.task_text_error, Toast.LENGTH_SHORT).show()

            return null
        }
    }

    fun updateTask() {
        var task_text_el = task_text_input
        var task_text: String = task_text_el.text.toString()

        if( task_text.length > 5 ) {
            var task_date: String = getDate()
            var check_active: CheckBox = findViewById(R.id.active_check)
            var active: Boolean = check_active.isChecked

            val db: SQLiteDatabase = DBHelper(applicationContext).writableDatabase

            val values = ContentValues()

            values.put(DBHelper.COL_TEXT, task_text)
            values.put(DBHelper.COL_ACTIVE, active)
            values.put(DBHelper.COL_DATE, task_date)

            val select: String = DBHelper.COL_ID + " = ?"
            val selectArgs: Array<String> = arrayOf(edit.toString())

            val count: Int = db.update(DBHelper.SQL_TABLE, values, select, selectArgs)

            if( count == 1 ) {
                edit = -1

                val form: View = findViewById(R.id.include)
                form.visibility = View.GONE

                updateList()
            } else
                Toast.makeText(applicationContext, resources.getString(R.string.update_error), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, R.string.task_text_error, Toast.LENGTH_SHORT).show()
        }
    }

    fun getTasks(): Array<ArrayList<Task>> {
        val db: SQLiteDatabase = DBHelper(applicationContext).writableDatabase

        val projection: Array<String> = arrayOf(
                DBHelper.COL_ID,
                DBHelper.COL_TEXT,
                DBHelper.COL_ACTIVE,
                DBHelper.COL_DATE)

        val sort: String = DBHelper.COL_DATE + " DESC"

        val cursor: Cursor = db.query(DBHelper.SQL_TABLE, projection, null, null, null, null, sort)

        var active_tasks: ArrayList<Task> = ArrayList<Task>()
        var done_tasks: ArrayList<Task> = ArrayList<Task>()
        var i: Int = 0

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COL_ID)).toInt()
            val text = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TEXT))
            val active = if ( cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_ACTIVE)) == 1 ) true else false
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_DATE))
            if( active )
                active_tasks.add(Task(id, text, active, date))
            else
                done_tasks.add(Task(id, text, active, date))
            i++
        }

        cursor.close()

        return arrayOf(active_tasks, done_tasks)
    }

    fun toggleTask(id: Int, active: Boolean) {
        val db: SQLiteDatabase = DBHelper(applicationContext).writableDatabase
        val values = ContentValues()

        values.put(DBHelper.COL_ACTIVE, active)
        values.put(DBHelper.COL_DATE, getDate())

        val select: String = DBHelper.COL_ID + " = ?"
        val selectArgs: Array<String> = arrayOf(id.toString())

        val count: Int = db.update(DBHelper.SQL_TABLE, values, select, selectArgs)

        if( count == 1 )
            updateList()
        else
            Toast.makeText(applicationContext, resources.getString(R.string.update_error), Toast.LENGTH_SHORT).show()
    }

    fun showUpdateForm(task: Task) {
        edit = task.id
        val form: View = findViewById(R.id.include)
        form.visibility = View.VISIBLE

        val del_btn: Button = findViewById(R.id.delete_button)
        del_btn.visibility = View.VISIBLE

        val task_btn: Button = findViewById(R.id.task_button)
        task_btn.text = resources.getString(R.string.update_task)

        var task_text_el: EditText = findViewById(R.id.task_text_input)
        task_text_el.setText(task.text)

        var check_active: CheckBox = findViewById(R.id.active_check)
        check_active.isChecked = task.active
    }

    fun removeTask() {
        if( edit != -1 ) {
            val db: SQLiteDatabase = DBHelper(applicationContext).writableDatabase

            val select: String = DBHelper.COL_ID + " = ?"
            val selectArgs: Array<String> = arrayOf(edit.toString())

            db.delete(DBHelper.SQL_TABLE, select, selectArgs)

            val form: View = findViewById(R.id.include)
            form.visibility = View.GONE

            edit = -1
            updateList()
        }
    }
}
