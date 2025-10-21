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

import org.apache.log4j.MDC;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.TipiAutorizzazioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiAutorizzazioneExtendedDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Constants;

public class TipiAutorizzazioneApiServiceHelper extends AbstractServiceHelper {

    public TipiAutorizzazioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiAutorizzazioneExtendedDTO> getTipiAutorizzazione(String XRequestId, String XForwardedFor,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiAutorizzazioneApiServiceHelper::getTipiAutorizzazione] BEGIN");
            List<TipiAutorizzazioneExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-autorizzazione";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiAutorizzazioneApi tipiAutorizzazioneApi = rtarget.proxy(TipiAutorizzazioneApi.class);
                Response resp = tipiAutorizzazioneApi.getTipiAutorizzazione((String) MDC.get(Constants.X_REQUEST_ID), (String) MDC.get(Constants.X_FORWARDER_FOR),  httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiAutorizzazioneApiServiceHelper::getTipiAutorizzazione] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiAutorizzazioneExtendedDTO>> tipiAutorizzazioneListType = new GenericType<List<TipiAutorizzazioneExtendedDTO>>() {
                };
                result = resp.readEntity(tipiAutorizzazioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiAutorizzazioneApiServiceHelper::getTipiAutorizzazione] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiAutorizzazioneApiServiceHelper::getTipiAutorizzazione] END");
            }
            return result;
        }
        
        public List<TipiAutorizzazioneExtendedDTO> getTipiAutorizzazioneByIdAmbito(Integer idAmbito,  String dataIniVal,  String dataFineVal,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiAutorizzazioneApiServiceHelper::getTipiAutorizzazioneByIdAmbito] BEGIN");
            List<TipiAutorizzazioneExtendedDTO> result = new ArrayList<>();
            String targetUrl ="";
            if(dataIniVal != null && dataFineVal != null) {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito+"/tipi-autorizzazione?dataIniVal="+dataIniVal+"&dataFineVal="+dataFineVal ;  	          
            }else if(dataIniVal != null && dataFineVal == null) {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito+"/tipi-autorizzazione?dataIniVal="+dataIniVal;
            }else if(dataIniVal == null && dataFineVal != null) {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito+"/tipi-autorizzazione?&dataFineVal="+dataFineVal;     
            }else {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito+"/tipi-autorizzazione";
            } 
            
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
                TipiAutorizzazioneApi tipiAutorizzazioneApi = rtarget.proxy(TipiAutorizzazioneApi.class);
                Response resp = tipiAutorizzazioneApi.getTipiAutorizzazioneByIdAmbito(idAmbito.toString(), dataIniVal, dataFineVal,  httpHeaders, httpRequest);
                  LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipiAutorizzazioneByIdAmbito] RESPONSE STATUS : " + resp.getStatus());
      
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiAutorizzazioneApiServiceHelper::getTipiAutorizzazioneByIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiAutorizzazioneExtendedDTO>> tipiAutorizzazioneListType = new GenericType<List<TipiAutorizzazioneExtendedDTO>>() {
                };
                result = resp.readEntity(tipiAutorizzazioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiAutorizzazioneApiServiceHelper::getTipiAutorizzazioneByIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiAutorizzazioneApiServiceHelper::getTipiAutorizzazioneByIdAmbito] END");
            }
            return result;
        }
    
        
        public TipiAutorizzazioneExtendedDTO getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito(Integer idTipoAutorizzazione, Integer idAmbito,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiAutorizzazioneApiServiceHelper::getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] BEGIN");
            TipiAutorizzazioneExtendedDTO result = new TipiAutorizzazioneExtendedDTO();
            String targetUrl = this.endpointBase + "/tipi-autorizzazione/id/" + idTipoAutorizzazione + "/id-ambito/" + idAmbito;
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
                TipiAutorizzazioneApi tipiAutorizzazioneApi = rtarget.proxy(TipiAutorizzazioneApi.class);
                Response resp = tipiAutorizzazioneApi.getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito(idTipoAutorizzazione.toString(), idAmbito.toString(),
	    				  httpHeaders, httpRequest);
                 LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
   
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiAutorizzazioneApiServiceHelper::getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipiAutorizzazioneExtendedDTO> tipiAutorizzazioneListType = new GenericType<TipiAutorizzazioneExtendedDTO>() {
                };
                result = resp.readEntity(tipiAutorizzazioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiAutorizzazioneApiServiceHelper::getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiAutorizzazioneApiServiceHelper::getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito] END");
            }
            return result;
        }
        
        public TipiAutorizzazioneExtendedDTO getTipoAutorizzazioneByCodeAndIdAmbito(String codTipoAutorizzazione, Integer idAmbito,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiAutorizzazioneApiServiceHelper::getTipoAutorizzazioneByCodeAndIdAmbito] BEGIN");
            TipiAutorizzazioneExtendedDTO result = new TipiAutorizzazioneExtendedDTO();
            String targetUrl = this.endpointBase + "/ambiti/" + idAmbito+ "/tipi-autorizzazione/" + codTipoAutorizzazione; 
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
                TipiAutorizzazioneApi tipiAutorizzazioneApi = rtarget.proxy(TipiAutorizzazioneApi.class);
                Response resp = tipiAutorizzazioneApi.getTipoAutorizzazioneByCodeAndIdAmbito(codTipoAutorizzazione, idAmbito.toString(),
        				  httpHeaders, httpRequest);
                 LOGGER.debug("[TipiProvvedimentoApiServiceHelper::getTipoAutorizzazioneByCodeAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
  
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiAutorizzazioneApiServiceHelper::getTipoAutorizzazioneByCodeAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipiAutorizzazioneExtendedDTO> tipiAutorizzazioneListType = new GenericType<TipiAutorizzazioneExtendedDTO>() {
                };
                result = resp.readEntity(tipiAutorizzazioneListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiAutorizzazioneApiServiceHelper::getTipoAutorizzazioneByCodeAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiAutorizzazioneApiServiceHelper::getTipoAutorizzazioneByCodeAndIdAmbito] END");
            }
            return result;
        }
}
