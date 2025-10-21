/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.MessaggiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.MessaggiDTO;

public class MessaggiApiServiceHelper extends AbstractServiceHelper {

    public MessaggiApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
    
    public List<MessaggiDTO> loadMessaggi( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[MessaggiApiServiceHelper::loadMessaggi] BEGIN");
        List<MessaggiDTO> result = new ArrayList<>();
        String targetUrl = this.endpointBase + "/messaggi";
        try {

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            MessaggiApi messaggiApi = rtarget.proxy(MessaggiApi.class);
            Response resp = messaggiApi.loadMessaggi( httpHeaders, httpRequest);
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[MessaggiApiServiceHelper::loadMessaggi] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<List<MessaggiDTO>> messaggiType = new GenericType<List<MessaggiDTO>>() {
            };
            result = resp.readEntity(messaggiType);
        } catch (ProcessingException e) {
            LOGGER.error("[MessaggiApiServiceHelper::loadMessaggi] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[MessaggiApiServiceHelper::loadMessaggi] END");
        }
        return result;
    }
    
    public MessaggiDTO loadMessaggiByCodMessaggio(String codMessaggio,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[MessaggiApiServiceHelper::loadMessaggiByCodMessaggio] BEGIN");
        MessaggiDTO result = new MessaggiDTO();
        String targetUrl = this.endpointBase + "/messaggi/" + codMessaggio;
        try {

            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            MessaggiApi messaggiApi = rtarget.proxy(MessaggiApi.class);
            Response resp = messaggiApi.loadMessaggiByCodMessaggio(codMessaggio,  httpHeaders, httpRequest);
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[MessaggiApiServiceHelper::loadMessaggiByCodMessaggio] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<MessaggiDTO> messaggiType = new GenericType<MessaggiDTO>() {
            };
            result = resp.readEntity(messaggiType);
        } catch (ProcessingException e) {
            LOGGER.error("[MessaggiApiServiceHelper::loadMessaggiByCodMessaggio] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[MessaggiApiServiceHelper::loadMessaggiByCodMessaggio] END");
        }
        return result;
    }
        
}
