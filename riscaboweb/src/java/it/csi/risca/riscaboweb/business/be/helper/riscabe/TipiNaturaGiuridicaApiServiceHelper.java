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

import it.csi.risca.riscaboweb.business.be.TipiNaturaGiuridicaApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiNaturaGiuridicaDTO;

public class TipiNaturaGiuridicaApiServiceHelper extends AbstractServiceHelper {

    public TipiNaturaGiuridicaApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiNaturaGiuridicaDTO> loadTipiNaturaGiuridica( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiNaturaGiuridicaApiServiceHelper::loadTipiNaturaGiuridica] BEGIN");
            List<TipiNaturaGiuridicaDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-natura-giuridica";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiNaturaGiuridicaApi tipiSoggettoApi = rtarget.proxy(TipiNaturaGiuridicaApi.class);
                Response resp = tipiSoggettoApi.loadTipiNaturaGiuridica( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiNaturaGiuridicaApiServiceHelper::loadTipiNaturaGiuridica] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiNaturaGiuridicaDTO>> tipiNaturaGiuridicaListType = new GenericType<List<TipiNaturaGiuridicaDTO>>() {
                };
                result = resp.readEntity(tipiNaturaGiuridicaListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiNaturaGiuridicaApiServiceHelper::loadTipiNaturaGiuridica] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiNaturaGiuridicaApiServiceHelper::loadTipiNaturaGiuridica] END");
            }
            return result;
        }

        
        public TipiNaturaGiuridicaDTO loadTipoNaturaGiuridicaByIdOrCod(MultivaluedMap<String, Object> requestHeaders, String idTipoNaturaGiuridica) throws GenericException {
            LOGGER.debug("[TipiNaturaGiuridicaApiServiceHelper::loadTipoNaturaGiuridicaByIdOrCod] BEGIN");
            TipiNaturaGiuridicaDTO result = new TipiNaturaGiuridicaDTO();
            String targetUrl = this.endpointBase + "/tipi-natura-giuridica/" + idTipoNaturaGiuridica;
            try {
                Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiNaturaGiuridicaApiServiceHelper::loadTipoNaturaGiuridicaByIdOrCod] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipiNaturaGiuridicaDTO> tipiNaturaGiuridicaType = new GenericType<TipiNaturaGiuridicaDTO>() {
                };
                result = resp.readEntity(tipiNaturaGiuridicaType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiNaturaGiuridicaApiServiceHelper::loadTipoNaturaGiuridicaByIdOrCod] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiNaturaGiuridicaApiServiceHelper::loadTipoNaturaGiuridicaByIdOrCod] END");
            }
            return result;
        } 
        

}
