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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.ProfilazioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ProfilazioneDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class ProfilazioneApiServiceHelper extends AbstractServiceHelper {

    public ProfilazioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public ProfilazioneDTO loadProfilazione( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[ProfilazioneApiServiceHelper::loadProfilazione] BEGIN");
            ProfilazioneDTO result = new ProfilazioneDTO();
            String targetUrl = this.endpointBase + "/profilazione";
            try {
            	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
                //HttpHeaders headers 
            	Client client = null;
            	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
            		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
               }else {
                      client = ClientBuilder.newClient();
            	}
                 WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                ProfilazioneApi profilazioneApi = rtarget.proxy(ProfilazioneApi.class);
                Response resp = profilazioneApi.loadProfilazione( httpHeaders, httpRequest);
                //Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[ProfilazioneApiServiceHelper::loadProfilazione] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<ProfilazioneDTO> profilazioneListType = new GenericType<ProfilazioneDTO>() {
                };
                result = resp.readEntity(profilazioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[ProfilazioneApiServiceHelper::loadProfilazione] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[ProfilazioneApiServiceHelper::loadProfilazione] END");
            }
            return result;
        }
     
}
