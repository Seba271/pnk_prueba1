package com.example.webservice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón para registrar un usuario
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }

        // Botón para mostrar los usuarios
        val btnMostrarUsuarios = findViewById<Button>(R.id.btnMostrarUsuarios) // Asegúrate de tener este botón en tu XML
        btnMostrarUsuarios.setOnClickListener {
            val intent = Intent(this, MostrarUsuarios::class.java) // Redirige a la actividad MostrarUsuarios
            startActivity(intent)
        }

        // Botón para la pantalla "AcercaDe"
        val loginButton = findViewById<Button>(R.id.buttonabout)
        loginButton.setOnClickListener {
            val intent = Intent(this, AcercaDe::class.java)
            startActivity(intent)
        }

        // Botón para capturar datos
        val capButton = findViewById<Button>(R.id.buttoncapture)
        capButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
