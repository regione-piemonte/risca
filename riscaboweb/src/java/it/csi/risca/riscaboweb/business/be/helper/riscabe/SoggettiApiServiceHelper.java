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

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.business.be.SoggettiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorObjectDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.SoggettiExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.SoggettiExtendedResponseDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Utils;


public class SoggettiApiServiceHelper extends AbstractServiceHelper {

    public SoggettiApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }
	
    
    public SoggettiExtendedResponseDTO loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(Integer idAmbito, Integer idTipoSoggetto, String cfSoggetto,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg] BEGIN");
        SoggettiExtendedResponseDTO result = new SoggettiExtendedResponseDTO();
        String targetUrl = this.endpointBase + "/soggetti/?idAmbito=" + idAmbito + "&idTipoSoggetto=" + idTipoSoggetto + "&cfSoggetto=" + cfSoggetto;
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
            SoggettiApi soggettoApi = rtarget.proxy(SoggettiApi.class);
            Response resp = soggettoApi.loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(idAmbito.toString(),  idTipoSoggetto.toString(),  cfSoggetto,
					   httpHeaders,  httpRequest);
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[SoggettiApiServiceHelper::loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<SoggettiExtendedResponseDTO> soggetto = new GenericType<SoggettiExtendedResponseDTO>() {
            };
            result = resp.readEntity(soggetto);
        } catch (ProcessingException e) {
            LOGGER.error("[SoggettiApiServiceHelper::loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg] END");
        }
        return result;
    }
    
    public SoggettiExtendedDTO loadSoggettoById(Integer idSoggetto,String fruitore,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettoById] BEGIN");
        SoggettiExtendedDTO result = new SoggettiExtendedDTO();
        String targetUrl = this.endpointBase + "/soggetti/" + idSoggetto;
        if(fruitore != null)
        	targetUrl += "?fruitore="+fruitore;
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
           SoggettiApi soggettoApi = rtarget.proxy(SoggettiApi.class);
           Response resp = soggettoApi.loadSoggettoById(idSoggetto.toString(), fruitore,  httpHeaders, httpRequest);
           LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettoById] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[SoggettiApiServiceHelper::loadSoggettoById] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<SoggettiExtendedDTO> soggetto = new GenericType<SoggettiExtendedDTO>() {
            };
            result = resp.readEntity(soggetto);
        } catch (ProcessingException e) {
            LOGGER.error("[SoggettiApiServiceHelper::loadSoggettoById] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettoById] END");
        }
        return result;
    }
    

    public Response saveSoggetto(SoggettiExtendedDTO soggetto,String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[SoggettiApiServiceHelper::saveSoggetto] BEGIN");
        SoggettiExtendedDTO result = new SoggettiExtendedDTO();
        String targetUrl = this.endpointBase + "/soggetti";
        if(fruitore != null)
        	targetUrl += "?fruitore="+fruitore;
        String jsonWarning= null;
        try {
        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<SoggettiExtendedDTO> entity = Entity.json(soggetto);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
            LOGGER.debug("[SoggettiApiServiceHelper::saveSoggetto] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
            	ErrorObjectDTO errO = getErrorObjectDTO(resp);
                if(errO == null) {
                	ErrorDTO err = getError(resp);
                    LOGGER.debug("[SoggettiApiServiceHelper::saveSoggetto] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                LOGGER.debug("[SoggettiApiServiceHelper::saveSoggetto] SERVER EXCEPTION : " + errO);
                throw new GenericException(errO);
            }
            GenericType<SoggettiExtendedDTO> dtoType = new GenericType<SoggettiExtendedDTO>() {
            };
            result = resp.readEntity(dtoType);
            
            jsonWarning = resp.getHeaderString("jsonWarning");
        } catch (ProcessingException e) {
            LOGGER.debug("[SoggettiApiServiceHelper::saveSoggetto] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[SoggettiApiServiceHelper::saveSoggetto] END");
        }
        return Response.ok(result).header("jsonWarning",jsonWarning).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
    }


	public Response updateSoggetto(SoggettiExtendedDTO soggetto,String fruitore, Integer indModManuale,  
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[SoggettiApiServiceHelper::updateSoggetto] BEGIN");
        SoggettiExtendedDTO result = new SoggettiExtendedDTO();
        String targetUrl = this.endpointBase + "/soggetti";
		boolean firstParam = true;
		if (fruitore != null) {
			targetUrl += "?fruitore=" + fruitore;
			firstParam = false;
		}
		if (indModManuale != null) {
			targetUrl += firstParam ? "?indModManuale=" + indModManuale : "&indModManuale=" + indModManuale;
			firstParam = false;
		}
        String jsonWarning= null;
        try {
        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<SoggettiExtendedDTO> entity = Entity.json(soggetto);
            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
            LOGGER.debug("[SoggettiApiServiceHelper::updateSoggetto] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
            	ErrorObjectDTO errO = getErrorObjectDTO(resp);
                if(errO == null) {
                	ErrorDTO err = getError(resp);
                    LOGGER.debug("[SoggettiApiServiceHelper::saveSoggetto] SERVER EXCEPTION : " + err);
                    throw new GenericException(err);
                }
                LOGGER.debug("[SoggettiApiServiceHelper::saveSoggetto] SERVER EXCEPTION : " + errO);
                throw new GenericException(errO);
            }
            GenericType<SoggettiExtendedDTO> dtoType = new GenericType<SoggettiExtendedDTO>() {
            };
            result = resp.readEntity(dtoType);
            jsonWarning = resp.getHeaderString("jsonWarning");
        } catch (ProcessingException e) {
            LOGGER.debug("[SoggettiApiServiceHelper::updateSoggetto] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[SoggettiApiServiceHelper::updateSoggetto] END");
        }
        return Response.ok(result).header("jsonWarning",jsonWarning).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}
	
	public SoggettiExtendedDTO deleteSoggetto(Integer idSoggetto,String fruitore, Integer idRecapito,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[SoggettiApiServiceHelper::deleteSoggetto] BEGIN");
        SoggettiExtendedDTO result = new SoggettiExtendedDTO();
        String targetUrl = this.endpointBase + "/soggetti/" + idSoggetto;
        
		boolean firstParam = true;
		if (fruitore != null) {
			targetUrl += "?fruitore=" + fruitore;
			firstParam = false;
		}
		if (idRecapito != null) {
			targetUrl += firstParam ? "?idRecapito=" + idRecapito : "&idRecapito=" + idRecapito;
			firstParam = false;
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
            SoggettiApi soggettoApi = rtarget.proxy(SoggettiApi.class);
            Response resp = soggettoApi.deleteSoggetto(idSoggetto.toString(), fruitore, idRecapito != null ? idRecapito.toString() :null,  httpHeaders, httpRequest);
            LOGGER.debug("[SoggettiApiServiceHelper::deleteSoggetto] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.debug("[SoggettiApiServiceHelper::deleteSoggetto] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<SoggettiExtendedDTO> dtoType = new GenericType<SoggettiExtendedDTO>() {
            };
            result = resp.readEntity(dtoType);
        } catch (ProcessingException e) {
            LOGGER.debug("[SoggettiApiServiceHelper::deleteSoggetto] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[SoggettiApiServiceHelper::deleteSoggetto] END");
        }
        return result;
	}
	
    public Response loadSoggettiByCampoRicerca(Integer idAmbito, String campoRicerca, 
			 Integer offset, Integer limit,String sort,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettiByCampoRicerca] BEGIN");
        List<SoggettiExtendedDTO> result = new ArrayList<>();
        String targetUrl="";
        if (StringUtils.isNotBlank(sort) && offset != null && limit != null) {
             targetUrl = this.endpointBase + "/soggetti-qry?idAmbito=" + idAmbito + "&campoRicerca=" + campoRicerca+"&offset="+offset+"&limit="+limit+"&sort="+sort;
        }else if(StringUtils.isBlank(sort) && offset != null && limit != null) {
            targetUrl = this.endpointBase + "/soggetti-qry?idAmbito=" + idAmbito + "&campoRicerca=" + campoRicerca+"&offset="+offset+"&limit="+limit;
        }else {
            targetUrl = this.endpointBase + "/soggetti-qry?idAmbito=" + idAmbito + "&campoRicerca=" + campoRicerca;
        }
        String paginazione=null;
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
            SoggettiApi soggettoApi = rtarget.proxy(SoggettiApi.class);
            Response resp = soggettoApi.loadSoggettiByCampoRicerca(idAmbito.toString(), campoRicerca,  offset.toString(),  limit.toString(), sort,
						   httpHeaders,  httpRequest); 
            LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettiByCampoRicerca] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[SoggettiApiServiceHelper::loadSoggettiByCampoRicerca] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<List<SoggettiExtendedDTO>> dtoType = new GenericType<List<SoggettiExtendedDTO>>() {
            };
            result = resp.readEntity(dtoType);
            LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettiByCampoRicerca] Size list Soggetti "+ result.size());
            paginazione = resp.getHeaderString("PaginationInfo");
            LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettiByCampoRicerca] paginazione : "+ paginazione);
           
        } catch (ProcessingException e) {
            LOGGER.error("[SoggettiApiServiceHelper::loadSoggettiByCampoRicerca] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[SoggettiApiServiceHelper::loadSoggettiByCampoRicerca] END");
        }
        return Response.ok(result).header("PaginationInfo",paginazione).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
    }
}
