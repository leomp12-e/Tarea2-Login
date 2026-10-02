package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(
                    onLoginSuccess = {
                        // Navegacion clasica con Intent.
                        // NOTA: En proyectos reales con Compose se usaria NavHost.
                        // Aqu¨ª usamos Intent para simplificar.
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                )
            }
        }
    }
}