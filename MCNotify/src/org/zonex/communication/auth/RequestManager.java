package org.zonex.communication.auth;

import org.zonex.config.Configuration;

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
        con.setRequestProperty("Cookie", "server_secret_key=" + getIPAddress(true));
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
