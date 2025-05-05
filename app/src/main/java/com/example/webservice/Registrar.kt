package com.example.webservice

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Registrar : AppCompatActivity() {

    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var email: EditText
    private lateinit var clave: EditText
    private lateinit var btn_reg: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        nombre = findViewById(R.id.txtnombre)
        apellido = findViewById(R.id.txtapellido)
        email = findViewById(R.id.txtemail)
        clave = findViewById(R.id.txtclave)
        btn_reg = findViewById(R.id.btn_registro)

        btn_reg.setOnClickListener {
            val nombreText = nombre.text.toString().trim()
            val apellidoText = apellido.text.toString().trim()
            val emailText = email.text.toString().trim()
            val claveText = clave.text.toString().trim()

            if (nombreText.isBlank() || apellidoText.isBlank() || emailText.isBlank() || claveText.isBlank()) {
                mostrarAlertaCamposVacios()
            } else {
                guardar(nombreText, apellidoText, emailText, claveText)
                val intent = Intent(this, MostrarUsuarios::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun guardar(nom: String, ape: String, mai: String, cla: String) {
        val helper = ConexionDbHelper(this)
        val db = helper.writableDatabase
        try {
            val datos = ContentValues().apply {
                put("Nombre", nom)
                put("Apellido", ape)
                put("Email", mai)
                put("Clave", cla)
            }
            db.insert("USUARIOS", null, datos)
            Toast.makeText(this, "Datos ingresados sin problemas", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            db.close()
        }
    }

    private fun mostrarAlertaCamposVacios() {
        // Si ya integraste SweetAlertDialog
        // SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        //     .setTitleText("Campos incompletos")
        //     .setContentText("Por favor, complete todos los campos antes de continuar.")
        //     .setConfirmText("Aceptar")
        //     .setConfirmClickListener { dialog -> dialog.dismissWithAnimation() }
        //     .show()

        // Temporal con Toast si aún no está agregado SweetAlert
        Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
    }
}
