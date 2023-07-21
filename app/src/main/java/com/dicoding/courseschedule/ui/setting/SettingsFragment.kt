package com.dicoding.courseschedule.ui.setting

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NOTIFICATION_ID
import com.dicoding.courseschedule.util.NightMode
import java.util.Calendar
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var dailyReminderSwitch: SwitchPreferenceCompat
    private lateinit var dailyReminder: DailyReminder

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val themePreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        themePreference?.setOnPreferenceChangeListener { _, newValue ->
            val themeMode = NightMode.valueOf(newValue.toString().toUpperCase(Locale.US))
            updateTheme(themeMode.value)
            true
        }

        dailyReminder = DailyReminder()

        val dailyReminderSwitch = findPreference<SwitchPreferenceCompat>("daily_reminder_switch")
        dailyReminderSwitch?.setOnPreferenceChangeListener { _, newValue ->
            val isEnabled = newValue as Boolean
            if (isEnabled) {
                dailyReminder.setDailyReminder(requireContext())
            } else {
                dailyReminder.cancelAlarm(requireContext())
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean{
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}