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

import java.math.BigDecimal;

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

import it.csi.risca.riscaboweb.business.be.CalcoloInteressiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class CalcoloInteressiApiServiceHelper  extends AbstractServiceHelper {

    public CalcoloInteressiApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
	public BigDecimal calcoloInteressi(String fruitore, BigDecimal importo, String dataScadenza, String dataVersamento,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)  throws GenericException, ProcessingException{
		    LOGGER.debug("[CalcoloInteressiApiServiceHelper::calcoloInteressi] BEGIN");
	        BigDecimal result = null;
	        StringBuilder targetUrlBuilder = new StringBuilder(this.endpointBase + "/calcolo-interessi");
		   	 if (fruitore != null) {
			     targetUrlBuilder.append("?")
			                    .append("fruitore=").append(fruitore);
			 }
	        if (importo != null) {
	            targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?")
	                           .append("importo=").append(importo);
	        }

	        if (dataScadenza != null) {
	            targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?")
	                           .append("dataScadenza=").append(dataScadenza);
	        }

	        if (dataVersamento != null) {
	            targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?")
	                           .append("dataVersamento=").append(dataVersamento);
	        }

	        String targetUrl = targetUrlBuilder.toString();

			
	        try {
	
	        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
	        	Client client = ClientBuilder.newClient();
	        	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
	         		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
	            }
	            WebTarget target = client.target(this.endpointBase);
	            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
	            CalcoloInteressiApi calcoloInteressiApi = rtarget.proxy(CalcoloInteressiApi.class);
	            Response resp = calcoloInteressiApi.calcoloInteressi(fruitore, importo, dataScadenza, dataVersamento,  httpHeaders, httpRequest);
	            LOGGER.debug("[CalcoloInteressiApiServiceHelper::calcoloInteressi] RESPONSE STATUS : " + resp.getStatus());
	            if (resp.getStatus() >= 400) {
	                ErrorDTO err = getError(resp);
	                LOGGER.error("[CalcoloInteressiApiServiceHelper::calcoloInteressi] SERVER EXCEPTION : " + err);
	                throw new GenericException(err);
	            }
	            GenericType<BigDecimal> dtoType = new GenericType<BigDecimal>() {};
	            result = resp.readEntity(dtoType);
	            
	        } catch (ProcessingException e) {
	            LOGGER.error("[CalcoloInteressiApiServiceHelper::calcoloInteressi] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[CalcoloInteressiApiServiceHelper::calcoloInteressi] END");
	        }
	        return result;
  
		
	}

}
