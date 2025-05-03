package com.example.webservice

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var fecha: TextView
    lateinit var temp: TextView
    lateinit var hum: TextView
    lateinit var imagenTemp: ImageView
    lateinit var datos: RequestQueue
    val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Casteo de elementos
        fecha = findViewById(R.id.txt_fecha)
        temp = findViewById(R.id.txt_temp)
        hum = findViewById(R.id.txt_humedad)
        imagenTemp = findViewById(R.id.imagen_temp)

        // Inicializamos la cola de Volley
        datos = Volley.newRequestQueue(this)

        // Iniciamos refresco automático
        mHandler.post(refrescar)
    }

    // Función para obtener fecha y hora
    private fun fechahora(): String {
        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMMM yyyy, hh:mm:ss a", Locale.getDefault())
        return sdf.format(c.time)
    }

    // Función para cambiar imagen según la temperatura
    private fun cambiarImagen(valor: Float) {
        if (valor >= 20) {
            imagenTemp.setImageResource(R.drawable.tempalta)
        } else {
            imagenTemp.setImageResource(R.drawable.tempbaja)
        }
    }

    // Función para obtener los datos del WebService
    private fun obtenerDatos() {
        val url = "https://www.pnk.cl/muestra_datos.php"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response: JSONObject ->
                try {
                    temp.text = "${response.getString("temperatura")} °C"
                    hum.text = "${response.getString("humedad")} %"

                    val valor = response.getString("temperatura").toFloat()
                    cambiarImagen(valor)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }
        )
        datos.add(request)
    }

    // Función que se ejecuta cada segundo
    private val refrescar = object : Runnable {
        override fun run() {
            fecha.text = fechahora() // Actualiza fecha
            obtenerDatos()           // Actualiza temperatura y humedad
            mHandler.postDelayed(this, 1000) // Repite cada segundo
        }
    }
}
