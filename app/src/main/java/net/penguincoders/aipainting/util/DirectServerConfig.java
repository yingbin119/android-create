package net.penguincoders.aipainting.util;

public class DirectServerConfig {
    public static final String DIRECT_SERVER_URL = "http://10.12.52.33"; // ymx服务器URL
    public static final int DIRECT_SERVER_PORT = 5003; // ymx服务器端口

    public static String getDirectServerAddress() {
        return DIRECT_SERVER_URL + ":" + DIRECT_SERVER_PORT;
    }
}
