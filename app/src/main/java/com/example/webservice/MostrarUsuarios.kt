package com.example.webservice

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MostrarUsuarios : AppCompatActivity() {

    private lateinit var listado: ListView
    private lateinit var searchView: SearchView
    private lateinit var listausuario: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_usuarios)

        listado = findViewById(R.id.listaUsuarios)
        searchView = findViewById(R.id.searchView)

        val searchTextId = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val searchText = searchView.findViewById<EditText>(searchTextId)
        searchText.setHintTextColor(android.graphics.Color.BLACK)
        searchText.setTextColor(android.graphics.Color.BLACK)

        val searchIconId = searchView.context.resources.getIdentifier("android:id/search_mag_icon", null, null)
        val searchIcon = searchView.findViewById<ImageView>(searchIconId)
        searchIcon.setColorFilter(android.graphics.Color.BLACK)



        cargarLista()

        listado.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position) ?: return@setOnItemClickListener
            val datos = item.split(" ")
            if (datos.size >= 3) {
                val idusu = datos[0].toIntOrNull() ?: 0
                val nombre = datos[1]
                val apellido = datos[2]

                val helper = ConexionDbHelper(this)
                val db = helper.readableDatabase

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

        // Filtro en tiempo real
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
        val btnVolver: Button = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Esto cierra esta actividad y vuelve a la anterior
        }

    }

    override fun onStart() {
        super.onStart()
        cargarLista()
    }

    private fun cargarLista() {
        listausuario = obtenerUsuarios()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listausuario)
        listado.adapter = adapter
    }

    private fun obtenerUsuarios(): ArrayList<String> {
        val datos = ArrayList<String>()
        val helper = ConexionDbHelper(this)
        val db = helper.readableDatabase
        val sql = "SELECT id, nombre, apellido FROM USUARIOS"
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
}
