/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
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




public class TestOauth2PetstoreSpring {

    public static void launchSniffer() {
        try {
            TcpTunnelGui.main(new String[] {"8888", "XXXXX", "80"});
        } catch (IOException e) {
        	System.out.println("[TestOauth2PetstoreSpring::launchSniffer] ERROR : " + e.getMessage());
        };
    }

    public static String ENDPOINT_BASE = "XXXXX";

    public static void main(String[] args) {
  
    }
}