package com.example.webservice

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MostrarUsuarios : AppCompatActivity() {

    private lateinit var listado: ListView
    private lateinit var listausuario: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_usuarios)

        listado = findViewById(R.id.listaUsuarios)

        CargarLista()

        listado.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = listausuario[position]
                val datos = item.split(" ")
                if (datos.size >= 3) {
                    val idusu = datos[0].toIntOrNull() ?: 0
                    val nombre = datos[1]
                    val apellido = datos[2]

                    val helper = ConexionDbHelper(this)
                    val db = helper.readableDatabase

                    // Obtener clave y email del usuario con ID dado
                    val cursor = db.rawQuery("SELECT clave, email FROM USUARIOS WHERE id = ?", arrayOf(idusu.toString()))
                    var clave = ""
                    var email = ""

                    if (cursor.moveToFirst()) {
                        clave = cursor.getString(0)
                        email = cursor.getString(1)
                    }
                    cursor.close()
                    db.close()

                    val intent = Intent(this@MostrarUsuarios, ModificarEliminar::class.java).apply {
                        putExtra("Id", idusu)
                        putExtra("Nombre", nombre)
                        putExtra("Apellido", apellido)
                        putExtra("Email", email)
                        putExtra("clave", clave)
                    }
                    startActivity(intent)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        CargarLista()
    }

    private fun CargarLista() {
        listausuario = listaUsuario()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listausuario)
        listado.adapter = adapter
    }

    private fun listaUsuario(): ArrayList<String> {
        val datos = ArrayList<String>()
        val helper = ConexionDbHelper(this)
        val db = helper.readableDatabase

        val sql = "SELECT id, nombre, apellido FROM USUARIOS"
        val c = db.rawQuery(sql, null)
        if (c.moveToFirst()) {
            do {
                // Solo mostramos ID, nombre y apellido
                val linea = "${c.getInt(0)} ${c.getString(1)} ${c.getString(2)}"
                datos.add(linea)
            } while (c.moveToNext())
        }
        c.close()
        db.close()
        return datos
    }
}
