package com.example.prototipo

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import java.util.Calendar
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Locale

class NovoLembreteActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setPadding(32, 32, 32, 32)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }
        setContentView(layout)

        val isEditing = intent.getBooleanExtra("isEditing", false)

        val typefaceBold = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")
        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        val titleTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = if (isEditing) "Editar Lembrete" else "Novo Lembrete"
            textSize = 28f
            setPadding(0, 0, 0, 32)
            gravity = Gravity.CENTER_HORIZONTAL
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            typeface = typefaceBold
        }
        layout.addView(titleTextView)

        val titleEditText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            hint = "Título"
            inputType = InputType.TYPE_CLASS_TEXT
            setPadding(16)
            typeface = typefaceRegular
            setBackgroundResource(android.R.drawable.edit_text)
        }
        layout.addView(titleEditText)

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
        layout.addView(dateTimeEditText)

        val volumeTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Volume"
            setPadding(0, 16, 0, 8)
            typeface = typefaceRegular
        }
        layout.addView(volumeTextView)

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
        layout.addView(volumeSpinner)

        val categoryTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Categoria"
            setPadding(0, 16, 0, 8)
            typeface = typefaceRegular
        }
        layout.addView(categoryTextView)

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
        layout.addView(categorySpinner)

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
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f).apply {
                marginEnd = 16
            }
            text = "Guardar"
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            typeface = typefaceRegular
            setPadding(16)
            setOnClickListener {
                val titulo = titleEditText.text.toString()
                val dataHora = dateTimeEditText.text.toString()
                val volumeSelecionado = volumeSpinner.selectedItem.toString()
                val categoriaSelecionada = categorySpinner.selectedItem.toString()
                guardarLembrete(titulo, dataHora, volumeSelecionado, categoriaSelecionada, isEditing)
            }
        }
        buttonLayout.addView(saveButton)

        val cancelButton = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text = "Cancelar"
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            typeface = typefaceRegular
            setPadding(16)
            setOnClickListener {
                finish()
            }
        }
        buttonLayout.addView(cancelButton)

        val categoria = intent.getStringExtra("categoria")
        val lembrete = intent.getStringExtra("lembrete")

        // Use the existing information to pre-fill the fields if editing
        if (isEditing && categoria != null && lembrete != null) {
            // Logic to pre-fill the fields with the existing reminder's information
            // Assuming lembrete is formatted as "title - dateTime - volume"
            val lembreteParts = lembrete.split(" - ")
            if (lembreteParts.size == 3) {
                titleEditText.setText(lembreteParts[0])
                dateTimeEditText.setText(lembreteParts[1])
                val volumeAdapter = volumeSpinner.adapter as ArrayAdapter<String>
                val volumePosition = volumeAdapter.getPosition(lembreteParts[2])
                volumeSpinner.setSelection(volumePosition)
                val categoriaAdapter = categorySpinner.adapter as ArrayAdapter<String>
                val categoriaPosition = categoriaAdapter.getPosition(categoria)
                categorySpinner.setSelection(categoriaPosition)
            }
        }
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

    private fun guardarLembrete(titulo: String, dataHora: String, volume: String, categoria: String, isEditing: Boolean) {
        val sharedPreferences = getSharedPreferences("LembretesApp", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val lembretes = sharedPreferences.getStringSet("lembretes", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        if (isEditing) {
            val oldLembrete = intent.getStringExtra("lembrete")
            if (oldLembrete != null) {
                lembretes.remove(oldLembrete)
            }
        }
        lembretes.add("$titulo|$dataHora|$volume|$categoria")

        editor.putStringSet("lembretes", lembretes)
        editor.apply()

        scheduleNotification(titulo, "Lembrete: $titulo", dataHora)

        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("categoria", categoria)
            putExtra("lembrete", "$titulo - $dataHora - $volume")
        })
        finish()
    }

    private fun scheduleNotification(title: String, message: String, dateTime: String) {
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            action = "com.example.prototipo.ACTION_NOTIFY"
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.parse(dateTime)

        if (date != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.time, pendingIntent)
        }
    }
}