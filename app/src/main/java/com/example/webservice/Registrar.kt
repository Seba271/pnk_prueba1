package com.example.webservice

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog

class Registrar : AppCompatActivity() {

    private lateinit var nombre: EditText
    private lateinit var apellido: EditText
    private lateinit var email: EditText
    private lateinit var clave: EditText
    private lateinit var repetirClave: EditText
    private lateinit var btn_reg: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        nombre = findViewById(R.id.txtnombre)
        apellido = findViewById(R.id.txtapellido)
        email = findViewById(R.id.txtemail)
        clave = findViewById(R.id.txtclave)
        repetirClave = findViewById(R.id.txtrepetirclave)
        btn_reg = findViewById(R.id.btn_registro)

        btn_reg.setOnClickListener {
            val nombreText = nombre.text.toString().trim()
            val apellidoText = apellido.text.toString().trim()
            val emailText = email.text.toString().trim()
            val claveText = clave.text.toString().trim()
            val repetirClaveText = repetirClave.text.toString().trim()

            when {
                nombreText.isBlank() || apellidoText.isBlank() || emailText.isBlank() ||
                        claveText.isBlank() || repetirClaveText.isBlank() -> {
                    mostrarAlertaCamposVacios()
                }
                !esEmailValido(emailText) -> {
                    mostrarAlertaEmailInvalido()
                }
                claveText != repetirClaveText -> {
                    mostrarAlertaClavesNoCoinciden()
                }
                !esClaveRobusta(claveText) -> {
                    mostrarAlertaClaveRobusta()
                }
                else -> {
                    guardar(nombreText, apellidoText, emailText, claveText)
                    mostrarAlertaRegistroExitoso()
                }
            }
        }
    }

    private fun esEmailValido(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // ✅ Validación de contraseña robusta
    private fun esClaveRobusta(clave: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        return regex.matches(clave)
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
        } catch (e: Exception) {
            mostrarAlerta("Error", "No se pudo guardar el usuario: ${e.message}")
        } finally {
            db.close()
        }
    }

    private fun mostrarAlertaCamposVacios() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Campos incompletos")
            .setContentText("Por favor, complete todos los campos antes de continuar.")
            .setConfirmText("Aceptar")
            .setConfirmClickListener { dialog -> dialog.dismissWithAnimation() }
            .show()
    }

    private fun mostrarAlertaClavesNoCoinciden() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText("Las contraseñas no coinciden.")
            .setConfirmText("Intentar de nuevo")
            .setConfirmClickListener { dialog -> dialog.dismissWithAnimation() }
            .show()
    }

    private fun mostrarAlertaEmailInvalido() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Correo inválido")
            .setContentText("Ingrese un correo electrónico válido.")
            .setConfirmText("Entendido")
            .setConfirmClickListener { dialog -> dialog.dismissWithAnimation() }
            .show()
    }

    // ✅ Alerta cuando la contraseña no sea robusta
    private fun mostrarAlertaClaveRobusta() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Contraseña débil")
            .setContentText("La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y un carácter especial.")
            .setConfirmText("Entendido")
            .setConfirmClickListener { dialog -> dialog.dismissWithAnimation() }
            .show()
    }

    private fun mostrarAlertaRegistroExitoso() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Registro exitoso")
            .setContentText("El usuario se ha registrado correctamente.")
            .setConfirmText("Aceptar")
            .setConfirmClickListener { dialog ->
                dialog.dismissWithAnimation()
                val intent = Intent(this, MostrarUsuarios::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(titulo)
            .setContentText(mensaje)
            .setConfirmText("Aceptar")
            .setConfirmClickListener { dialog -> dialog.dismissWithAnimation() }
            .show()
    }
}
