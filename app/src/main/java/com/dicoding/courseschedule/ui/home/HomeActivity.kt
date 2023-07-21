package com.dicoding.courseschedule.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import com.dicoding.courseschedule.ui.list.ListActivity
import com.dicoding.courseschedule.ui.setting.SettingsActivity
import com.dicoding.courseschedule.util.DayName
import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.timeDifference

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private var queryType = QueryType.CURRENT_DAY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = resources.getString(R.string.today_schedule)

        val cardHome = findViewById<CardHomeView>(R.id.view_home)
        val emptyText = findViewById<TextView>(R.id.tv_empty_home)

        val factory = HomeViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        viewModel.todayCourses.observe(this) { course ->
            showTodaySchedule(course, cardHome, emptyText)
        }
    }

    private fun showTodaySchedule(course: Course?, cardHome: CardHomeView, emptyText: TextView) {
        checkQueryType(course)
        if (course != null) {
            val dayName = DayName.getByNumber(course.day)
            val time = String.format(getString(R.string.time_format), dayName, course.startTime, course.endTime)
            val remainingTime = timeDifference(course.day, course.startTime)

            cardHome.setCourseName(course.courseName)
            cardHome.setTime(time)
            cardHome.setRemainingTime(remainingTime)
            cardHome.setLecturer(course.lecturer)
            cardHome.setNote(course.note)

            cardHome.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        } else {
            cardHome.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        }
    }

    private fun checkQueryType(course: Course?) {
        if (course == null) {
            val newQueryType: QueryType = when (queryType) {
                QueryType.CURRENT_DAY -> QueryType.NEXT_DAY
                QueryType.NEXT_DAY -> QueryType.PAST_DAY
                else -> QueryType.CURRENT_DAY
            }
            viewModel.setQueryType(newQueryType)
            queryType = newQueryType
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent? = when (item.itemId) {
            R.id.action_settings -> Intent(this, SettingsActivity::class.java)
            R.id.action_add -> Intent(this, AddCourseActivity::class.java)
            R.id.action_list -> Intent(this, ListActivity::class.java)
            else -> null
        }
        intent?.let {
            startActivity(it)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
