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

import it.csi.risca.riscaboweb.business.be.TipoDilazioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoDilazioneDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class TipoDilazioneApiServiceHelper extends AbstractServiceHelper {

    public TipoDilazioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        
        public List<TipoDilazioneDTO> loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(Integer idAmbito, Integer idTipoDilazione, 
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipoDilazioneApiServiceHelper::loadTipoDilazioneByIdAmbitoAndIdTipoDilazione] BEGIN");
            List<TipoDilazioneDTO> result = new ArrayList<>();
            String targetUrl;
            if(idTipoDilazione != null)
            	targetUrl = this.endpointBase + "/dilazione/ambito/" + idAmbito + "?idTipoDilazione=" + idTipoDilazione;
            else
            	targetUrl = this.endpointBase + "/dilazione/ambito/" + idAmbito;
            try {
            	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            	Client client = null;
            	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
             		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
                }else {
                      client = ClientBuilder.newClient();
            	}
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipoDilazioneApi tipoDilazioneApi = rtarget.proxy(TipoDilazioneApi.class);
                Response resp = tipoDilazioneApi.loadTipoDilazioneByIdAmbitoAndIdTipoDilazione(idAmbito.toString(),idTipoDilazione != null ? idTipoDilazione.toString():null,
	    				   httpHeaders, httpRequest);
                  LOGGER.debug("[TipoDilazioneApiServiceHelper::loadTipoDilazioneByIdAmbitoAndIdTipoDilazione] RESPONSE STATUS : " + resp.getStatus());
             
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipoDilazioneApiServiceHelper::loadTipoDilazioneByIdAmbitoAndIdTipoDilazione] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoDilazioneDTO>> tipoDilazioneListType = new GenericType<List<TipoDilazioneDTO>>() {
                };
                result = resp.readEntity(tipoDilazioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipoDilazioneApiServiceHelper::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipoDilazioneApiServiceHelper::loadTipiTitoloByIdAmbitoAndIdTipoDilazione] END");
            }
            return result;
        }

}
