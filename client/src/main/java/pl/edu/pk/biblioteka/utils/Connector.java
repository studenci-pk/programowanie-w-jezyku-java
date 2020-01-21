package pl.edu.pk.biblioteka.utils;

import org.apache.log4j.Logger;

/**
 * Klasa zwracajaca URL do komunikacji z serwerem
 */
public class Connector {

    private static Integer port = 8080;
    private static String host = "localhost";
    private static String protocol = "http";
    private static final Logger logger = Logger.getLogger(Connector.class.getName());

    public static String getBasicURL() {
        return String.format("%s://%s:%d", protocol, host, port);
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        Connector.port = port;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        Connector.host = host;
    }
}
