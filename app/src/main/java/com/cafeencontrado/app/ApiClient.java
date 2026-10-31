
package com.cafeencontrado.app;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final String BASE_URL =
            "http://10.0.2.2/cafe_api/";

    public interface ApiCallback {
        void onSuccess(JSONObject response);
        void onError(String message);
    }

    public static void post(
            String endpoint,
            JSONObject data,
            ApiCallback callback
    ) {
        new Thread(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL(BASE_URL + endpoint);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setDoOutput(true);
                connection.setRequestProperty(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );

                byte[] body = data.toString().getBytes(StandardCharsets.UTF_8);

                try (OutputStream output = connection.getOutputStream()) {
                    output.write(body);
                }

                int status = connection.getResponseCode();

                InputStream input = status >= 200 && status < 300
                        ? connection.getInputStream()
                        : connection.getErrorStream();

                StringBuilder result = new StringBuilder();

                if (input != null) {
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(input, StandardCharsets.UTF_8))) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                    }
                }

                JSONObject response = new JSONObject(result.toString());

                if (status >= 200 && status < 300) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(
                            response.optString(
                                    "message",
                                    "Error al comunicarse con el servidor."
                            )
                    );
                }

            } catch (Exception e) {
                callback.onError(
                        "No se pudo conectar con el servidor. Verifica que XAMPP esté iniciado."
                );

            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
