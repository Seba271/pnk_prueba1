package com.example.webservice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.pedant.SweetAlert.SweetAlertDialog

lateinit var nom: EditText
lateinit var ape: EditText
lateinit var email: EditText
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

        // Inicialización de los campos
        nom = findViewById(R.id.txtnombremod)
        ape = findViewById(R.id.txtapellidomod)
        email = findViewById(R.id.txtemailmod)
        clav = findViewById(R.id.txtclavemod)
        repclav = findViewById(R.id.txtrepclavemod)
        mod = findViewById(R.id.btn_modificar)
        elim = findViewById(R.id.btn_eliminar)

        // Obtener datos del Intent
        val idusu = intent.getIntExtra("Id", 0)
        val nombre = intent.getStringExtra("Nombre") ?: ""
        val apellido = intent.getStringExtra("Apellido") ?: ""
        val correo = intent.getStringExtra("Email") ?: ""
        val clave = intent.getStringExtra("clave") ?: ""

        // Pre-llenar los campos con los datos actuales
        nom.setText(nombre)
        ape.setText(apellido)
        email.setText(correo)
        clav.setText(clave)
        repclav.setText(clave)

        // Modificar los datos
        mod.setOnClickListener {
            val nombreTxt = nom.text.toString().trim()
            val apellidoTxt = ape.text.toString().trim()
            val emailTxt = email.text.toString().trim()
            val claveTxt = clav.text.toString().trim()
            val repClaveTxt = repclav.text.toString().trim()

            when {
                nombreTxt.isBlank() -> {
                    mostrarAlerta("Error", "El nombre no puede estar vacío.")
                }
                apellidoTxt.isBlank() -> {
                    mostrarAlerta("Error", "El apellido no puede estar vacío.")
                }
                emailTxt.isBlank() -> {
                    mostrarAlerta("Error", "El correo electrónico no puede estar vacío.")
                }
                !esEmailValido(emailTxt) -> {
                    mostrarAlerta("Error", "El correo electrónico no es válido.")
                }
                claveTxt.isBlank() -> {
                    mostrarAlerta("Error", "La contraseña no puede estar vacía.")
                }
                repClaveTxt.isBlank() -> {
                    mostrarAlerta("Error", "Debe repetir la contraseña.")
                }
                claveTxt != repClaveTxt -> {
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Las contraseñas no coinciden.")
                        .show()
                }
                !esClaveRobusta(claveTxt) -> {
                    mostrarAlerta("Error", "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales.")
                }
                else -> {
                    modificar(idusu, nombreTxt, apellidoTxt, emailTxt, claveTxt)
                    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Modificado")
                        .setContentText("Datos actualizados correctamente")
                        .setConfirmClickListener {
                            it.dismiss()
                            onBackPressedDispatcher.onBackPressed()
                        }
                        .show()
                }
            }
            val btnVolver: Button = findViewById(R.id.btnVolver)
            btnVolver.setOnClickListener {
                finish() // Esto cierra esta actividad y vuelve a la anterior
            }
        }

        val btnVolver: Button = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


        // Confirmar y eliminar usuario
        elim.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("¿Estás seguro?")
                .setContentText("Esta acción eliminará el usuario permanentemente.")
                .setConfirmText("Sí, eliminar")
                .setCancelText("Cancelar")
                .setConfirmClickListener { dialog ->
                    eliminar(idusu)
                    dialog.dismissWithAnimation()

                    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Eliminado")
                        .setContentText("Usuario eliminado correctamente")
                        .setConfirmClickListener {
                            it.dismiss()
                            onBackPressedDispatcher.onBackPressed()
                        }
                        .show()
                }
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }
                .show()
        }
    }



    private fun mostrarAlerta(titulo: String, mensaje: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(titulo)
            .setContentText(mensaje)
            .show()
    }

    // Función para validar correo electrónico
    private fun esEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Validación de la contraseña robusta
    private fun esClaveRobusta(clave: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-={}|\\[\\]:;\"'<>,.?/~`])[A-Za-z\\d!@#\$%^&*()_+\\-={}|\\[\\]:;\"'<>,.?/~`]{8,}$")
        return regex.matches(clave)
    }




    private fun modificar(id: Int, nombre: String, apellido: String, correo: String, clave: String) {
        val helper = ConexionDbHelper(this)
        val db = helper.writableDatabase
        val sql = "UPDATE USUARIOS SET NOMBRE='$nombre', APELLIDO='$apellido', EMAIL='$correo', CLAVE='$clave' WHERE ID=$id"
        db.execSQL(sql)
        db.close()
    }

    private fun eliminar(id: Int) {
        val helper = ConexionDbHelper(this)
        val db = helper.writableDatabase
        val sql = "DELETE FROM USUARIOS WHERE ID=$id"
        db.execSQL(sql)
        db.close()
    }

}
