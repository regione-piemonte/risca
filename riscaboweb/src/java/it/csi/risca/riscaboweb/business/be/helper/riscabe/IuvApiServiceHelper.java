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

import it.csi.risca.riscaboweb.business.be.IuvApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.IuvDTO;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

public class IuvApiServiceHelper extends AbstractServiceHelper {

	public IuvApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public IuvDTO getIuvByNap(String fruitore, String nap,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[IuvApiServiceHelper::getIuvByNap] BEGIN");
		IuvDTO result = new IuvDTO();
		String targetUrl;
		targetUrl = this.endpointBase + "/iuv";
		if (fruitore != null) {
			targetUrl += "?fruitore=" + fruitore;
		}
		if (nap != null) {
			targetUrl += (fruitore != null ? "&" : "?") + "nap=" + nap;
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
			IuvApi iuvApi = rtarget.proxy(IuvApi.class);
			Response resp = iuvApi.getIuvByNap(fruitore, nap,  httpHeaders, httpRequest);
			if (resp.getStatus() >= 400) {
				ErrorDTO err = getError(resp);
				LOGGER.error("[IuvApiServiceHelper::getIuvByNap] SERVER EXCEPTION : " + err);
				throw new GenericException(err);
			}
			GenericType<IuvDTO> iuvType = new GenericType<IuvDTO>() {
			};
			result = resp.readEntity(iuvType);
		} catch (ProcessingException e) {
			LOGGER.error("[IuvApiServiceHelper::getIuvByNap] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio [ " + targetUrl + " ]");
		} finally {
			LOGGER.debug("[IuvApiServiceHelper::getIuvByNap] END");
		}
		return result;
	}

}
