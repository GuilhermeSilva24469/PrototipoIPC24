package com.example.prototipo

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import java.text.SimpleDateFormat
import java.util.*

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

        if (isEditing) {
            val lembrete = intent.getStringExtra("lembrete")
            lembrete?.split("|")?.let {
                titleEditText.setText(it[0])
                dateTimeEditText.setText(it[1])
                volumeSpinner.setSelection(resources.getStringArray(R.array.volume_array).indexOf(it[2]))
                categorySpinner.setSelection(resources.getStringArray(R.array.categoria_array).indexOf(it[3]))
            }
        }
    }

    private fun showDateTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val timePickerDialog = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        editText.setText(sdf.format(calendar.time))
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun guardarLembrete(titulo: String, dataHora: String, volume: String, categoria: String, isEditing: Boolean) {
        val sharedPreferences = getSharedPreferences("LembretesApp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val lembretes = sharedPreferences.getStringSet("lembretes", mutableSetOf()) ?: mutableSetOf()

        if (isEditing) {
            val oldLembrete = intent.getStringExtra("lembrete")
            if (oldLembrete != null) {
                lembretes.remove(oldLembrete)
            }
        }

        val novoLembrete = "$titulo|$dataHora|$volume|$categoria"
        lembretes.add(novoLembrete)

        editor.putStringSet("lembretes", lembretes)
        editor.apply()

        scheduleNotification(titulo, "Lembrete: $titulo", dataHora, volume, categoria)

        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra("categoria", categoria)
            putExtra("lembrete", "$titulo - $dataHora - $volume")
        })
        finish()
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun scheduleNotification(title: String, message: String, dateTime: String, volume: String, categoria: String) {
        val notificationIntent = Intent(this, ReminderReceiver::class.java).apply {
            action = "com.example.prototipo.ACTION_NOTIFY"
            putExtra("title", title)
            putExtra("message", message)
            putExtra("volume", volume)
            putExtra("categoria", categoria)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = dateTimeFormat.parse(dateTime)
        date?.let {
            val triggerTime = it.time

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        }
    }
}
