package org.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SSRFExampleApplication {

    public static void main(String[] args) {
        System.out.println("\nEJEMPLO CODIGO SIN VULNERABILIDAD");
        String imgUrl = "https://jsonplaceholder.typicode.com/todos";
        String response = getImage(imgUrl);
        System.out.println(response);

        System.out.println("\nEJEMPLO CON VULNERABILIDAD SIN CONTROLAR");
        String imgUrl1 = "https://jsonplaceholder.typicode.com/todos?imgUrl=https://localhost:3001/todos/1"; // Cambia esto por la URL que desees probar
        String response1 = getImage(imgUrl1);
        System.out.println(response1);
    }

    public static String getImage(String imgUrl) {
        if (imgUrl != null && !imgUrl.isEmpty()) {
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    connection.disconnect();
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        } else {
            return "{\"mensaje\": [\"error\", \"Validacion incorrecta\", \"A10:2021 – Falsificación de Solicitudes del Lado del Servidor (SSRF)\"]}";
        }
    }
}
