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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.ProvvedimentiIstanzeApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorObjectDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ProvvedimentoDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class ProvvedimentiIstanzeApiServiceHelper extends AbstractServiceHelper {

    public ProvvedimentiIstanzeApiServiceHelper (String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
	

    
    public List<ProvvedimentoDTO> getProvvedimentiIstanze(MultivaluedMap<String, Object> requestHeaders) throws GenericException {
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentiIstanze] BEGIN");
        List<ProvvedimentoDTO> result = new ArrayList<>();
        String targetUrl = this.endpointBase + "/provvedimenti-istanze" ;
        try {
        	
            Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentiIstanze] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<List<ProvvedimentoDTO>> riscossione = new GenericType<List<ProvvedimentoDTO>>() {
            };
            result = resp.readEntity(riscossione);
        } catch (ProcessingException e) {
            LOGGER.error("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentiIstanze] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentiIstanze] END");
        }
        return result;
    }



	public List<ProvvedimentoDTO> getProvvedimentiIstanzeByidRiscossione(Integer idRiscossione,
			MultivaluedMap<String, Object> requestHeaders) throws GenericException {
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentiIstanzeByidRiscossione] BEGIN");
        List<ProvvedimentoDTO> result = new ArrayList<>();
        String targetUrl = this.endpointBase +"/riscossioni/"+idRiscossione+ "/provvedimenti-istanze" ;
        try {
            Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentiIstanzeByidRiscossione] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<List<ProvvedimentoDTO>> riscossione = new GenericType<List<ProvvedimentoDTO>>() {
            };
            result = resp.readEntity(riscossione);
        } catch (ProcessingException e) {
            LOGGER.error("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentiIstanzeByidRiscossione] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentiIstanzeByidRiscossione] END");
        }
        return result;
	}



	public ProvvedimentoDTO getProvvedimentoIstanzaByIdProvvedimenti(Integer idProvvedimentiIstanze,
			MultivaluedMap<String, Object> requestHeaders) throws GenericException {
		 LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentoIstanzaByIdProvvedimenti] BEGIN");
	        ProvvedimentoDTO result = new ProvvedimentoDTO();
	        String targetUrl = this.endpointBase +"/provvedimenti-istanze/"+idProvvedimentiIstanze ;
	        try {
	            Response resp = getInvocationBuilder(targetUrl, requestHeaders).get();
	            if (resp.getStatus() >= 400) {
	                ErrorDTO err = getError(resp);
	                LOGGER.error("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentoIstanzaByIdProvvedimenti] SERVER EXCEPTION : " + err);
	                throw new GenericException(err);
	            }
	            GenericType<ProvvedimentoDTO> riscossione = new GenericType<ProvvedimentoDTO>() {
	            };
	            result = resp.readEntity(riscossione);
	        } catch (ProcessingException e) {
	            LOGGER.error("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentoIstanzaByIdProvvedimenti] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::getProvvedimentoIstanzaByIdProvvedimenti] END");
	        }
	        return result;
	}



	public ProvvedimentoDTO saveProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		  LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::saveProvvedimentiIstanze] BEGIN");
		  ProvvedimentoDTO result = new ProvvedimentoDTO();
	        String targetUrl = this.endpointBase + "/provvedimenti-istanze";
	        try {
	        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
	            Entity<ProvvedimentoDTO> entity = Entity.json(provvedimentoDTO);
	            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
	            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::saveProvvedimentiIstanze] RESPONSE STATUS : " + resp.getStatus());
	          
	            if (resp.getStatus() >= 400) {
	                ErrorDTO err = getError(resp);
	                LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::saveProvvedimentiIstanze] SERVER EXCEPTION : " + err);
	                throw new GenericException(err);
	            }
	            GenericType<ProvvedimentoDTO> dtoType = new GenericType<ProvvedimentoDTO>() {
	            };
	            result = resp.readEntity(dtoType);
	        } catch (ProcessingException e) {
	            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::saveProvvedimentiIstanze] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::saveProvvedimentiIstanze] END");
	        }
	        return result;
	}



	public ProvvedimentoDTO updateProvvedimentiIstanze(ProvvedimentoDTO provvedimentoDTO, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest)  throws Exception {
		  LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::updateProvvedimentiIstanze] BEGIN");
		  ProvvedimentoDTO result = new ProvvedimentoDTO();
	        String targetUrl = this.endpointBase + "/provvedimenti-istanze";
	        try {
	        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
	            Entity<ProvvedimentoDTO> entity = Entity.json(provvedimentoDTO);
	            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
	          
	            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::updateProvvedimentiIstanze] RESPONSE STATUS : " + resp.getStatus());
	            if (resp.getStatus() >= 400) {
	                ErrorDTO err = getError(resp);
	                LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::updateProvvedimentiIstanze] SERVER EXCEPTION : " + err);
	                throw new GenericException(err);
	            }
	            GenericType<ProvvedimentoDTO> dtoType = new GenericType<ProvvedimentoDTO>() {
	            };
	            result = resp.readEntity(dtoType);
	        } catch (ProcessingException e) {
	            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::updateProvvedimentiIstanze] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::updateProvvedimentiIstanze] END");
	        }
	        return result;
	}



	public ProvvedimentoDTO deleteProvvedimentiIstanze(Integer idProvvedimentiIstanze, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
        LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::deleteProvvedimentiIstanze] BEGIN");
        ProvvedimentoDTO result = new ProvvedimentoDTO();
        String targetUrl = this.endpointBase + "/provvedimenti-istanze/" + idProvvedimentiIstanze;
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
            ProvvedimentiIstanzeApi provvedimentiIstanzeApi = rtarget.proxy(ProvvedimentiIstanzeApi.class);
            Response resp = provvedimentiIstanzeApi.deleteProvvedimentiIstanze(idProvvedimentiIstanze.toString(),  httpHeaders, httpRequest);
          
            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::deleteProvvedimentiIstanze] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
            	ErrorObjectDTO errO = getErrorObjectDTO(resp);
                if(errO == null) {
                	ErrorDTO err = getError(resp);
                    LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::deleteProvvedimentiIstanze] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::deleteProvvedimentiIstanze] SERVER EXCEPTION : " + errO);
                throw new GenericException(errO);
            }
            GenericType<ProvvedimentoDTO> dtoType = new GenericType<ProvvedimentoDTO>() {
            };
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::deleteProvvedimentiIstanze] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[ProvvedimentiIstanzeApiServiceHelper::deleteProvvedimentiIstanze] END");
        }
        return result;
	}

 

    
}