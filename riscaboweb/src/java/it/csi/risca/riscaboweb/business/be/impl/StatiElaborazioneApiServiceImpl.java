/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.StatiElaborazioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.StatiElaborazioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StatoElaborazioneDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;



@Component
public class StatiElaborazioneApiServiceImpl extends AbstractApiServiceImpl implements StatiElaborazioneApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	private StatiElaborazioneApiServiceHelper statiElaborazioneApiServiceHelper;

	@Override
	public Response loadStatiElaborazioneByIdAmbitoAndIdFunzionalita(String idAmbitoS, String idFunzionalitaS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
     List<StatoElaborazioneDTO> listStatiElaborazione = new ArrayList<>();
	       
	        try {
				Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

				Integer  idFunzionalita = ValidationFilter.validateParameter("idFunzionalita", idFunzionalitaS, 0, Integer.MAX_VALUE);


	        	listStatiElaborazione = statiElaborazioneApiServiceHelper.loadStatiElaborazioneByIdAmbitoAndIdFunzionalita(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idAmbito, idFunzionalita);
			} catch (ParameterValidationException e) {
	        	return handleException(e, className, methodName);   

	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listStatiElaborazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
