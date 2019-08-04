package org.zonex.authenticator;

import org.zonex.config.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class RequestManager {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String BASE_URL = "http://localhost/MCNotify-CloudServer/";

    public static Response sendRequest(String method, String endpoint, String body) throws IOException {
        URL obj = null;
        try {
            obj = new URL(BASE_URL + endpoint);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Cookie", "server_secret_key=" + Configuration.SECRET_KEY.getValue() + ";");
        if(body != null) {
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Content-Length", Integer.toString(body.length()));
            con.getOutputStream().write(body.getBytes());
        }
        con.connect();
        int responseCode = con.getResponseCode();
        // For debugging requests to the server
        // System.out.println(readStream(con.getErrorStream()));
        // System.out.println(readStream(con.getInputStream()));



        return new Response(responseCode, readStream(con.getInputStream()));
    }

    private static String readStream(InputStream stream) throws IOException {
        if(stream == null){
            return "";
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
