package com.example.prototipo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : Activity() {

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Log.d("MainActivity", "onCreate: Iniciando criação do layout principal")

            // Solicitar permissão de notificação para Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestNotificationPermission()
            }

            val mainLayout = RelativeLayout(this).apply {
                layoutParams = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }

            Log.d("MainActivity", "onCreate: RelativeLayout criado")

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
                id = View.generateViewId() // Gerar um ID para o título
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    // Define as regras de layout para alinhar o TextView no topo do RelativeLayout
                    addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    addRule(RelativeLayout.CENTER_HORIZONTAL)
                    topMargin = 32
                }
                text = "ReminderApp"
                textSize = 40f
                setPadding(0, 0, 0, 32)
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                typeface = typefaceBold
            }
            mainLayout.addView(titleTextView)

            Log.d("MainActivity", "onCreate: TextView de título criado")

            val imageView = ImageView(this).apply {
                id = View.generateViewId()
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.BELOW, titleTextView.id)
                    addRule(RelativeLayout.CENTER_HORIZONTAL)
                    topMargin = 16
                }
                setImageResource(R.drawable.ic_icon_reminderapp)
            }
            mainLayout.addView(imageView)

            val calendarioButton = Button(this).apply {
                id = View.generateViewId()
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                text = "Calendário"
                textSize = 20f
                setPadding(40)
                typeface = typefaceBold
                setOnClickListener {
                    val intent = Intent(this@MainActivity, CalendarioActivity::class.java)
                    startActivity(intent)
                }
            }
            layout.addView(calendarioButton)

            Log.d("MainActivity", "onCreate: Botões criados")

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

            mainLayout.addView(bottomNavigationView)

            setContentView(mainLayout)

            Log.d("MainActivity", "onCreate: Layout principal definido como conteúdo")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida
                Log.d("MainActivity", "Permissão de notificação concedida")
            } else {
                // Permissão negada
                Log.d("MainActivity", "Permissão de notificação negada")
            }
        }
    }
}
