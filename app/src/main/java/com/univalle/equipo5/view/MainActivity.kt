package com.univalle.equipo5.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.univalle.equipo5.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(5000)
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        screenSplash.setKeepOnScreenCondition { false }
        checkSession()
    }

    private fun checkSession() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // Usuario con sesión activa: carga la actividad principal con el fragmento Home
            setContentView(R.layout.activity_main)
        } else {
            // Sin sesión: redirige al login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment)
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiar SharedPreferences al cerrar la app completamente
        val sharedPreferences = getSharedPreferences("sound_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}
