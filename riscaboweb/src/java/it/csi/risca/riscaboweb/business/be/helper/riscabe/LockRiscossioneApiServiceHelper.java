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

import it.csi.risca.riscaboweb.business.be.LockRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.LockRiscossioneDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class LockRiscossioneApiServiceHelper extends AbstractServiceHelper {

	public LockRiscossioneApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public List<LockRiscossioneDTO> getAllLockRiscossione(String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[LockRiscossioneApiServiceHelper::getAllLockRiscossione] BEGIN");
		List<LockRiscossioneDTO> result = new ArrayList<>();
		String targetUrl = this.endpointBase + "/lock-riscossione";
		if (fruitore != null) {
			targetUrl += "?fruitore=" + fruitore;
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
			LockRiscossioneApi lockRiscossioneApi = rtarget.proxy(LockRiscossioneApi.class);
			Response resp = lockRiscossioneApi.getAllLockRiscossione(fruitore,  httpHeaders,
					httpRequest);
			LOGGER.debug(
					"[LockRiscossioneApiServiceHelper::getAllLockRiscossione] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[LockRiscossioneApiServiceHelper::getAllLockRiscossione] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			
			if (resp.getEntity() != null) {
				GenericType<List<LockRiscossioneDTO>> dtoType = new GenericType<List<LockRiscossioneDTO>>() {};
				result = resp.readEntity(dtoType);
			} 

		} catch (ProcessingException e) {
			LOGGER.debug("[LockRiscossioneApiServiceHelper::getAllLockRiscossione] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[LockRiscossioneApiServiceHelper::getAllLockRiscossione] END");
		}
		return result;
	}

	public LockRiscossioneDTO getLockRiscossioneByIdRiscossione(String fruitore, Integer idRiscossione,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[LockRiscossioneApiServiceHelper::getLockRiscossioneByIdRiscossione] BEGIN");
		LockRiscossioneDTO result = null;
		String targetUrl = this.endpointBase + "/lock-riscossione";

		if (fruitore != null) {
			targetUrl += "?fruitore=" + fruitore;
		}
		if (idRiscossione != null) {
			targetUrl += (fruitore != null ? "&" : "?") + "idRiscossione=" + idRiscossione;
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
			LockRiscossioneApi lockRiscossioneApi = rtarget.proxy(LockRiscossioneApi.class);
			Response resp = lockRiscossioneApi.getLockRiscossioneByIdRiscossione(fruitore, idRiscossione.toString(),
					 httpHeaders, httpRequest);
			LOGGER.debug("[LockRiscossioneApiServiceHelper::getLockRiscossioneByIdRiscossione] RESPONSE STATUS : "
					+ resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[LockRiscossioneApiServiceHelper::getLockRiscossioneByIdRiscossione] SERVER EXCEPTION : "
						+ err);
				throw new GenericException(err);
			}
			if (resp.hasEntity()) {
				GenericType<LockRiscossioneDTO> dtoType = new GenericType<LockRiscossioneDTO>() {};
				result = resp.readEntity(dtoType);	
			}

		} catch (ProcessingException e) {
			LOGGER.debug("[LockRiscossioneApiServiceHelper::getLockRiscossioneByIdRiscossione] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[LockRiscossioneApiServiceHelper::getLockRiscossioneByIdRiscossione] END");
		}
		return result;
	}

	public LockRiscossioneDTO saveLockRiscossione(LockRiscossioneDTO lockRiscossioneDTO, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[LockRiscossioneApiServiceHelper::saveLockRiscossione] BEGIN");
		LockRiscossioneDTO result = new LockRiscossioneDTO();
		String targetUrl = this.endpointBase + "/lock-riscossione";

		if (fruitore != null) {
			targetUrl += "?fruitore=";
		}

		try {

        	
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<LockRiscossioneDTO> entity = Entity.json(lockRiscossioneDTO);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			LOGGER.debug(
					"[LockRiscossioneApiServiceHelper::saveLockRiscossione] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[LockRiscossioneApiServiceHelper::saveLockRiscossione] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<LockRiscossioneDTO> dtoType = new GenericType<LockRiscossioneDTO>() {
			};
			result = resp.readEntity(dtoType);
		} catch (ProcessingException e) {
			LOGGER.debug("[LockRiscossioneApiServiceHelper::saveLockRiscossione] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[LockRiscossioneApiServiceHelper::saveLockRiscossione] END");
		}
		return result;
	}

	public LockRiscossioneDTO deleteLockRiscossione(Integer idRiscossione, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		LOGGER.debug("[LockRiscossioneApiServiceHelper::deleteLockRiscossione] BEGIN");
		LockRiscossioneDTO result = new LockRiscossioneDTO();
		String targetUrl = this.endpointBase + "/lock-riscossione";
		
		if (fruitore != null) {
			targetUrl += "?fruitore=" + fruitore;
		}
		if (idRiscossione != null) {
			targetUrl += (fruitore != null ? "&" : "?") + "idRiscossione=" + idRiscossione;
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
			LockRiscossioneApi lockRiscossioneApi = rtarget.proxy(LockRiscossioneApi.class);
			Response resp = lockRiscossioneApi.deleteLockRiscossione(idRiscossione.toString(), fruitore, 
					httpHeaders, httpRequest);
			LOGGER.debug(
					"[LockRiscossioneApiServiceHelper::deleteLockRiscossione] RESPONSE STATUS : " + resp.getStatus());
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.debug("[LockRiscossioneApiServiceHelper::deleteLockRiscossione] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<LockRiscossioneDTO> dtoType = new GenericType<LockRiscossioneDTO>() {
			};
			result = resp.readEntity(dtoType);
		} catch (ProcessingException e) {
			LOGGER.debug("[LockRiscossioneApiServiceHelper::deleteLockRiscossione] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[LockRiscossioneApiServiceHelper::deleteLockRiscossione] END");
		}
		return result;
	}

}
