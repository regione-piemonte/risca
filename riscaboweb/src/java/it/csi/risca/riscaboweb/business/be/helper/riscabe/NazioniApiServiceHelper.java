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

import it.csi.risca.riscaboweb.business.be.NazioniApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.NazioniDTO;

public class NazioniApiServiceHelper extends AbstractServiceHelper {

    public NazioniApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<NazioniDTO> loadNazioni(boolean attivo,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[NazioniApiServiceHelper::loadNazioni] BEGIN");
            List<NazioniDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/nazioni?attivo=" + attivo;
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                NazioniApi nazioniApi = rtarget.proxy(NazioniApi.class);
                Response resp = nazioniApi.loadNazioni(attivo,  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[NazioniApiServiceHelper::loadNazioni] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<NazioniDTO>> nazioniListType = new GenericType<List<NazioniDTO>>() {
                };
                result = resp.readEntity(nazioniListType);
            } catch (ProcessingException e) {
                LOGGER.error("[NazioniApiServiceHelper::loadNazioni] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[NazioniApiServiceHelper::loadNazioni] END");
            }
            return result;
        }
        
}
