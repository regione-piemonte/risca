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

import it.csi.risca.riscaboweb.business.be.RegioniApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RegioneExtendedDTO;

public class RegioniApiServiceHelper extends AbstractServiceHelper {

    public RegioniApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<RegioneExtendedDTO> loadRegioni(  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[RegioniApiServiceHelper::loadRegioni] BEGIN");
            List<RegioneExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/regioni";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                RegioniApi regioniApi = rtarget.proxy(RegioniApi.class);
                Response resp = regioniApi.loadRegioni( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[RegioniApiServiceHelper::loadRegioni] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<RegioneExtendedDTO>> provinceListType = new GenericType<List<RegioneExtendedDTO>>() {
                };
                result = resp.readEntity(provinceListType);
            } catch (ProcessingException e) {
                LOGGER.error("[RegioniApiServiceHelper::loadRegioni] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[RegioniApiServiceHelper::loadRegioni] END");
            }
            return result;
        }
		public RegioneExtendedDTO loadRegioneByCodRegione(String codRegione, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
			  LOGGER.debug("[RegioniApiServiceHelper::loadRegioneByCodRegione] BEGIN");
	            RegioneExtendedDTO result = new RegioneExtendedDTO();
	            String targetUrl = this.endpointBase + "/regioni/"+codRegione;
	            try {

	                Client client = ClientBuilder.newClient();
	                WebTarget target = client.target(this.endpointBase);
	                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
	                RegioniApi regioniApi = rtarget.proxy(RegioniApi.class);
	                Response resp = regioniApi.loadRegioneByCodRegione(codRegione,  httpHeaders, httpRequest);
	                if (resp.getStatus() >= 400) {
	                    ErrorDTO err = getError(resp);
	                    LOGGER.error("[RegioniApiServiceHelper::loadRegioneByCodRegione] SERVER EXCEPTION : " + err);
	                    throw new GenericException(err);
	                }
	                GenericType<RegioneExtendedDTO> provinceListType = new GenericType<RegioneExtendedDTO>() {
	                };
	                result = resp.readEntity(provinceListType);
	            } catch (ProcessingException e) {
	                LOGGER.error("[RegioniApiServiceHelper::loadRegioneByCodRegione] EXCEPTION : " + e);
	                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	            } finally {
	                LOGGER.debug("[RegioniApiServiceHelper::loadRegioneByCodRegione] END");
	            }
	            return result;
		}
    
}
