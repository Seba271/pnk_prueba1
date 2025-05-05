package com.example.webservice

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
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
