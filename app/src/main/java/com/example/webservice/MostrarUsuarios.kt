package com.example.webservice

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
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

        // Cargar los usuarios desde la base de datos
        CargarLista()
        // Configurar el listener para el clic en un elemento de la lista
        listado.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = listausuario[position]
                val datos = item.split(" ")
                if (datos.size >= 3) {
                    val idusu = datos[0].toIntOrNull() ?: 0
                    val nombre = datos[1]
                    val apellido = datos[2]

                    // Obtener la clave desde la base de datos (sin mostrarla)
                    val helper = ConexionDbHelper(this)
                    val db = helper.readableDatabase
                    val cursor = db.rawQuery("SELECT clave FROM USUARIOS WHERE id = ?", arrayOf(idusu.toString()))
                    var clave = ""
                    if (cursor.moveToFirst()) {
                        clave = cursor.getString(0)
                    }
                    cursor.close()
                    db.close()

                    val intent = Intent(this@MostrarUsuarios, ModificarEliminar::class.java).apply {
                        putExtra("Id", idusu)
                        putExtra("Nombre", nombre)
                        putExtra("Apellido", apellido)
                        putExtra("clave", clave)
                    }
                    startActivity(intent)
                }
            }


    }

    // Método para obtener los usuarios desde la base de datos
    private fun listaUsuario(): ArrayList<String> {
        val datos = ArrayList<String>()
        val helper = ConexionDbHelper(this)
        val db = helper.readableDatabase

        val sql = "SELECT * FROM USUARIOS"
        val c = db.rawQuery(sql, null)
        if (c.moveToFirst()) {
            do {
                val linea = "${c.getInt(0)} ${c.getString(1)} ${c.getString(2)}"
                datos.add(linea)
            } while (c.moveToNext())
        }
        c.close()
        db.close()
        return datos
    }

    // Método para cargar la lista de usuarios en el ListView
    private fun CargarLista() {
        listausuario = listaUsuario() // Cargar los usuarios
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listausuario)
        listado.adapter = adapter // Asignamos el adaptador al ListView
    }
}
