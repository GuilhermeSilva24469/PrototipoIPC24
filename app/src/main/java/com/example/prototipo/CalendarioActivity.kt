package com.example.prototipo

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat

class CalendarioActivity : Activity() {

    private val reminders = mapOf(
        "2024-06-20" to listOf("Meeting at 10 AM", "Doctor appointment at 3 PM"),
        "2024-06-21" to listOf("Lunch with Sarah at 1 PM"),
        "2024-06-22" to listOf("Gym at 6 PM", "Dinner with John at 7 PM")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val typefaceBold = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")
        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        // Create main layout
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setPadding(32, 32, 32, 32)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }

        // Create CalendarView
        val calendarView = CalendarView(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }
        mainLayout.addView(calendarView)

        // Create TextView for selected date
        val selectedDateTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            textSize = 24f
            typeface = typefaceBold
            setPadding(0, 16, 0, 16)
        }
        mainLayout.addView(selectedDateTextView)

        // Create ListView for reminders
        val remindersListView = ListView(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 0).apply {
                weight = 1f
            }
        }
        mainLayout.addView(remindersListView)

        // Set initial reminders for today's date
        val today = android.text.format.DateFormat.format("dd-MM-yyyy", calendarView.date).toString()
        selectedDateTextView.text = today
        updateReminders(today, remindersListView)

        // Set listener for date changes
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
            selectedDateTextView.text = selectedDate
            updateReminders(selectedDate, remindersListView)
        }


        // Set the main layout as the content view
        setContentView(mainLayout)
    }

    private fun updateReminders(date: String, listView: ListView) {
        val reminderList = reminders[date] ?: listOf("Não tem lembretes para este dia.")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reminderList)
        listView.adapter = adapter
    }
}
