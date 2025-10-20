package test.tokenretry;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.soap.util.net.TcpTunnelGui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


//NOTA BENE: LE STRINGHE XXXXXX SONO DA VALORIZZARE CON I RIFERIMENTI OPPORTUNI

public class TestOauth2PetstoreSpring {

    public static void launchSniffer() {
        try {
            TcpTunnelGui.main(new String[] {"XXXXXX", "XXXXXX", "80"});
        } catch (IOException e) {
        	System.out.println("[TestOauth2PetstoreSpring::launchSniffer] ERROR : " + e.getMessage());
        };
    }

    public static String ENDPOINT_BASE = "XXXXXX";

    public static void main(String[] args) {

    }
}