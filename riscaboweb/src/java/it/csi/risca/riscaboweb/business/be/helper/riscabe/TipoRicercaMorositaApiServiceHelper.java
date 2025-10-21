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

import it.csi.risca.riscaboweb.business.be.TipoRicercaMorositaApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRicercaMorositaDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class TipoRicercaMorositaApiServiceHelper extends AbstractServiceHelper {

    public TipoRicercaMorositaApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
    
   public List<TipoRicercaMorositaDTO> loadAllTipoRicercaMorosita( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipoRicercaMorositaApiServiceHelper::loadAllTipoRicercaMorosita] BEGIN");
            List<TipoRicercaMorositaDTO> result = new ArrayList<>();
            String targetUrl;
            targetUrl = this.endpointBase + "/tipi-ricerca-morosita";
            try {
                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipoRicercaMorositaApi tipoRicercaMorositaApi = rtarget.proxy(TipoRicercaMorositaApi.class);
                Response resp = tipoRicercaMorositaApi.loadAllTipoRicercaMorosita( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipoRicercaMorositaApiServiceHelper::loadAllTipoRicercaMorosita] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipoRicercaMorositaDTO>> tipoRicercaMorositaListType = new GenericType<List<TipoRicercaMorositaDTO>>() {
                };
                result = resp.readEntity(tipoRicercaMorositaListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipoRicercaMorositaApiServiceHelper::loadAllTipoRicercaMorosita] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipoRicercaMorositaApiServiceHelper::loadAllTipoRicercaMorosita] END");
            }
            return result;
        }

   public Response  ricercaMorosita(String fruitore, String tipoRicercaMorosita, Integer anno, Integer flgRest,
			Integer flgAnn, String lim,  Integer offset, Integer limit, String sort,
			 HttpHeaders httpHeaders,
			HttpServletRequest httpRequest)throws GenericException {
	    LOGGER.debug("[TipoRicercaMorositaApiServiceHelper::ricercaMorosita] BEGIN");
	    Response resp = null;
        String targetUrl = this.endpointBase + "/ricerca_morosita";
 		boolean firstParam = true;
 		if (fruitore != null) {
 			targetUrl += "?fruitore=" + fruitore;
 			firstParam = false;
 		}
 		if (tipoRicercaMorosita != null) {
 			targetUrl += firstParam ? "?tipoRicercaMorosita=" + tipoRicercaMorosita : "&tipoRicercaMorosita=" + tipoRicercaMorosita;
 			firstParam = false;
 		}
 		if (anno != null) {
 			targetUrl += firstParam ? "?anno=" + anno : "&anno=" + anno;
 			firstParam = false;
 		}
 		
 		if (flgRest != null) {
 			targetUrl += firstParam ? "?flgRest=" + flgRest : "&flgRest=" + flgRest;
 			firstParam = false;
 		}
 		if (flgAnn != null) {
 			targetUrl += firstParam ? "?flgAnn=" + flgAnn : "&flgAnn=" + flgAnn;
 			firstParam = false;
 		}
 		if (lim != null) {
 			targetUrl += firstParam ? "?lim=" + lim : "&lim=" + lim;
 			firstParam = false;
 		}
 		if (offset != null) {
 			targetUrl += firstParam ? "?offset=" + offset : "&offset=" + offset;
 			firstParam = false;
 		}
 		if (limit != null) {
 			targetUrl += firstParam ? "?limit=" + limit : "&limit=" + limit;
 			firstParam = false;
 		}
 		if (sort != null) {
 			targetUrl += firstParam ? "?sort=" + sort : "&sort=" + sort;
 			firstParam = false;
 		}
         try {
          	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
         
             Client client = ClientBuilder.newClient();
           	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) 
        		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
           
             WebTarget target = client.target(this.endpointBase);
             ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
				TipoRicercaMorositaApi tipoRicercaMorositaApi = rtarget.proxy(TipoRicercaMorositaApi.class);
				resp = tipoRicercaMorositaApi.ricercaMorosita(fruitore, tipoRicercaMorosita,
						anno != null ? anno.toString() : null, flgRest.toString(), flgAnn.toString(), lim,
						offset != null ? offset.toString() : null, limit != null ? limit.toString() : null, sort,
						httpHeaders, httpRequest);
             if (resp.getStatus() >= 400) {
                 ErrorDTO err = getError(resp);
                 LOGGER.error("[TipoRicercaMorositaApiServiceHelper::ricercaMorosita] SERVER EXCEPTION : " + err);
                 throw new GenericException(err);
             }
         } catch (ProcessingException e) {
             LOGGER.error("[TipoRicercaMorositaApiServiceHelper::ricercaMorosita] EXCEPTION : " + e);
             throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
         } finally {
             LOGGER.debug("[TipoRicercaMorositaApiServiceHelper::ricercaMorosita] END");
         }
         return resp;
   }

}
