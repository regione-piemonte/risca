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

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Entity;


import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TassiDiInteresseDTO;
import it.csi.risca.riscaboweb.business.be.TassiDiInteresseApi;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.EtichetteDTO;

import it.csi.risca.riscaboweb.business.be.helper.AddHeaderFilter;

public class EtichetteApiServiceHelper extends AbstractServiceHelper {
	private static final String CONFIGURAZIONE_ELEMENTI_APPLICATIVI = "/configurazioni-elementi-applicativi/idAmbito/";
	private final String className = this.getClass().getSimpleName();
	private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private static final String IDENTITY = "identity";
	
	public EtichetteApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public Response loadConfigurazioniElementiApplicativi(
			Long idAmbito, 
			String fruitore,
			MultivaluedMap<String, Object> requestHeaders) throws GenericException {
		
		LOGGER.info("idAmbito: " + idAmbito);

		// Verifica che idAmbito non sia null
		/*if (idAmbito == null) {
			String errorMessage = "idAmbito non può essere null ";
			LOGGER.error(errorMessage);
			throw new GenericException(errorMessage);
		}*/

		String targetUrl = Utils.buildTargetUrl(this.endpointBase + CONFIGURAZIONE_ELEMENTI_APPLICATIVI + idAmbito);
		Response resp = null;

		LOGGER.info("target_url: " + targetUrl);

		LOGGER.debug("[EtichetteApiServiceHelper::loadConfigurazioniElementiApplicativi] BEGIN");
		List<EtichetteDTO> result = new ArrayList<>();

		String responseBody;
		try {
			resp = getInvocationBuilder(targetUrl, requestHeaders).get();
			GenericType<List<EtichetteDTO>> etichetteListType = new GenericType<List<EtichetteDTO>>() {};
			LOGGER.info("Response Status: " + resp.getStatus());
			LOGGER.info("Response Headers: " + resp.getHeaders());
			responseBody = resp.readEntity(String.class);
			LOGGER.info("Response Body: " + responseBody);

			
			// Verifica se il tipo di contenuto è JSON
			/*if (resp.getMediaType().toString().contains("application/json")) {
				//result = objectMapper.readValue(responseBody, new TypeReference<List<EtichetteDTO>>() {});
				result = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, EtichetteDTO.class));
				//mapper.getTypeFactory().constructCollectionType(List.class, MyClass.class)
			} else {
				LOGGER.error("Content-Type della risposta non è JSON");
				throw new GenericException("Content-Type della risposta non è JSON");
			}*/
			
			
		} catch (ProcessingException e) {
			LOGGER.error("[EtichetteApiServiceHelper::loadConfigurazioniElementiApplicativi] EXCEPTION : " + e);
			throw new ProcessingException("Errore nella chiamata al servizio: " + e.getMessage() + ", " + e);
		}
		
		LOGGER.debug("[EtichetteApiServiceHelper::loadConfigurazioniElementiApplicativi] END");
		
		return Response.ok(responseBody).build();
	}
}
