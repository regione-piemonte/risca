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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.TipiSoggettoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiSoggettoDTO;

public class TipiSoggettoApiServiceHelper extends AbstractServiceHelper {

    public TipiSoggettoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiSoggettoDTO> loadTipiSoggetto( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiSoggettoApiServiceHelper::loadTipiSoggetto] BEGIN");
            List<TipiSoggettoDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-soggetto";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiSoggettoApi tipiSoggettoApi = rtarget.proxy(TipiSoggettoApi.class);
                Response resp = tipiSoggettoApi.loadTipiSoggetto( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiSoggettoApiServiceHelper::loadTipiSoggetto] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiSoggettoDTO>> tipiSoggettoListType = new GenericType<List<TipiSoggettoDTO>>() {
                };
                result = resp.readEntity(tipiSoggettoListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiSoggettoApiServiceHelper::loadTipiSoggetto] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiSoggettoApiServiceHelper::loadTipiSoggetto] END");
            }
            return result;
        }

        
        public TipiSoggettoDTO loadTipiSoggettoByIdOrCodTipoSoggetto(MultivaluedMap<String, Object> requestHeaders, String idTipoSoggetto) throws GenericException {
            LOGGER.debug("[TipiSoggettoApiServiceHelper::loadTipiSoggettoByIdOrCodTipoSoggetto] BEGIN");
            TipiSoggettoDTO result = new TipiSoggettoDTO();
            String targetUrl = this.endpointBase + "/tipi-soggetto/" + idTipoSoggetto;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiSoggettoApiServiceHelper::loadTipiSoggettoByIdOrCodTipoSoggetto] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipiSoggettoDTO> tipiSoggettoType = new GenericType<TipiSoggettoDTO>() {
                };
                result = resp.readEntity(tipiSoggettoType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiSoggettoApiServiceHelper::loadTipiSoggettoByIdOrCodTipoSoggetto] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiSoggettoApiServiceHelper::loadTipiSoggettoByIdOrCodTipoSoggetto] END");
            }
            return result;
        } 
        

}
