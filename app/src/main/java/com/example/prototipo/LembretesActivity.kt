package com.example.prototipo

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding

class LembretesActivity : Activity() {

    private val lembretesMap = mutableMapOf<String, MutableList<String>>()
    private lateinit var lembretesLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

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
        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        val voltarTextView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                marginEnd = 16
            }
            text = "Voltar"
            textSize = 18f
            setPadding(16)
            typeface = typefaceBold
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
            setOnClickListener {
                finish()
            }
        }
        topBar.addView(voltarTextView)

        val textView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
            text = "Os meus lembretes"
            textSize = 22f
            setPadding(16)
            typeface = typefaceBold
            gravity = Gravity.CENTER
        }
        topBar.addView(textView)

        val addButton = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            text = "Adicionar"
            setPadding(16)
            typeface = typefaceRegular
            setOnClickListener {
                val intent = Intent(this@LembretesActivity, NovoLembreteActivity::class.java)
                startActivityForResult(intent, 1)
            }
        }
        topBar.addView(addButton)

        mainLayout.addView(topBar)

        val scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, 0).apply {
                weight = 1f
            }
        }

        lembretesLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }

        scrollView.addView(lembretesLayout)
        mainLayout.addView(scrollView)

        val lembretes = loadLembretes()
        lembretesMap.putAll(lembretes)

        refreshLembretes()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Channel"
            val descriptionText = "Channel for Reminder Notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("reminder_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showEditDeleteDialog(categoria: String, lembrete: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Opções de Lembrete")
        dialogBuilder.setMessage("Escolha uma opção:")
        dialogBuilder.setPositiveButton("Editar") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, NovoLembreteActivity::class.java).apply {
                putExtra("categoria", categoria)
                putExtra("lembrete", lembrete)
                putExtra("isEditing", true)
            }
            startActivityForResult(intent, 2)
        }
        dialogBuilder.setNegativeButton("Eliminar") { dialog, _ ->
            dialog.dismiss()
            lembretesMap[categoria]?.remove(lembrete)
            refreshLembretes()
        }
        dialogBuilder.setNeutralButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun refreshLembretes() {
        lembretesLayout.removeAllViews()
        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

        lembretesMap.forEach { (categoria, lembretes) ->
            val categoriaTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                text = categoria
                textSize = 18f
                setPadding(16, 16, 16, 8)
                typeface = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")
            }
            lembretesLayout.addView(categoriaTextView)

            lembretes.forEach { lembrete ->
                val lembreteLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    setPadding(16, 8, 16, 8)
                }

                val checkBox = CheckBox(this).apply {
                    layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                }
                lembreteLayout.addView(checkBox)

                val lembreteTextView = TextView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                        marginStart = 16
                    }
                    text = lembrete.replace(" - ", "\n")
                    textSize = 16f
                    typeface = typefaceRegular
                    setOnClickListener { v ->
                        showEditDeleteDialog(categoria, lembrete)
                    }
                }
                lembreteLayout.addView(lembreteTextView)

                lembretesLayout.addView(lembreteLayout)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1 || requestCode == 2) {
                val categoria = data?.getStringExtra("categoria") ?: return
                val lembrete = data.getStringExtra("lembrete") ?: return
                lembretesMap[categoria]?.add(lembrete)
                refreshLembretes()
            }
        }
    }

    private fun loadLembretes(): Map<String, MutableList<String>> {
        val sharedPreferences = getSharedPreferences("LembretesApp", MODE_PRIVATE)
        val lembretes = sharedPreferences.getStringSet("lembretes", setOf()) ?: setOf()

        val lembretesMap = mutableMapOf<String, MutableList<String>>()

        lembretes.forEach { lembrete ->
            val parts = lembrete.split("|")
            val titulo = parts[0]
            val dataHora = parts[1]
            val volume = parts[2]
            val categoria = parts[3]

            if (!lembretesMap.containsKey(categoria)) {
                lembretesMap[categoria] = mutableListOf()
            }

            lembretesMap[categoria]?.add("$titulo - $dataHora - $volume")
        }

        return lembretesMap
    }
}
