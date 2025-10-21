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

import it.csi.risca.riscaboweb.business.be.AccertamentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AccertamentoExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CsiLogAuditDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorObjectDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.util.Utils;

public class AccertamentoApiServiceHelper extends AbstractServiceHelper {

    public AccertamentoApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }

	public Response loadAllAccertamentiOrByIdStatoDeb(String fruitore, Integer idStatoDebitorio, Integer offset, Integer limit, String sort,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException, ProcessingException {
        LOGGER.debug("[AccertamentoApiServiceHelper::loadAllAccertamentiOrByIdStatoDeb] BEGIN");
        List<AccertamentoExtendedDTO> result = new ArrayList<>();
        String targetUrl="";
        if(StringUtils.isBlank(sort) && offset != null && limit != null) {
        	if (idStatoDebitorio!= null) {
        		if(fruitore != null)
                    targetUrl = this.endpointBase + "/accertamenti?fruitore="+fruitore+"&idStatoDebitorio=" + idStatoDebitorio + "&offset="+offset+"&limit="+limit+"&sort="+sort;
                else
                    targetUrl = this.endpointBase + "/accertamenti?idStatoDebitorio=" + idStatoDebitorio + "&offset="+offset+"&limit="+limit+"&sort="+sort;
           }else  {
        	   if(fruitore != null)
        		   targetUrl = this.endpointBase + "/accertamenti?fruitore="+fruitore+"&offset="+offset+"&limit="+limit+"&sort="+sort;
                else
        		   targetUrl = this.endpointBase + "/accertamenti?offset="+offset+"&limit="+limit+"&sort="+sort;
           }
        }else {
        	if (idStatoDebitorio!= null) {
        		if(fruitore != null)
        			targetUrl = this.endpointBase + "/accertamenti?fruitore="+fruitore+"&idStatoDebitorio=" + idStatoDebitorio;
        		else
        		    targetUrl = this.endpointBase + "/accertamenti?idStatoDebitorio=" + idStatoDebitorio;
           }else  {
        	   if(fruitore != null)
        		   targetUrl = this.endpointBase + "/accertamenti?fruitore="+fruitore;
        	   else
                   targetUrl = this.endpointBase + "/accertamenti";
           }
        }
        
        try {

        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
        	Client client = ClientBuilder.newClient();
        	if(map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
         		  client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,  (String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
            }
            WebTarget target = client.target(this.endpointBase);
            ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
            AccertamentoApi accertamentoApi = rtarget.proxy(AccertamentoApi.class);
            Response resp = accertamentoApi.loadAllAccertamentiOrByIdStatoDeb(fruitore, idStatoDebitorio.toString(), offset.toString(), limit.toString(), sort,  httpHeaders, httpRequest);
            LOGGER.debug("[AccertamentoApiServiceHelper::loadAllAccertamentiOrByIdStatoDeb] RESPONSE STATUS : " + resp.getStatus());
            if (resp.getStatus() >= 400) {
                ErrorDTO err = getError(resp);
                LOGGER.error("[AccertamentoApiServiceHelper::loadAllAccertamentiOrByIdStatoDeb] SERVER EXCEPTION : " + err);
                throw new GenericException(err);
            }
            GenericType<List<AccertamentoExtendedDTO>> dtoType = new GenericType<List<AccertamentoExtendedDTO>>() {
            };
            result = resp.readEntity(dtoType);
            LOGGER.debug("[AccertamentoApiServiceHelper::loadAllAccertamentiOrByIdStatoDeb] Size list Soggetti "+ result.size());
           
        } catch (ProcessingException e) {
            LOGGER.error("[AccertamentoApiServiceHelper::loadAllAccertamentiOrByIdStatoDeb] EXCEPTION : " + e);
            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
        } finally {
            LOGGER.debug("[AccertamentoApiServiceHelper::loadAllAccertamentiOrByIdStatoDeb] END");
        }
        return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
  
	}


	public AccertamentoExtendedDTO saveAccertamenti(AccertamentoExtendedDTO accertamento,String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws  ProcessingException, GenericException {
	       LOGGER.debug("[AccertamentoApiServiceHelper::saveAccertamenti] BEGIN");
	       AccertamentoExtendedDTO result = new AccertamentoExtendedDTO();
	        String targetUrl = null ;
	        if(fruitore != null)
	              targetUrl = this.endpointBase + "/accertamenti?fruitore="+fruitore;
	        
	        targetUrl = this.endpointBase + "/accertamenti";
	        try {
	        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
	            Entity<AccertamentoExtendedDTO> entity = Entity.json(accertamento);
	            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
	            LOGGER.debug("[AccertamentoApiServiceHelper::saveAccertamenti] RESPONSE STATUS : " + resp.getStatus());
	            if (resp.getStatus() >= 400) {
	            	ErrorObjectDTO errO = getErrorObjectDTO(resp);
	                if(errO == null) {
	                	ErrorDTO err = getError(resp);
	                    LOGGER.debug("[AccertamentoApiServiceHelper::saveAccertamenti] SERVER EXCEPTION : " + err);
	                    throw new GenericException(err);
	                }
	                LOGGER.debug("[AccertamentoApiServiceHelper::saveAccertamenti] SERVER EXCEPTION : " + errO);
	                throw new GenericException(errO);
	            }
	            
	            GenericType<AccertamentoExtendedDTO> dtoType = new GenericType<AccertamentoExtendedDTO>() {
	            };
	            result = resp.readEntity(dtoType);
	        } catch (ProcessingException e) {
	            LOGGER.debug("[AccertamentoApiServiceHelper::saveAccertamenti] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[AccertamentoApiServiceHelper::saveAccertamenti] END");
	        }
	        return result;
	}


	public AccertamentoExtendedDTO updateAccertamenti(AccertamentoExtendedDTO accertamento,String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException, ProcessingException {
	      LOGGER.debug("[AccertamentoApiServiceHelper::updateAccertamenti] BEGIN");
	      AccertamentoExtendedDTO result = new AccertamentoExtendedDTO();
	        String targetUrl = null ;
	        if(fruitore != null)
	              targetUrl = this.endpointBase + "/accertamenti?fruitore="+fruitore;
	        
	        targetUrl = this.endpointBase + "/accertamenti";
	        try {
	        	MultivaluedMap<String, Object> map =  getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
	            Entity<AccertamentoExtendedDTO> entity = Entity.json(accertamento);
	            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
	            LOGGER.debug("[AccertamentoApiServiceHelper::updateAccertamenti] RESPONSE STATUS : " + resp.getStatus());
	            if (resp.getStatus() >= 400) {
	            	ErrorObjectDTO errO = getErrorObjectDTO(resp);
	                if(errO == null) {
	                	ErrorDTO err = getError(resp);
	                    LOGGER.debug("[AccertamentoApiServiceHelper::updateAccertamenti] SERVER EXCEPTION : " + err);
	                    throw new GenericException(err);
	                }
	                LOGGER.debug("[AccertamentoApiServiceHelper::saveAccertamenti] SERVER EXCEPTION : " + errO);
	                throw new GenericException(errO);
	            }

	            LOGGER.debug("[AccertamentoApiServiceHelper::updateAccertamenti] RESPONSE STATUS : " + resp.getStatus());
	            if (resp.getStatus() >= 400) {
	            	ErrorObjectDTO errO = getErrorObjectDTO(resp);
	                if(errO == null) {
	                	ErrorDTO err = getError(resp);
	                    LOGGER.debug("[AccertamentoApiServiceHelper::updateAccertamenti] SERVER EXCEPTION : " + err);
	                    throw new GenericException(err);
	                }
	                LOGGER.debug("[AccertamentoApiServiceHelper::updateAccertamenti] SERVER EXCEPTION : " + errO);
	                throw new GenericException(errO);
	            }
	            GenericType<AccertamentoExtendedDTO> dtoType = new GenericType<AccertamentoExtendedDTO>() {
	            };
	            result = resp.readEntity(dtoType);
	        } catch (ProcessingException e) {
	            LOGGER.debug("[AccertamentoApiServiceHelper::updateAccertamenti] EXCEPTION : " + e);
	            throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
	        } finally {
	            LOGGER.debug("[AccertamentoApiServiceHelper::updateAccertamenti] END");
	        }
	        return result;
	}
	
	public String deleteByIdAccertamento(Integer idAccertamento, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[AccertamentoApiServiceHelper::deleteByIdAccertamento] BEGIN");
		Long result = null;
		String targetUrl = this.endpointBase + "/accertamenti/idAccertamento/" + idAccertamento;
		if (fruitore != null)
			targetUrl += "?fruitore=" + fruitore;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Client client = ClientBuilder.newClient();
			if (map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null)
				client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,
						(String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));

			WebTarget target = client.target(this.endpointBase);
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			AccertamentoApi accertamentoApi = rtarget.proxy(AccertamentoApi.class);
			Response resp = accertamentoApi.deleteByIdAccertamento(idAccertamento.toString(), fruitore, httpHeaders,
					httpRequest);

			handleResponseErrors(resp);
			GenericType<Long> idPagamentoGe = new GenericType<Long>() {
			};
			result = resp.readEntity(idPagamentoGe);
		} catch (ProcessingException e) {
			LOGGER.error("[AccertamentoApiServiceHelper::deleteByIdAccertamento] EXCEPTION : " + e);
			throw new ProcessingException(e);
		} finally {
			LOGGER.debug("[AccertamentoApiServiceHelper::deleteByIdAccertamento] END");
		}
		return idAccertamento.toString();
	}
	
}
