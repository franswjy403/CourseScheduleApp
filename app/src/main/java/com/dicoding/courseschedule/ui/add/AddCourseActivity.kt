package com.dicoding.courseschedule.ui.add

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity() {

    private lateinit var startTimeButton: ImageButton
    private lateinit var endTimeButton: ImageButton
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView
    private lateinit var viewModel: AddCourseViewModel

    private var selectedStartTime: Calendar = Calendar.getInstance()
    private var selectedEndTime: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        startTimeButton = findViewById(R.id.ib_start_time)
        endTimeButton = findViewById(R.id.ib_end_time)
        startTimeTextView = findViewById(R.id.tv_input_start_time)
        endTimeTextView = findViewById(R.id.tv_input_end_time)

        startTimeButton.setOnClickListener {
            showTimePickerDialog(selectedStartTime, startTimeTextView)
        }

        endTimeButton.setOnClickListener {
            showTimePickerDialog(selectedEndTime, endTimeTextView)
        }

        viewModel.saved.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { isSaved ->
                if (isSaved) {
                    finish()
                } else {
                    Toast.makeText(this, "Insertion failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val courseName = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString()
                val day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition
                val startTime = findViewById<TextView>(R.id.tv_input_start_time).text.toString()
                val endTime = findViewById<TextView>(R.id.tv_input_end_time).text.toString()
                val lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString()
                val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString()

                viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTimePickerDialog(calendar: Calendar, textView: TextView) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minuteOfDay ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minuteOfDay)
                updateTimeTextView(calendar, textView)
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    private fun updateTimeTextView(calendar: Calendar, textView: TextView) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        textView.text = timeString
    }
}