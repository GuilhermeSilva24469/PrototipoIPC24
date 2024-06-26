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
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

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

        configureTopBar(mainLayout)
        configureScrollView(mainLayout)
        configureBottomNavigationView(mainLayout)

        val lembretes = loadLembretes()
        lembretesMap.putAll(lembretes)
        removeExpiredReminders()
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

    private fun showSuccessMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val categoria = data?.getStringExtra("categoria") ?: return
            val lembrete = data.getStringExtra("lembrete") ?: return
            val oldLembrete = data.getStringExtra("oldLembrete")
            val oldCategoria = data.getStringExtra("oldCategoria")

            when (requestCode) {
                1 -> { // Adding new reminder
                    if (!lembretesMap.containsKey(categoria)) {
                        lembretesMap[categoria] = mutableListOf()
                    }
                    lembretesMap[categoria]?.add(lembrete)
                    showSuccessMessage("Lembrete adicionado com sucesso")
                }
                2 -> { // Editing existing reminder
                    if (oldCategoria != null && oldLembrete != null) {
                        // Remove the old reminder if it exists
                        lembretesMap[oldCategoria]?.remove(oldLembrete)
                        if (lembretesMap[oldCategoria]?.isEmpty() == true) {
                            lembretesMap.remove(oldCategoria)
                        }
                    }

                    if (!lembretesMap.containsKey(categoria)) {
                        lembretesMap[categoria] = mutableListOf()
                    }
                    lembretesMap[categoria]?.add(lembrete)
                }
            }

            saveLembretes()
            refreshLembretes()
        }
    }

    private fun showEditDeleteDialog(categoria: String, lembrete: String) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setTitle("Opções de Lembrete")
        dialogBuilder.setMessage("Escolha uma opção:")
        dialogBuilder.setPositiveButton("Editar") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, EditarLembreteActivity::class.java).apply {
                putExtra("categoria", categoria)
                putExtra("lembrete", lembrete)
                putExtra("oldCategoria", categoria)
                putExtra("oldLembrete", lembrete)
            }
            startActivityForResult(intent, 2)
        }
        dialogBuilder.setNegativeButton("Eliminar") { dialog, _ ->
            dialog.dismiss()
            lembretesMap[categoria]?.remove(lembrete)
            if (lembretesMap[categoria]?.isEmpty() == true) {
                lembretesMap.remove(categoria)
            }
            saveLembretes()
            removeExpiredReminders()
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
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        lembretesMap.forEach { (categoria, lembretes) ->
            val categoriaTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                text = categoria
                textSize = 18f
                setPadding(16, 16, 16, 8)
                typeface = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")
            }
            lembretesLayout.addView(categoriaTextView)

            val iterator = lembretes.iterator()
            while (iterator.hasNext()) {
                val lembrete = iterator.next()
                val parts = lembrete.split(" - ")
                if (parts.size >= 2) {
                    val dataHoraString = parts[1]
                    try {
                        val dataHora = dateFormat.parse(dataHoraString)
                        if (dataHora != null && dataHora.time < System.currentTimeMillis()) {
                            iterator.remove()
                            continue
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

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

    private fun removeExpiredReminders() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val iterator = lembretesMap.values.iterator()
        while (iterator.hasNext()) {
            val lembretes = iterator.next()
            val reminderIterator = lembretes.iterator()
            while (reminderIterator.hasNext()) {
                val lembrete = reminderIterator.next()
                val parts = lembrete.split(" - ")
                if (parts.size >= 2) {
                    val dataHoraString = parts[1]
                    try {
                        val dataHora = dateFormat.parse(dataHoraString)
                        if (dataHora != null && dataHora.time < System.currentTimeMillis()) {
                            reminderIterator.remove()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (lembretes.isEmpty()) {
                iterator.remove()
            }
        }
        saveLembretes()
    }

    private fun saveLembretes() {
        val sharedPreferences = getSharedPreferences("LembretesApp", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val lembretesSet = lembretesMap.flatMap { entry ->
            entry.value.map { lembrete ->
                val parts = lembrete.split(" - ")
                val titulo = parts[0]
                val dataHora = parts[1]
                val volume = parts[2]
                val categoria = entry.key
                "$titulo|$dataHora|$volume|$categoria"
            }
        }.toSet()

        editor.putStringSet("lembretes", lembretesSet)
        editor.apply()

        Toast.makeText(this, "Alterações guardadas com sucesso", Toast.LENGTH_SHORT).show()
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

    private fun configureTopBar(mainLayout: LinearLayout) {
        val topBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            gravity = Gravity.CENTER_VERTICAL
        }

        val typefaceBold = Typeface.createFromAsset(assets, "fonts/SF-Pro-Text-Bold.otf")

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
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            typeface = typefaceBold
            gravity = Gravity.CENTER
        }
        topBar.addView(textView)

        val typefaceRegular = Typeface.createFromAsset(assets, "fonts/SF-Pro-Display-Medium.otf")

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
    }

    private fun configureScrollView(mainLayout: LinearLayout) {
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
                    val intent = Intent(this@LembretesActivity, HorarioActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_homepage -> {
                    val intent = Intent(this@LembretesActivity, MainActivity::class.java)
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