package com.cafeencontrado.app;

import android.app.Activity;
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

public class RegisterActivity extends Activity {

    private EditText nameInput, emailInput, passwordInput;
    private TextView error;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.registerEmail);
        passwordInput = findViewById(R.id.registerPassword);
        error = findViewById(R.id.registerError);

        findViewById(R.id.backLogin).setOnClickListener(v -> finish());

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> register());
    }

    private void register() {

        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String pass = passwordInput.getText().toString();

        String message = null;

        if (name.length() < 2) {
            message = "Escribe tu nombre completo.";
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            message = "Escribe un correo electrónico válido.";
        } else if (pass.length() < 4) {
            message = "La contraseña debe tener al menos 4 caracteres.";
        }

        if (message != null) {
            showError(message);
            return;
        }

        registerButton.setEnabled(false);
        registerButton.setText("Registrando...");

        try {
            JSONObject datos = new JSONObject();

            datos.put("nombre", name);
            datos.put("correo", email);
            datos.put("contrasena", pass);

            ApiClient.post("registro.php", datos, new ApiClient.ApiCallback() {

                @Override
                public void onSuccess(JSONObject response) {
                    runOnUiThread(() -> {

                        registerButton.setEnabled(true);
                        registerButton.setText("Crear cuenta");

                        if (response.optBoolean("success")) {

                            SharedPreferences prefs =
                                    getSharedPreferences("cafe_demo", MODE_PRIVATE);

                            prefs.edit()
                                    .putBoolean("logged_in", true)
                                    .putString("user_name", name)
                                    .putString("user_email", email)
                                    .apply();

                            Toast.makeText(
                                    RegisterActivity.this,
                                    response.optString(
                                            "message",
                                            "¡Cuenta creada correctamente!"
                                    ),
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent = new Intent(
                                    RegisterActivity.this,
                                    HomeActivity.class
                            );

                            intent.putExtra("name", name);
                            intent.putExtra("email", email);

                            startActivity(intent);
                            finish();

                        } else {
                            showError(response.optString(
                                    "message",
                                    "No se pudo crear la cuenta."
                            ));
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        registerButton.setEnabled(true);
                        registerButton.setText("Crear cuenta");
                        showError(message);
                    });
                }
            });

        } catch (Exception e) {
            registerButton.setEnabled(true);
            registerButton.setText("Crear cuenta");
            showError("Ocurrió un error al preparar el registro.");
        }
    }

    private void showError(String message) {
        error.setText(message);
        error.setVisibility(View.VISIBLE);
    }
}
