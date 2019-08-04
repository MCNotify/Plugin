package org.zonex.authenticator;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.zonex.config.Configuration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Authenticator {

    private int subscriptionLevel = 0;
    private int server_id = 0;

    public Authenticator(){}

    public void init(){
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("server_name", this.getIPAddress(true));
        jsonBody.put("server_port", Bukkit.getServer().getPort());
        jsonBody.put("recovery_email", Configuration.RECOVERY_EMAIL.getValue());

        try {
            Response response = RequestManager.sendRequest("GET", "servers.php", jsonBody.toJSONString());
            if(response.getResponseCode() == 200){

                // If the request was successful, determine the subscription level
                JSONObject json = response.getResponseBody();
                this.server_id = Math.toIntExact((Long) json.get("server_id"));
                this.subscriptionLevel = Math.toIntExact((Long) json.get("subscription_level"));
                System.out.println("[MCNotify] Connection successful. Subscription Level: " + subscriptionLevel);
            } else if (response.getResponseCode() == 401) {
                System.out.println("[MCNotify] ERROR: Server Secret Key is required.");
            } else {
                System.out.println("[MCNotify] ERROR: Could not reach MCNotify servers. Are you connected to the internet?");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getSubscriptionLevel(){
        return this.subscriptionLevel;
    }

    public int getServerId(){
        return this.server_id;
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
