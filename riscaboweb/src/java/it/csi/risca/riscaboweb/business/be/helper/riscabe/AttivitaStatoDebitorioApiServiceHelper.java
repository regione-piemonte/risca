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

import it.csi.risca.riscaboweb.business.be.AttivitaStatoDebitorioApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AttivitaStatoDebitorioDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;

public class AttivitaStatoDebitorioApiServiceHelper extends AbstractServiceHelper {

    public AttivitaStatoDebitorioApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<AttivitaStatoDebitorioDTO> getAttivitaStatoDeb(String tipoAttivita,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[AttivitaStatoDebitorioApiServiceHelper::getAttivitaStatoDeb] BEGIN");
            List<AttivitaStatoDebitorioDTO> result = new ArrayList<>();
            String targetUrl;
            if(tipoAttivita == null)
            	targetUrl = this.endpointBase + "/attivita-stato-deb";
            else
            	targetUrl = this.endpointBase + "/attivita-stato-deb?tipoAttivita=" + tipoAttivita;
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                AttivitaStatoDebitorioApi attivitaApi = rtarget.proxy(AttivitaStatoDebitorioApi.class);
                Response resp = attivitaApi.getAttivitaStatoDeb(tipoAttivita,  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[AttivitaStatoDebitorioApiServiceHelper::getAttivitaStatoDeb] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<AttivitaStatoDebitorioDTO>> attivitaStatoDebListType = new GenericType<List<AttivitaStatoDebitorioDTO>>() {
                };
                result = resp.readEntity(attivitaStatoDebListType);
            } catch (ProcessingException e) {
                LOGGER.error("[AttivitaStatoDebitorioApiServiceHelper::getAttivitaStatoDeb] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[AttivitaStatoDebitorioApiServiceHelper::getAttivitaStatoDeb] END");
            }
            return result;
        }
       
}
