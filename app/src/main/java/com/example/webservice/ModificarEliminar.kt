package com.example.webservice

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

lateinit var nom: EditText
lateinit var ape: EditText
lateinit var clav: EditText
lateinit var repclav: EditText

lateinit var mod: Button
lateinit var elim: Button

class ModificarEliminar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modificar_eliminar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        nom = findViewById(R.id.txtnombremod)
        ape = findViewById(R.id.txtapellidomod)
        clav = findViewById(R.id.txtrepclavemod)
        repclav = findViewById(R.id.txtrepclavemod)
        mod = findViewById(R.id.btn_modificar)
        elim = findViewById(R.id.btn_eliminar)

        val idusu = intent.getIntExtra("Id", 0)
        val nombre = intent.getStringExtra("Nombre") ?: ""
        val apellido = intent.getStringExtra("Apellido") ?: ""
        val clave = intent.getStringExtra("clave") ?: ""
        val repclave = intent.getStringExtra("clave") ?: ""

        nom.setText(nombre)
        ape.setText(apellido)
        clav.setText(clave)
        repclav.setText(repclave)


    }
}