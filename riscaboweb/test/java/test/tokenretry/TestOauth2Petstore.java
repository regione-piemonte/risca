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
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

//import it.csi.risca.riscaboweb.integration.petstore.PetApi;
//import it.csi.risca.riscaboweb.integration.petstore.dto.Pet;
import it.csi.risca.riscaboweb.util.oauth2.OauthHelper;
import it.csi.risca.riscaboweb.util.oauth2.ResteasyOauthWrapper;


public class TestOauth2Petstore {

    public static void launchSniffer() {
        try {
            TcpTunnelGui.main(new String[] {"8888", "tst-interop-api-ent.ecosis.csi.it", "80"});
        } catch (IOException e) {
			System.out.println("[TestOauth2Petstore::launchSniffer] ERROR : " + e.getMessage());
        };
    }
    
    public static String ENDPOINT_BASE = "XXXXX";

    
    public static void main(String[] args) {

    }
}