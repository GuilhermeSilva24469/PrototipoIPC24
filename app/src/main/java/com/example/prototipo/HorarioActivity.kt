package com.example.prototipo

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

class HorarioActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setPadding(32, 32, 32, 32)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }
        setContentView(layout)

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
        layout.addView(voltarTextView)

        val titleTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Horário Escolar - Engenharia Informática"
            textSize = 28f
            setPadding(0, 0, 0, 32)
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            typeface = typefaceBold
        }
        layout.addView(titleTextView)

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
            layout.addView(scheduleTextView)
        }
    }
}
