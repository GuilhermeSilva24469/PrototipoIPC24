package com.example.prototipo

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

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

        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setPadding(32, 32, 32, 32)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }
        setContentView(mainLayout)

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

        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        val titleEditText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            hint = "Título"
            inputType = InputType.TYPE_CLASS_TEXT
            setPadding(16)
            typeface = typefaceRegular
            setBackgroundResource(android.R.drawable.edit_text)
        }
        mainLayout.addView(titleEditText)

        val dateTimeEditText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            hint = "Dia e hora"
            inputType = InputType.TYPE_NULL
            setPadding(16)
            typeface = typefaceRegular
            setBackgroundResource(android.R.drawable.edit_text)
            setOnClickListener {
                showDateTimePickerDialog(this)
            }
        }
        mainLayout.addView(dateTimeEditText)

        val volumeTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Volume"
            setPadding(0, 16, 0, 8)
            typeface = typefaceRegular
        }
        mainLayout.addView(volumeTextView)

        val volumeSpinner = Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            adapter = ArrayAdapter.createFromResource(
                context,
                R.array.volume_array,
                android.R.layout.simple_spinner_item
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
        mainLayout.addView(volumeSpinner)

        val categoryTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Categoria"
            setPadding(0, 16, 0, 8)
            typeface = typefaceRegular
        }
        mainLayout.addView(categoryTextView)

        val categorySpinner = Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            adapter = ArrayAdapter.createFromResource(
                context,
                R.array.categoria_array,
                android.R.layout.simple_spinner_item
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
        mainLayout.addView(categorySpinner)

        val buttonLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                topMargin = 32
            }
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            weightSum = 2f
        }
        mainLayout.addView(buttonLayout)

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

    private fun showDateTimePickerDialog(editText: EditText) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedDateTime = String.format("%02d/%02d/%04d %02d:%02d",
                    selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute)
                editText.setText(selectedDateTime)
            }, hour, minute, true)
            timePickerDialog.show()
        }, year, month, day)
        datePickerDialog.show()
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
