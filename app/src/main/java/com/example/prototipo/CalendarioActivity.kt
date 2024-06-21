package com.example.prototipo

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        // Create BottomNavigationView
        val bottomNavigationView = BottomNavigationView(this).apply {
            id = View.generateViewId()
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
            itemIconTintList = ContextCompat.getColorStateList(context, android.R.color.black)
            itemTextColor = ContextCompat.getColorStateList(context, android.R.color.black)
            menu.add(0, R.id.navigation_horario, 0, "Horário").apply {
                icon = ContextCompat.getDrawable(context, R.drawable.ic_horario)?.apply {
                    setTint(ContextCompat.getColor(context, android.R.color.black))
                }
            }
            menu.add(0, R.id.navigation_homepage, 1, "Homepage").apply {
                icon = ContextCompat.getDrawable(context, R.drawable.ic_homepage)?.apply {
                    setTint(ContextCompat.getColor(context, android.R.color.black))
                }
            }
            menu.add(0, R.id.navigation_lembretes, 2, "Lembretes").apply {
                icon = ContextCompat.getDrawable(context, R.drawable.ic_lembretes)?.apply {
                    setTint(ContextCompat.getColor(context, android.R.color.black))
                }
            }
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_horario -> {
                        val intent = Intent(this@CalendarioActivity, HorarioActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.navigation_homepage -> {
                        val intent = Intent(this@CalendarioActivity, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.navigation_lembretes -> {
                        val intent = Intent(this@CalendarioActivity, LembretesActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

        // Adicionar BottomNavigationView ao main layout
        mainLayout.addView(bottomNavigationView)

        // Set the main layout as the content view
        setContentView(mainLayout)
    }

    private fun updateReminders(date: String, listView: ListView) {
        val reminderList = reminders[date] ?: listOf("Não há lembretes para este dia.")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reminderList)
        listView.adapter = adapter
    }
}
