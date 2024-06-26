package com.example.prototipo

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import java.text.SimpleDateFormat
import java.util.*

class EditarLembreteActivity : Activity() {

    private lateinit var categoriaSpinner: Spinner
    private lateinit var lembreteEditText: EditText
    private lateinit var dateTimeEditText: EditText
    private lateinit var volumeSpinner: Spinner
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

        oldCategoria = intent.getStringExtra("oldCategoria")
        oldLembrete = intent.getStringExtra("oldLembrete")

        val lembreteParts = oldLembrete?.split(" - ") ?: emptyList()
        if (lembreteParts.size == 3) {
            val categoriaAdapter = categoriaSpinner.adapter as ArrayAdapter<String>
            val volumeAdapter = volumeSpinner.adapter as ArrayAdapter<String>

            categoriaSpinner.setSelection(categoriaAdapter.getPosition(oldCategoria))
            lembreteEditText.setText(lembreteParts[0])
            dateTimeEditText.setText(lembreteParts[1])
            volumeSpinner.setSelection(volumeAdapter.getPosition(lembreteParts[2]))
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
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                marginEnd = 16
            }
            text = "Editar Lembrete"
            textSize = 28f
            setPadding(0, 0, 0, 32)
            gravity = Gravity.CENTER_HORIZONTAL
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            typeface = typefaceBold
        }
        topBar.addView(textView)

        mainLayout.addView(topBar)
    }

    private fun configureForm(mainLayout: LinearLayout) {
        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        lembreteEditText = EditText(this).apply {
            hint = "Título"
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 16)
            }
            typeface = typefaceRegular
        }
        mainLayout.addView(lembreteEditText)

        dateTimeEditText = EditText(this).apply {
            hint = "Dia e hora"
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 16)
            }
            typeface = typefaceRegular
            isFocusable = false
            isClickable = true
            setOnClickListener {
                showDateTimePickerDialog()
            }
        }
        mainLayout.addView(dateTimeEditText)

        val volumeTextView = TextView(this).apply {
            text = "Volume"
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 8)
            }
            typeface = typefaceRegular
        }
        mainLayout.addView(volumeTextView)

        volumeSpinner = Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 16)
            }
            adapter = ArrayAdapter(
                this@EditarLembreteActivity,
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Baixo", "Médio", "Alto")
            )
        }
        mainLayout.addView(volumeSpinner)

        val categoriaTextView = TextView(this).apply {
            text = "Categoria"
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 8)
            }
            typeface = typefaceRegular
        }
        mainLayout.addView(categoriaTextView)

        categoriaSpinner = Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 16)
            }
            adapter = ArrayAdapter(
                this@EditarLembreteActivity,
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Tarefas da semana", "Tarefas do mês", "Trabalho", "Pessoal", "Estudo", "Outro")
            )
        }
        mainLayout.addView(categoriaSpinner)

        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(0, 32, 0, 0)
            }
        }

        val saveButton = Button(this).apply {
            text = "GUARDAR"
            typeface = typefaceRegular
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f).apply {
                setMargins(0, 0, 8, 0)
            }
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            setOnClickListener {
                val resultIntent = Intent().apply {
                    putExtra("categoria", categoriaSpinner.selectedItem.toString())
                    putExtra("lembrete", "${lembreteEditText.text} - ${dateTimeEditText.text} - ${volumeSpinner.selectedItem.toString()}")
                    putExtra("oldCategoria", oldCategoria)
                    putExtra("oldLembrete", oldLembrete)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
        buttonLayout.addView(saveButton)

        val cancelButton = Button(this).apply {
            text = "CANCELAR"
            typeface = typefaceRegular
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f).apply {
                setMargins(8, 0, 0, 0)
            }
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            setOnClickListener {
                finish()
            }
        }
        buttonLayout.addView(cancelButton)

        mainLayout.addView(buttonLayout)
    }

    private fun showDateTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                showTimePickerDialog(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(calendar: Calendar) {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                dateTimeEditText.setText(format.format(calendar.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }
}