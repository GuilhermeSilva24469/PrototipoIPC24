package com.example.prototipo

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HorarioActivity : Activity() {

    private val scheduleMap = mutableMapOf(
        "Segunda-feira" to mutableListOf("Algoritmos - 09:00 - 11:00"),
        "Terça-feira" to mutableListOf("Estruturas de Dados - 11:00 - 13:00"),
        "Quarta-feira" to mutableListOf("Sistemas Operativos - 14:00 - 16:00"),
        "Quinta-feira" to mutableListOf("Redes de Computadores - 09:00 - 11:00"),
        "Sexta-feira" to mutableListOf("Inteligência Artificial - 11:00 - 13:00")
    )

    private lateinit var contentLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = RelativeLayout(this).apply {
            layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }

        contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                addRule(RelativeLayout.ABOVE, View.generateViewId())
            }
            setPadding(32, 32, 32, 32)
        }

        val typefaceBold = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")
        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        val titleTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            text = "Horário Escolar - Engenharia Informática"
            textSize = 28f
            setPadding(0, 0, 0, 32)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            typeface = typefaceBold
        }
        contentLayout.addView(titleTextView)

        updateSchedule(typefaceBold, typefaceRegular)

        val bottomNavigationView = BottomNavigationView(this).apply {
            id = View.generateViewId()
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
            setBackgroundResource(android.R.color.white)
            itemIconTintList =
                ContextCompat.getColorStateList(context, R.color.bottom_nav_item_color)
            itemTextColor = ContextCompat.getColorStateList(context, R.color.bottom_nav_item_color)
            menu.add(0, R.id.navigation_horario, 0, "Horário").apply {
                icon = ContextCompat.getDrawable(context, R.drawable.ic_horario)
            }
            menu.add(0, R.id.navigation_homepage, 1, "Homepage").apply {
                icon = ContextCompat.getDrawable(context, R.drawable.ic_homepage)
            }
            menu.add(0, R.id.navigation_lembretes, 2, "Lembretes").apply {
                icon = ContextCompat.getDrawable(context, R.drawable.ic_lembretes)
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_horario -> {
                    // Já estamos na tela de horário, nada a fazer
                    true
                }
                R.id.navigation_homepage -> {
                    val intent = Intent(this@HorarioActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_lembretes -> {
                    val intent = Intent(this@HorarioActivity, LembretesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        val fab = FloatingActionButton(this).apply {
            id = View.generateViewId()
            setImageResource(android.R.drawable.ic_input_add)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                setMargins(16, 16, 16, 200) // Increased bottom margin to position higher
            }
        }
        fab.setOnClickListener {
            showAddEventDialog()
        }

        Log.d("HorarioActivity", "onCreate: BottomNavigationView criado")

        layout.addView(contentLayout)
        layout.addView(bottomNavigationView)
        layout.addView(fab)

        setContentView(layout)
    }

    private fun updateSchedule(typefaceBold: Typeface, typefaceRegular: Typeface) {
        contentLayout.removeAllViews()

        val titleTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            text = "Horário Escolar - Engenharia Informática"
            textSize = 28f
            setPadding(0, 0, 0, 32)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            typeface = typefaceBold
        }
        contentLayout.addView(titleTextView)

        scheduleMap.forEach { (dia, horarios) ->
            val horarioTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                text = dia
                textSize = 20f
                setPadding(16, 8, 16, 8)
                typeface = typefaceBold
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            }
            contentLayout.addView(horarioTextView)

            horarios.forEach { horario ->
                val horarioDetailTextView = TextView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    text = horario
                    textSize = 18f
                    setPadding(32, 8, 16, 8)
                    typeface = typefaceRegular
                    setOnClickListener {
                        showEditEventDialog(dia, horario)
                    }
                }
                contentLayout.addView(horarioDetailTextView)
            }
        }
    }

    private fun showAddEventDialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16)
        }

        val daySpinner = Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        val daysAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        daySpinner.adapter = daysAdapter

        val eventEditText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            hint = "Descrição do evento"
        }

        dialogLayout.addView(daySpinner)
        dialogLayout.addView(eventEditText)

        AlertDialog.Builder(this)
            .setTitle("Adicionar Evento")
            .setView(dialogLayout)
            .setPositiveButton("Adicionar") { _, _ ->
                val selectedDay = daySpinner.selectedItem.toString()
                val eventText = eventEditText.text.toString()
                if (eventText.isNotBlank()) {
                    scheduleMap[selectedDay]?.add(eventText)
                    updateSchedule(
                        Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf"),
                        Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")
                    )
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showEditEventDialog(day: String, event: String) {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16)
        }

        val eventEditText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setText(event)
        }

        dialogLayout.addView(eventEditText)

        AlertDialog.Builder(this)
            .setTitle("Editar Evento")
            .setView(dialogLayout)
            .setPositiveButton("Salvar") { _, _ ->
                val newEventText = eventEditText.text.toString()
                if (newEventText.isNotBlank()) {
                    scheduleMap[day]?.apply {
                        remove(event)
                        add(newEventText)
                    }
                    updateSchedule(
                        Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf"),
                        Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")
                    )
                }
            }
            .setNeutralButton("Excluir") { _, _ ->
                scheduleMap[day]?.remove(event)
                updateSchedule(
                    Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf"),
                    Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
