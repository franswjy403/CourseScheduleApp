package com.dicoding.courseschedule.ui

import android.content.ComponentName
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.courseschedule.ui.add.AddCourseActivity
import com.dicoding.courseschedule.ui.home.HomeActivity
import com.dicoding.courseschedule.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun testAddCourseButton() {
        Espresso.onView(withId(R.id.action_add)).perform(ViewActions.click())

        Intents.intended(IntentMatchers.anyIntent())

        val expectedComponent = ComponentName(InstrumentationRegistry.getTargetContext(), AddCourseActivity::class.java)
        Intents.intended(IntentMatchers.hasComponent(expectedComponent))
    }
}