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

import it.csi.risca.riscaboweb.business.be.TipoRicercaRimborsiApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRicercaRimborsoDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class TipoRicercaRimborsiApiServiceHelper extends AbstractServiceHelper {

	public TipoRicercaRimborsiApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public List<TipoRicercaRimborsoDTO> loadAllTipoRicercaRimborsi(
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[TipoRicercaRimborsiApiServiceHelper::loadAllTipoRicercaRimborsi] BEGIN");
		List<TipoRicercaRimborsoDTO> result = new ArrayList<>();
		String targetUrl;
		targetUrl = this.endpointBase + "/tipi-ricerca-rimborsi";
		try {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(this.endpointBase);
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			TipoRicercaRimborsiApi tipoRicercaRimborsiApi = rtarget.proxy(TipoRicercaRimborsiApi.class);
			Response resp = tipoRicercaRimborsiApi.loadAllTipoRicercaRimborsi( httpHeaders,
					httpRequest);
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.error(
						"[TipoRicercaRimborsiApiServiceHelper::loadAllTipoRicercaRimborsi] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<List<TipoRicercaRimborsoDTO>> tipoRicercaMorositaListType = new GenericType<List<TipoRicercaRimborsoDTO>>() {
			};
			result = resp.readEntity(tipoRicercaMorositaListType);
		} catch (ProcessingException e) {
			LOGGER.error("[TipoRicercaRimborsiApiServiceHelper::loadAllTipoRicercaRimborsi] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[TipoRicercaRimborsiApiServiceHelper::loadAllTipoRicercaRimborsi] END");
		}
		return result;
	}

	public Response ricercaRimborsi(String fruitore, String tipoRicercaRimborsi, Integer anno, Integer offset,
			Integer limit, String sort,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[TipoRicercaRimborsiApiServiceHelper::ricercaRimborsi] BEGIN");
		Response resp = null;
		String targetUrl = this.endpointBase + "/ricerca_rimborsi";
		boolean firstParam = true;
		if (fruitore != null) {
			targetUrl +=  "?fruitore=" + fruitore;
			firstParam = false;
		}
		if (tipoRicercaRimborsi != null) {
			targetUrl += firstParam ? "?tipoRicercaRimborsi=" + tipoRicercaRimborsi
					: "&tipoRicercaRimborsi=" + tipoRicercaRimborsi;
			firstParam = false;
		}
		if (anno != null) {
			targetUrl += firstParam ? "?anno=" + anno : "&anno=" + anno;
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
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Client client = ClientBuilder.newClient();
			if (map.get(IrideIdAdapterFilter.AUTH_ID_MARKER) != null)
				client = ClientBuilder.newClient().register(new AddHeaderFilter(IrideIdAdapterFilter.AUTH_ID_MARKER,
						(String) map.get(IrideIdAdapterFilter.AUTH_ID_MARKER).get(0)));
			WebTarget target = client.target(this.endpointBase);
			ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
			TipoRicercaRimborsiApi tipoRicercaRimborsiApi = rtarget.proxy(TipoRicercaRimborsiApi.class);
			resp = tipoRicercaRimborsiApi.ricercaRimborsi(fruitore, tipoRicercaRimborsi, anno != null ? anno.toString() : null, offset.toString(), limit.toString(), sort,
					 httpHeaders, httpRequest);
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.error("[TipoRicercaRimborsiApiServiceHelper::ricercaRimborsi] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
		} catch (ProcessingException e) {
			LOGGER.error("[TipoRicercaRimborsiApiServiceHelper::ricercaRimborsi] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[TipoRicercaRimborsiApiServiceHelper::ricercaRimborsi] END");
		}
		return resp;
	}

}
