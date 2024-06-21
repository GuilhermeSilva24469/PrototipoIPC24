package com.example.prototipo

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HorarioActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = RelativeLayout(this).apply {
            layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }

        val contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                addRule(RelativeLayout.ABOVE, View.generateViewId())
            }
            setPadding(32, 32, 32, 32)
        }

        val typefaceBold = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")
        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        val titleTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Horário Escolar - Engenharia Informática"
            textSize = 28f
            setPadding(0, 0, 0, 32)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            typeface = typefaceBold
        }
        contentLayout.addView(titleTextView)

        // Array com os dias da semana
        val diasSemana = arrayOf("Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira")

        // Criar TextViews para cada dia da semana com seu respectivo horário
        diasSemana.forEach { dia ->
            val horarioTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                text = dia
                textSize = 20f
                setPadding(16, 8, 16, 8)
                typeface = typefaceBold
                setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            }
            contentLayout.addView(horarioTextView)

            // Horários das aulas
            val horarios = getHorariosPorDia(dia) // Função fictícia para obter os horários por dia
            horarios.forEach { horario ->
                val horarioDetailTextView = TextView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                    text = horario
                    textSize = 18f
                    setPadding(32, 8, 16, 8)
                    typeface = typefaceRegular
                }
                contentLayout.addView(horarioDetailTextView)
            }
        }

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

        Log.d("HorarioActivity", "onCreate: BottomNavigationView criado")

        layout.addView(contentLayout)
        layout.addView(bottomNavigationView)

        setContentView(layout)
    }

    // Função fictícia para obter os horários por dia da semana
    private fun getHorariosPorDia(dia: String): List<String> {
        return when (dia) {
            "Segunda-feira" -> listOf("Algoritmos - 09:00 - 11:00")
            "Terça-feira" -> listOf("Estruturas de Dados - 11:00 - 13:00")
            "Quarta-feira" -> listOf("Sistemas Operativos - 14:00 - 16:00")
            "Quinta-feira" -> listOf("Redes de Computadores - 09:00 - 11:00")
            "Sexta-feira" -> listOf("Inteligência Artificial - 11:00 - 13:00")
            else -> emptyList()
        }
    }
}
