package com.example.prototipo

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditarLembreteActivity : Activity() {

    private lateinit var categoriaEditText: EditText
    private lateinit var lembreteEditText: EditText
    private lateinit var dateTimeEditText: EditText
    private lateinit var volumeEditText: EditText
    private var oldCategoria: String? = null
    private var oldLembrete: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setPadding(32, 32, 32, 32)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }
        setContentView(mainLayout)

        configureTopBar(mainLayout)
        configureForm(mainLayout)

        val bottomNavContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }

        mainLayout.addView(bottomNavContainer)

        configureBottomNavigationView(bottomNavContainer)

        oldCategoria = intent.getStringExtra("oldCategoria")
        oldLembrete = intent.getStringExtra("oldLembrete")

        val lembreteParts = oldLembrete?.split(" - ") ?: emptyList()
        if (lembreteParts.size == 3) {
            categoriaEditText.setText(oldCategoria)
            lembreteEditText.setText(lembreteParts[0])
            dateTimeEditText.setText(lembreteParts[1])
            volumeEditText.setText(lembreteParts[2])
        }
    }

    private fun configureTopBar(mainLayout: LinearLayout) {
        val topBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            gravity = Gravity.CENTER_VERTICAL
        }

        val typefaceBold = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")

        val textView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text = "Editar Lembrete"
            textSize = 28f
            setPadding(16)
            typeface = typefaceBold
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            gravity = Gravity.CENTER
        }
        topBar.addView(textView)

        mainLayout.addView(topBar)
    }

    private fun configureForm(mainLayout: LinearLayout) {
        categoriaEditText = EditText(this).apply {
            hint = "Categoria"
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 16)
            }
        }
        mainLayout.addView(categoriaEditText)

        lembreteEditText = EditText(this).apply {
            hint = "Lembrete"
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 16)
            }
        }
        mainLayout.addView(lembreteEditText)

        dateTimeEditText = EditText(this).apply {
            hint = "Data e Hora (dd/MM/yyyy HH:mm)"
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 16)
            }
        }
        mainLayout.addView(dateTimeEditText)

        volumeEditText = EditText(this).apply {
            hint = "Volume"
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 16)
            }
        }
        mainLayout.addView(volumeEditText)

        val buttonLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                topMargin = 32
            }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            weightSum = 2f
        }
        layout.addView(buttonLayout)

        val saveButton = Button(this).apply {
            text = "Guardar"
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f).apply {
                marginEnd = 16
            }
            setOnClickListener {
                val resultIntent = Intent().apply {
                    putExtra("categoria", categoriaEditText.text.toString())
                    putExtra("lembrete", "${lembreteEditText.text} - ${dateTimeEditText.text} - ${volumeEditText.text}")
                    putExtra("oldCategoria", oldCategoria)
                    putExtra("oldLembrete", oldLembrete)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
        mainLayout.addView(saveButton)

        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        val cancelButton = Button(this).apply {
            text= "Cancelar"
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            typeface= typefaceRegular
            setPadding(16)
            setOnClickListener {
                finish()
            }
        }
        buttonLayout.addView(cancelButton)
    }

    private fun configureBottomNavigationView(mainLayout: LinearLayout) {
        val bottomNavigationView = BottomNavigationView(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
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
                    val intent = Intent(this@EditarLembreteActivity, HorarioActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_homepage -> {
                    val intent = Intent(this@EditarLembreteActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_lembretes -> {
                    true
                }
                else -> false
            }
        }

        mainLayout.addView(bottomNavigationView)
    }
}
