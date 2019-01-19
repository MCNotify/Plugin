package org.mcnotify.utility;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.config.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestManager {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String BASE_URL = "http://localhost/MCNotify-CloudServer/";

    public void init(){
        JSONObject obj = new JSONObject();
        obj.put("server_name", Bukkit.getServer().getIp());
        obj.put("server_port", Bukkit.getServer().getPort());
        obj.put("server_secret_key", Configuration.SECRET_KEY);
        try {
            Response response = this.sendRequestWithoutCookies("servers.php", "POST", obj.toJSONString());
            if(response.getResponseCode() == 200){
                JSONObject json = response.getResponseBody();
                MCNotify.server_id = (Integer) json.get("server_id");
                return;
            } else {
                System.out.println("[MCNotify] ERROR: Could not reach the cloud servers. Are you connected to the internet?");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Response sendRequestWithoutCookies(String method, String endpoint, String body) throws IOException {
        URL obj = null;
        try {
            obj = new URL(BASE_URL + endpoint);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Return result
            return new Response(responseCode, response.toString());
        } else {
            // Request was invalid!
            return null;
        }
    }

    public Response sendRequest(String method, String endpoint, String body) throws IOException {
        URL obj = null;
        try {
            obj = new URL(BASE_URL + endpoint);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Cookie", "server_secret_key:" + Configuration.SECRET_KEY);
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Return result
            return new Response(responseCode, response.toString());
        } else {
            // Request was invalid!
            return null;
        }
    }
}
