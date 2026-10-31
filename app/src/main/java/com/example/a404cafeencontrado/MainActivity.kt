package com.example.a404cafeencontrado

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a404cafeencontrado.activity_login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Obtener datos y referencias
        val tvWelcomeUser = findViewById<TextView>(R.id.tvWelcomeUser)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        val userName = intent.getStringExtra("USER_NAME") ?: "Usuario"
        tvWelcomeUser.text = "Bienvenido/a:\n$userName"

        // 2. Programar el clic del botón "Regresar" para volver al Login
        btnRegresar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 3. Programar los botones de opciones del menú
        findViewById<Button>(R.id.btnOption1).setOnClickListener {
            Toast.makeText(this, "Abriendo Disponibilidad de PC...", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnOption2).setOnClickListener {
            Toast.makeText(this, "Abriendo Registro de Clientes...", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnOption3).setOnClickListener {
            Toast.makeText(this, "Abriendo Snacks...", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnOption4).setOnClickListener {
            Toast.makeText(this, "Abriendo Tiempo de Reserva...", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnOption5).setOnClickListener {
            Toast.makeText(this, "Abriendo Cobro...", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnOption6).setOnClickListener {
            Toast.makeText(this, "Abriendo Reporte Final...", Toast.LENGTH_SHORT).show()
        }
    }
}