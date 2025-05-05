package com.example.webservice

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    lateinit var splashAnimation: LottieAnimationView  // Declarar Lottie aquí

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuramos el splash screen para que no tenga la barra superior
        this.window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)  // Asegúrate que este sea el layout correcto

        splashAnimation = findViewById(R.id.lottieAnimationView)  // Inicializamos Lottie

        // Configurar la animación de Lottie
        splashAnimation.setAnimation("splashscreen_clima.json")  // Asegúrate que este es el nombre correcto de tu archivo JSON
        splashAnimation.playAnimation()

        // Hacer que el Splash se muestre durante 5 segundos antes de redirigir
        Handler().postDelayed({
            val intent = Intent(this, Login::class.java)  // Redirigir a MainActivity
            startActivity(intent)
            finish()  // Termina la actividad de Splash para que no puedan volver
        }, 5000) // 5000 milisegundos = 5 segundos
    }
}
