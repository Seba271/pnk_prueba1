package com.example.webservice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a los campos
        val txtusuario = findViewById<EditText>(R.id.txtusuariologin)
        val txtclave = findViewById<EditText>(R.id.txtcontralogin)
        val loginButton = findViewById<Button>(R.id.btn_iniciar)
        val registerButton = findViewById<Button>(R.id.button)

        // Botón de inicio de sesión
        loginButton.setOnClickListener {
            val usuarioIngresado = txtusuario.text.toString()
            val claveIngresada = txtclave.text.toString()

            if (usuarioIngresado == "admin" && claveIngresada == "admin") {
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón para ir a Registrar (opcional)
        registerButton.setOnClickListener {
            val intent = Intent(this, Registrar::class.java)
            startActivity(intent)
        }
    }
}
