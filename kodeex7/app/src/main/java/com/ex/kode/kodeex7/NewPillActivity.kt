package com.ex.kode.kodeex7

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pill.*
import java.util.*
import kotlin.collections.HashMap

open class NewPillActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pill)

        fillForm()

        val date: Calendar = Calendar.getInstance()
        val c_y = date.get(Calendar.YEAR).toInt()
        val c_m = date.get(Calendar.MONTH).toInt()
        val c_d = date.get(Calendar.DAY_OF_MONTH).toInt()

        findViewById<EditText>(R.id.pill_start_input).setOnClickListener(View.OnClickListener {
            val dp = DatePickerDialog(this, DatePickerDialog.OnDateSetListener {
                _, year, month, day ->
                findViewById<EditText>(R.id.pill_start_input).setText("%s.%s.%s".format("%02d".format(day), "%02d".format(month+1), year))
            }, c_y, c_m, c_d)
            dp.show()
        })

        findViewById<EditText>(R.id.pill_time_input).setOnClickListener(View.OnClickListener {
            val tp = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                _, h, m ->
                findViewById<EditText>(R.id.pill_time_input).setText("%s:%s".format("%02d".format(h), "%02d".format(m)))
            }, 12,0, true)
            tp.show()
        })

        val btn_img: Button = findViewById(R.id.pill_img_edit)
        btn_img.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "R.id.pill_img_edit", Toast.LENGTH_SHORT).show()
        })

        val btn_add: Button = findViewById(R.id.pill_add)
        btn_add.setOnClickListener(View.OnClickListener {
            getNewPillData()
        })

        pill_add.visibility = View.VISIBLE
    }

    fun fillForm() {
        // Populate *First day* field
        val first_day_input: EditText = findViewById(R.id.pill_start_input)
        first_day_input.setText(Pill.getDate(true))

        // Populate *Pill time* field
        val pill_time_input: EditText = findViewById(R.id.pill_time_input)
        pill_time_input.setText("12:00")

        // Populate *Regard to meal* spinner
        val meal_input: Spinner = findViewById(R.id.pill_meal_input)
        val meal_adapter: ArrayAdapter<String> = ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.pill_meal_related_values))
        meal_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        meal_input.adapter = meal_adapter

        // Populate *Meal delay* spinner
        val delay_input: Spinner = findViewById(R.id.pill_meal_delay_input)
        val delay_adapter: ArrayAdapter<String> = ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.pill_meal_delay_values))
        delay_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        delay_input.adapter = delay_adapter
    }

    fun getPillForm(): Pill? {
        val field_name: EditText = findViewById(R.id.pill_name_input)
        val name = field_name.text.toString().trim()

        val field_desc: EditText = findViewById(R.id.pill_desc_input)
        val desc = field_desc.text.toString().trim()

        val field_notes: EditText = findViewById(R.id.pill_note_input)
        val notes = field_notes.text.toString().trim()

        // (+) Check Pill image
        // var pic_el: ImageView = findViewById(R.id.pill_image)

        if( name.isNotEmpty() ) {
            return Pill(
                    -1,
                    name,
                    if (notes.isNotEmpty()) desc else name,
                    if (notes.isNotEmpty()) notes else name,
                    Gson().toJson(getSchedule()),
                    "pic"
                    )
        } else
            return null
    }

    fun getNewPillData() {
        val pill = getPillForm()

        if( pill != null ) {
            if( pill.addNewPill(applicationContext, pill) != null ) {
                finish()
            } else
                Toast.makeText(applicationContext, resources.getString(R.string.input_error), Toast.LENGTH_LONG).show()
        } else
             Toast.makeText(applicationContext, resources.getString(R.string.input_error), Toast.LENGTH_LONG).show()
    }

    fun getSchedule(): HashMap<String, String> {
        val output: HashMap<String, String> = HashMap()

        val start_input = pill_start_input
        output.set("start", start_input.text.toString())

        val pill_time = pill_time_input
        output.set("time", pill_time.text.toString())

        val meal_input = pill_meal_input
        var meal = "no"
        when( meal_input.selectedItem.toString() ) {
            resources.getString(R.string.eat_before) -> meal = "before"
            resources.getString(R.string.eat_after) -> meal = "after" }
        output.set("meal", meal)

        val delay = pill_meal_delay_input
        output.set("m_delay", delay.selectedItem.toString())

        return output
    }
}
