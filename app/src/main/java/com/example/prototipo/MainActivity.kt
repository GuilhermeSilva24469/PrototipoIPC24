package com.example.prototipo

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Log.d("MainActivity", "onCreate: Iniciando criação do layout principal")

            // Create a RelativeLayout to hold everything
            val mainLayout = RelativeLayout(this).apply {
                layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }

            Log.d("MainActivity", "onCreate: RelativeLayout criado")

            // Create a LinearLayout for the main content
            val layout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                    bottomMargin = 150 // Adjust to leave space for BottomNavigationView
                }
                setPadding(32, 32, 32, 32)
                setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                gravity = Gravity.CENTER
            }
            mainLayout.addView(layout)

            Log.d("MainActivity", "onCreate: LinearLayout criado")

            val typefaceBold = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")
            val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

            Log.d("MainActivity", "onCreate: Tipografia carregada")

            val titleTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                text = "ReminderApp"
                textSize = 36f
                setPadding(0, 0, 0, 32)
                gravity = Gravity.CENTER_HORIZONTAL
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                typeface = typefaceBold
            }
            layout.addView(titleTextView)

            Log.d("MainActivity", "onCreate: TextView de título criado")

            val calendarioButton = Button(this).apply {
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                text = "Calendário"
                setPadding(32)
                typeface = typefaceRegular
                setOnClickListener {
                    val intent = Intent(this@MainActivity, HorarioActivity::class.java)
                    startActivity(intent)
                }
            }
            layout.addView(calendarioButton)

            Log.d("MainActivity", "onCreate: Botões criados")

            // Create BottomNavigationView programmatically
            val bottomNavigationView = BottomNavigationView(this).apply {
                id = View.generateViewId()
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                }
                setBackgroundResource(android.R.color.white)
                itemIconTintList = ContextCompat.getColorStateList(context, R.color.bottom_nav_item_color)
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
                        val intent = Intent(this@MainActivity, HorarioActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.navigation_homepage -> {
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.navigation_lembretes -> {
                        val intent = Intent(this@MainActivity, LembretesActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            Log.d("MainActivity", "onCreate: BottomNavigationView criado")

            // Add BottomNavigationView to main layout
            mainLayout.addView(bottomNavigationView)

            // Set the main layout as the content view
            setContentView(mainLayout)

            Log.d("MainActivity", "onCreate: Layout principal definido como conteúdo")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
        }
    }
}