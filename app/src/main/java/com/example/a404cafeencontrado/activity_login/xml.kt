package com.example.a404cafeencontrado.activity_login

import android.content.Intent
import android.os.Bundle
import kotlin.jvm.java
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.a404cafeencontrado.R
import androidx.appcompat.app.AppCompatActivity
import com.example.a404cafeencontrado.MainActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //referencias de la interfaz utilizando su ID del XML
        val etUser = findViewById<EditText>(R.id.etUser)
        val etPass = findViewById<EditText>(R.id.etPass)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val tvForgotPass = findViewById<TextView>(R.id.tvForgotPass)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val btnSalirHeader = findViewById<Button>(R.id.btnSalirHeader)

        //funcion del boton "iniciar sesión"
        btnIniciarSesion.setOnClickListener {
            val user = etUser.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Navegación a la pantalla del menú principal
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER_NAME", user)
                startActivity(intent)
                finish()
            }
        }

        //textos interactivos
        tvForgotPass.setOnClickListener {
            Toast.makeText(this, "Módulo para recuperar contraseña", Toast.LENGTH_SHORT).show()
        }

        tvRegister.setOnClickListener {
            Toast.makeText(this, "Módulo para registro de usuario", Toast.LENGTH_SHORT).show()
        }

        btnSalirHeader.setOnClickListener {
            finishAffinity()
        }
    }
}