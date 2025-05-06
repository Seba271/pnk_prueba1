package com.example.webservice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.pedant.SweetAlert.SweetAlertDialog

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtusuario = findViewById<EditText>(R.id.txtusuariologin)
        val txtclave = findViewById<EditText>(R.id.txtcontralogin)
        val loginButton = findViewById<Button>(R.id.btn_iniciar)


        loginButton.setOnClickListener {
            val usuarioIngresado = txtusuario.text.toString()
            val claveIngresada = txtclave.text.toString()

            if (usuarioIngresado == "admin" && claveIngresada == "admin") {
                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("¡Bienvenido!")
                    .setContentText("Has iniciado sesión como administrador.")
                    .setConfirmClickListener {
                        it.dismissWithAnimation()
                        val intent = Intent(this, Menu::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .show()
            } else {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Error")
                    .setContentText("Credenciales inválidas")
                    .show()
            }
        }


    }
}
