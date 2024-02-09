package org.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MitigatedSSRFExampleApplication {

    private static final List<String> ALLOWED_DOMAINS = Arrays.asList("jsonplaceholder.typicode.com");

    public static void main(String[] args) {
        System.out.println("\nEJEMPLO CODIGO SIN VULNERABILIDAD");
        String imgUrl = "https://jsonplaceholder.typicode.com/todos/1";
        String response = getImage(imgUrl);
        System.out.println(response);

        System.out.println("\nEJEMPLO URI DOMINIO NO PERMITIDO");
        String imgUrl2 = "https://maliciouse.typicode.com/todos/1";
        String response2 = getImage(imgUrl2);
        System.out.println(response2);

        System.out.println("\nEJEMPLO CON VULNERABILIDAD CONTROLADA");
        String imgUrl1 = "https://jsonplaceholder.typicode.com/todos?imgUrl=https://localhost:3001/todos/1";
        String response1 = getImage(imgUrl1);
        System.out.println(response1);


        System.out.println("\nEJEMPLO CON VULNERABILIDAD CONTROLADA");
        String imgUrl3 = "http://jsonplaceholder.typicode.com/todos?imgUrl=http://localhost:3001/todos/1";
        String response3 = getImage(imgUrl3);
        System.out.println(response3);
    }

    public static String getImage(String imgUrl) {
        System.out.println("¿Es valido?:"+ isUrlAllowed(imgUrl));
        if (imgUrl != null && !imgUrl.isEmpty() && isUrlAllowed(imgUrl)) {
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(false);

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

    private static boolean isUrlAllowed(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (!ALLOWED_DOMAINS.contains(host)) {
                return false;
            }
            String urlLowerCase = url.toLowerCase();
            int countHttps = countSubstring(urlLowerCase, "https://");
            int countHttp = countSubstring(urlLowerCase, "http://");
            int countFtp = countSubstring(urlLowerCase, "ftp://");
            int countSftp = countSubstring(urlLowerCase, "sftp://");
            int countLdap = countSubstring(urlLowerCase, "ldap://");
            int countAmqp = countSubstring(urlLowerCase, "amqp://");
            int countFile = countSubstring(urlLowerCase, "file://");
            int countGopher = countSubstring(urlLowerCase, "gopher://");
            int countTFTP = countSubstring(urlLowerCase, "tftp://");
            return countHttps <= 1 && countHttp <= 1 && countFtp <= 1&& countSftp <= 1&& countLdap <= 1&& countAmqp <= 1&& countFile <= 1&& countGopher <= 1&& countTFTP <= 1;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private static int countSubstring(String str, String substr) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(substr, index)) != -1) {
            count++;
            index += substr.length();
        }
        return count;
    }

}
