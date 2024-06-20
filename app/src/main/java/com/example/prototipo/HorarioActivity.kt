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

        val voltarTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Voltar"
            textSize = 18f
            setPadding(16)
            typeface = typefaceBold
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
            setOnClickListener {
                finish()
            }
        }
        contentLayout.addView(voltarTextView)

        val titleTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Horário Escolar - Engenharia Informática"
            textSize = 28f
            setPadding(0, 0, 0, 32)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            typeface = typefaceBold
        }
        contentLayout.addView(titleTextView)

        // Mock schedule details
        val scheduleDetails = listOf(
            "Segunda-feira: Algoritmos - 09:00 - 11:00",
            "Terça-feira: Estruturas de Dados - 11:00 - 13:00",
            "Quarta-feira: Sistemas Operativos - 14:00 - 16:00",
            "Quinta-feira: Redes de Computadores - 09:00 - 11:00",
            "Sexta-feira: Inteligência Artificial - 11:00 - 13:00"
        )

        scheduleDetails.forEach { detail ->
            val scheduleTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                text = detail
                textSize = 18f
                setPadding(16, 8, 16, 8)
                typeface = typefaceRegular
            }
            contentLayout.addView(scheduleTextView)
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
                    val intent = Intent(this@HorarioActivity, HorarioActivity::class.java)
                    startActivity(intent)
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
}
