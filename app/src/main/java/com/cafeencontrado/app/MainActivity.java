package com.cafeencontrado.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends Activity {

    private EditText emailInput, passwordInput;
    private TextView loginError;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginError = findViewById(R.id.loginError);

        loginButton = findViewById(R.id.loginButton);
        TextView goRegister = findViewById(R.id.goRegister);

        loginButton.setOnClickListener(v -> login());

        goRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        TextView forgot = findViewById(R.id.forgotPassword);

        if (forgot != null) {
            forgot.setOnClickListener(v -> recoverPassword());
        }
    }

    private void login() {

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Escribe un correo electrónico válido.");
            return;
        }

        if (password.length() < 4) {
            showError("La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        loginError.setVisibility(View.GONE);

        loginButton.setEnabled(false);
        loginButton.setText("Ingresando...");

        try {
            JSONObject datos = new JSONObject();

            datos.put("correo", email);
            datos.put("contrasena", password);

            ApiClient.post("login.php", datos, new ApiClient.ApiCallback() {

                @Override
                public void onSuccess(JSONObject response) {
                    runOnUiThread(() -> {

                        loginButton.setEnabled(true);
                        loginButton.setText("Iniciar sesión");

                        if (response.optBoolean("success")) {

                            String name = response.optString("nombre");
                            String userEmail = response.optString("correo");

                            SharedPreferences prefs =
                                    getSharedPreferences("cafe_demo", MODE_PRIVATE);

                            prefs.edit()
                                    .putBoolean("logged_in", true)
                                    .putString("user_name", name)
                                    .putString("user_email", userEmail)
                                    .apply();

                            Toast.makeText(
                                    MainActivity.this,
                                    "¡Bienvenido a 404 Café Encontrado!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent = new Intent(
                                    MainActivity.this,
                                    HomeActivity.class
                            );

                            intent.putExtra("name", name);
                            intent.putExtra("email", userEmail);

                            startActivity(intent);
                            finish();

                        } else {
                            showError(response.optString(
                                    "message",
                                    "Correo o contraseña incorrectos."
                            ));
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        loginButton.setEnabled(true);
                        loginButton.setText("Iniciar sesión");
                        showError(message);
                    });
                }
            });

        } catch (Exception e) {
            loginButton.setEnabled(true);
            loginButton.setText("Iniciar sesión");
            showError("Ocurrió un error al preparar el inicio de sesión.");
        }
    }

    private void recoverPassword() {

        new AlertDialog.Builder(this)
                .setTitle("Recuperar contraseña")
                .setMessage(
                        "La recuperación de contraseña todavía no está conectada al servidor."
                )
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void showError(String message) {
        loginError.setText(message);
        loginError.setVisibility(View.VISIBLE);
    }
}
