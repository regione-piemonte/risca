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

import it.csi.risca.riscaboweb.business.be.DettaglioPagApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CsiLogAuditDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagListDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class DettaglioPagApiServiceHelper extends AbstractServiceHelper {

	public DettaglioPagApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public Response getDettaglioPagByIdRiscossioneAndIdSD(String fruitore, Integer idRiscossione, Integer idStatoDebitorio,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[DettaglioPagApiServiceHelper::getDettaglioPagByIdRiscossioneAndIdSD] BEGIN");
		StringBuilder targetUrlBuilder = new StringBuilder(this.endpointBase + "/dettaglio-pag");
		 if (fruitore != null) {
		     targetUrlBuilder.append("?")
		                    .append("fruitore=").append(fruitore);
		 }
		if (idRiscossione != null) {
		    targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?")
		                   .append("idRiscossione=").append(idRiscossione);
		}
		if (idStatoDebitorio != null) {
		    targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?")
		                   .append("idStatoDebitorio=").append(idStatoDebitorio);
		}
		String targetUrl = targetUrlBuilder.toString();

		List<DettaglioPagExtendedDTO> result = new ArrayList<>();
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Client client = ClientBuilder.newClient();
			if (map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
				client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,
						(String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
			}
			WebTarget target = client.target(this.endpointBase);
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			DettaglioPagApi dettaglioPagApi = rtarget.proxy(DettaglioPagApi.class);
			
			Response resp = dettaglioPagApi.getDettaglioPagByIdRiscossioneAndIdSD(fruitore, idRiscossione.toString(), idStatoDebitorio.toString(),  httpHeaders, httpRequest);
			LOGGER.debug("[DettaglioPagApiServiceHelper::getDettaglioPagByIdRiscossioneAndIdSD] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[DettaglioPagApiServiceHelper::getDettaglioPagByIdRiscossioneAndIdSD] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<List<DettaglioPagExtendedDTO>> dtoType = new GenericType<List<DettaglioPagExtendedDTO>>() {};
			result = resp.readEntity(dtoType);
		} catch (ProcessingException e) {
			LOGGER.debug("[DettaglioPagApiServiceHelper::getDettaglioPagByIdRiscossioneAndIdSD] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[DettaglioPagApiServiceHelper::getDettaglioPagByIdRiscossioneAndIdSD] END");
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}
	
	public Response	deleteDettaglioPagByIdDettaglioPag(String fruitore, Integer idDettaglioPag,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] BEGIN");
		String targetUrl = this.endpointBase + "/dettaglio-pag/idDettaglioPag/"+idDettaglioPag;

		if (fruitore != null) {
			targetUrl +=  "?fruitore=" + fruitore;
		}
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Client client = ClientBuilder.newClient();
			if (map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null) {
				client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,
						(String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
			}
			WebTarget target = client.target(this.endpointBase);
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			DettaglioPagApi dettaglioPagApi = rtarget.proxy(DettaglioPagApi.class);
			Response resp = dettaglioPagApi.deleteDettaglioPagByIdDettaglioPag(idDettaglioPag.toString(), fruitore,   httpHeaders, httpRequest);
			LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<Integer> dtoType = new GenericType<Integer>() {};
			idDettaglioPag = resp.readEntity(dtoType);
		} catch (ProcessingException e) {
			LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] END");
		}
		return Response.ok(idDettaglioPag).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	
	public DettaglioPagDTO saveDettaglioPag(String fruitore, DettaglioPagDTO dettaglioPag,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[DettaglioPagApiServiceHelper::saveDettaglioPag] BEGIN");
		DettaglioPagDTO result = new DettaglioPagDTO();
		String targetUrl = this.endpointBase + "/dettaglio-pag";
        if(fruitore != null)
        	targetUrl += "?fruitore="+fruitore;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<DettaglioPagDTO> entity = Entity.json(dettaglioPag);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<DettaglioPagDTO> dettaglioPagExtendedDTO = new GenericType<DettaglioPagDTO>() {
			};
			result = resp.readEntity(dettaglioPagExtendedDTO);
		} catch (ProcessingException e) {
			LOGGER.error("[DettaglioPagApiServiceHelper::saveDettaglioPag] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[DettaglioPagApiServiceHelper::saveDettaglioPag] END");
		}
		return result;
	}
	
	public List<DettaglioPagDTO> saveDettaglioPagList(String fruitore, DettaglioPagListDTO dettaglioPagList,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)  throws GenericException {
		LOGGER.debug("[DettaglioPagApiServiceHelper::saveDettaglioPagList] BEGIN");
		List<DettaglioPagDTO> result = new ArrayList<>();
		String targetUrl;
		targetUrl = this.endpointBase + "/dettaglio-pag-list" ;
        if(fruitore != null)
        	targetUrl += "?fruitore="+fruitore;
		try {

			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<DettaglioPagListDTO> entity = Entity.json(dettaglioPagList);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[DettaglioPagApiServiceHelper::deleteDettaglioPagByIdDettaglioPag] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<List<DettaglioPagDTO>> dettaglioPagExtendedDTO = new GenericType<List<DettaglioPagDTO>>() {
			};
			result = resp.readEntity(dettaglioPagExtendedDTO);
		} catch (ProcessingException e) {
			LOGGER.error("[DettaglioPagApiServiceHelper::saveDettaglioPagList] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[DettaglioPagApiServiceHelper::saveDettaglioPagList] END");
		}
		return result;
		
	}
}
