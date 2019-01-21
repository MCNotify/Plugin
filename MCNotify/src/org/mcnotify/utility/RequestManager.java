package org.mcnotify.utility;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.config.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Collections;
import java.util.List;

public class RequestManager {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String BASE_URL = "http://localhost/MCNotify-CloudServer/";
    private JavaPlugin plugin;

    public RequestManager(JavaPlugin plugin){
        // Used to shutdown the plugin if a request is unauthorized.
        this.plugin = plugin;
    }

    public void init(){
        System.out.println("[MCNotify] Connecting to MCNotify servers...");

        JSONObject obj = new JSONObject();
        obj.put("server_name", this.getIPAddress(true));
        obj.put("server_port", Bukkit.getServer().getPort());
        obj.put("server_secret_key", Configuration.SECRET_KEY.getValue());
        obj.put("recovery_email", Configuration.RECOVERY_EMAIL.getValue());
        try {
            Response response = this.sendRequestWithoutCookies("POST", "servers.php", obj.toJSONString());
            if(response.getResponseCode() == 200){
                JSONObject json = response.getResponseBody();
                MCNotify.server_id = Math.toIntExact((Long) json.get("server_id"));
                System.out.println("[MCNotify] Connection successful.");
                return;
            } else if (response.getResponseCode() == 401) {
                System.out.println("[MCNotify] ERROR: Your server is unauthorized! Disabling plugin.");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
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
        if(body != null) {
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Content-Length", Integer.toString(body.length()));
            con.getOutputStream().write(body.getBytes());
        }
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
        }  else {
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
        con.setRequestProperty("Cookie", "server_secret_key=" + Configuration.SECRET_KEY.getValue() + ";server_id=" + MCNotify.server_id);
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

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            // Return result
            return new Response(responseCode, readStream(con.getInputStream()));
        } else if (responseCode == 401) {
            System.out.println("[MCNotify] ERROR: Your server is unauthorized! Disabling plugin.");
            System.out.println("[MCNotify] ERROR: Endpoint: " + endpoint + " Server response: " + readStream(con.getErrorStream()));

            //plugin.getServer().getPluginManager().disablePlugin(plugin);
            return null;
        }  else {
            // Request was invalid!
            return null;
        }
    }

    private String readStream(InputStream stream) throws IOException {
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

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    private static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }
}
