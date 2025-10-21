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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.TracciamentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TracciamentoDTO;

public class TracciamentoApiServiceHelper extends AbstractServiceHelper {

    public TracciamentoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public TracciamentoDTO saveTracciamento(TracciamentoDTO tracciamento,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TracciamentoApiServiceHelper::saveTracciamento] BEGIN");
            TracciamentoDTO result = new TracciamentoDTO();
            String targetUrl = this.endpointBase + "/tracciamento";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TracciamentoApi tracciamentoApi = rtarget.proxy(TracciamentoApi.class);
                Response resp = tracciamentoApi.saveTracciamento(tracciamento,  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TracciamentoApiServiceHelper::saveTracciamento] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TracciamentoDTO> tracciamentoType = new GenericType<TracciamentoDTO>() {
                };
                result = resp.readEntity(tracciamentoType);
            } catch (ProcessingException e) {
                LOGGER.error("[TracciamentoApiServiceHelper::saveTracciamento] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TracciamentoApiServiceHelper::saveTracciamento] END");
            }
            return result;
        }
        
}
