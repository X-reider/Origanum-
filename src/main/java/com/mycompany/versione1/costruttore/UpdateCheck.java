package com.mycompany.versione1.costruttore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UpdateCheck {

    private static String user = "sathyaram"; // username itch.io
    private static String game = "origanum";   // gioco itch.io
    private static String actualVersion = new String("1.0.0");   // versione attuale del gioco

    public static String getActualVersion() {
        return actualVersion;
    }

    private static String OSArchitecture() {
        // Ottieni la proprietà di sistema os.arch
        String osArch = System.getProperty("os.arch");

        // Determina se il sistema è a 32 o 64 bit
        String architecture;
        if (osArch.equals("amd64") || osArch.equals("x86_64")) {
            architecture = "64";
        } else if (osArch.equals("x86") || osArch.equals("i386")) {
            architecture = "32";
        } else {
            //architecture = "Sconosciuto";
            architecture = "32"; //per restituire almeno un architettura valida
        }

        return architecture;
    }

    public static boolean Update() {

        String urlString = "https://itch.io/api/1/x/wharf/latest?target=" + user + "/" + game + "&channel_name=win" + OSArchitecture() + "-final";
        boolean check = false;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response
                try {
                    JsonElement jsonElement = JsonParser.parseString(response.toString());
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (jsonObject.has("latest") && !jsonObject.get("latest").isJsonNull()) {
                        String version = jsonObject.get("latest").getAsString();
                        // Per estrarre le informazioni che ti interessano dalla risposta JSON
                        if (!actualVersion.equals(version)) {
                            check = true;
                        } else {
                            check = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("GET request not worked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }
}
