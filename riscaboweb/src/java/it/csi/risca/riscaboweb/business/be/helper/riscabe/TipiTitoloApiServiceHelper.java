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

import it.csi.risca.riscaboweb.business.be.TipiTitoloApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiTitoloExtendedDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class TipiTitoloApiServiceHelper extends AbstractServiceHelper {

    public TipiTitoloApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
        public List<TipiTitoloExtendedDTO> getTipiTitolo( HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiTitoloApiServiceHelper::getTipiTitolo] BEGIN");
            List<TipiTitoloExtendedDTO> result = new ArrayList<>();
            String targetUrl = this.endpointBase + "/tipi-titolo";
            try {

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target(this.endpointBase);
                ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
                TipiTitoloApi tipiTitoloApi = rtarget.proxy(TipiTitoloApi.class);
                Response resp = tipiTitoloApi.getTipiTitolo( httpHeaders, httpRequest);
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiTitoloApiServiceHelper::getTipiTitolo] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiTitoloExtendedDTO>> tipiTitoloListType = new GenericType<List<TipiTitoloExtendedDTO>>() {
                };
                result = resp.readEntity(tipiTitoloListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiTitoloApiServiceHelper::getTipiTitolo] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiTitoloApiServiceHelper::getTipiTitolo] END");
            }
            return result;
        }
        
        public List<TipiTitoloExtendedDTO> getTipiTitoloByIdAmbito(Integer idAmbito,  String dataIniVal,  String dataFineVal,   HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiTitoloApiServiceHelper::getTipiTitoloByIdAmbito] BEGIN");
            List<TipiTitoloExtendedDTO> result = new ArrayList<>();
            String targetUrl ="";
            if(dataIniVal != null && dataFineVal != null) {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito+"/tipi-titolo?dataIniVal="+dataIniVal+"&dataFineVal="+dataFineVal;
                     
            }else if(dataIniVal != null && dataFineVal == null) {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito+"/tipi-titolo?dataIniVal="+dataIniVal;
                     
            }else if(dataIniVal == null && dataFineVal != null) {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito+"/tipi-titolo?dataFineVal="+dataFineVal;
                     
            }else {
            	targetUrl = this.endpointBase + "/ambiti/" + idAmbito+"/tipi-titolo";
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
                TipiTitoloApi tipiTitoloApi = rtarget.proxy(TipiTitoloApi.class);
                Response resp = tipiTitoloApi.getTipiTitoloByIdAmbito(idAmbito.toString(), dataIniVal, dataFineVal,  httpHeaders, httpRequest) ;
                  LOGGER.debug("[TipiTitoloApiServiceHelper::getTipiTitoloByIdAmbito] RESPONSE STATUS : " + resp.getStatus());
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiTitoloApiServiceHelper::getTipiTitoloByIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<List<TipiTitoloExtendedDTO>> tipiTitoloListType = new GenericType<List<TipiTitoloExtendedDTO>>() {
                };
                result = resp.readEntity(tipiTitoloListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiTitoloApiServiceHelper::getTipiTitoloByIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiTitoloApiServiceHelper::getTipiTitoloByIdAmbito] END");
            }
            return result;
        }
    
        
        public TipiTitoloExtendedDTO getTipoTitoloByIdTipoTitoloAndIdAmbito(Integer idTipoTitolo, Integer idAmbito,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiTitoloApiServiceHelper::getTipoTitoloByIdTipoTitoloAndIdAmbito] BEGIN");
            TipiTitoloExtendedDTO result = new TipiTitoloExtendedDTO();
            String targetUrl = this.endpointBase + "/tipi-titolo/id/" + idTipoTitolo + "/idAmbito/" + idAmbito;
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
                TipiTitoloApi tipiTitoloApi = rtarget.proxy(TipiTitoloApi.class);
                Response resp = tipiTitoloApi.getTipoTitoloByIdTipoTitoloAndIdAmbito(idTipoTitolo.toString(), idAmbito.toString(),
	    				   httpHeaders, httpRequest);
                  LOGGER.debug("[TipiTitoloApiServiceHelper::getTipoTitoloByIdTipoTitoloAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
             
                if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiTitoloApiServiceHelper::getTipoTitoloByIdTipoTitoloAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipiTitoloExtendedDTO> tipiTitoloListType = new GenericType<TipiTitoloExtendedDTO>() {
                };
                result = resp.readEntity(tipiTitoloListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiTitoloApiServiceHelper::getTipoTitoloByIdTipoTitoloAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiTitoloApiServiceHelper::getTipoTitoloByIdTipoTitoloAndIdAmbito] END");
            }
            return result;
        }
        
        public TipiTitoloExtendedDTO getTipoTitoloByCodeAndIdAmbito(String codOrIdTipoTitolo, Integer idAmbito,
				 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
            LOGGER.debug("[TipiTitoloApiServiceHelper::getTipoTitoloByCodeAndIdAmbito] BEGIN");
            TipiTitoloExtendedDTO result = new TipiTitoloExtendedDTO();
            String targetUrl = this.endpointBase + "/ambiti/" + idAmbito + "/tipi-titolo/" + codOrIdTipoTitolo; 
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
                TipiTitoloApi tipiTitoloApi = rtarget.proxy(TipiTitoloApi.class);
                Response resp = tipiTitoloApi.getTipoTitoloByCodeAndIdAmbito(codOrIdTipoTitolo, idAmbito.toString(),
        				  httpHeaders,  httpRequest);
                  LOGGER.debug("[TipiTitoloApiServiceHelper::getTipoTitoloByCodeAndIdAmbito] RESPONSE STATUS : " + resp.getStatus());
              if (resp.getStatus() >= 400) {
                    ErrorDTO err = getError(resp);
                    LOGGER.error("[TipiTitoloApiServiceHelper::getTipoTitoloByCodeAndIdAmbito] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                GenericType<TipiTitoloExtendedDTO> tipiTitoloListType = new GenericType<TipiTitoloExtendedDTO>() {
                };
                result = resp.readEntity(tipiTitoloListType);
            } catch (ProcessingException e) {
                LOGGER.error("[TipiTitoloApiServiceHelper::getTipoTitoloByCodeAndIdAmbito] EXCEPTION : " + e);
                throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
            } finally {
                LOGGER.debug("[TipiTitoloApiServiceHelper::getTipoTitoloByCodeAndIdAmbito] END");
            }
            return result;
        }
}
