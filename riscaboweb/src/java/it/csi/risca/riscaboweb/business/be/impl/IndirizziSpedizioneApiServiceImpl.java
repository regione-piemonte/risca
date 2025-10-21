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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.IndirizziSpedizioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.GenericExceptionList;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.IndirizziSpedizioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;



/**
 * The type Indirizzi Spedizione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class IndirizziSpedizioneApiServiceImpl extends AbstractApiServiceImpl implements IndirizziSpedizioneApi {

	 private final String className = this.getClass().getSimpleName();

	@Autowired
	private IndirizziSpedizioneApiServiceHelper indSpedHelper;

	@Override
	public Response saveIndirizziSpedizione(IndirizzoSpedizioneDTO indSped,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	    IndirizzoSpedizioneDTO dto = new IndirizzoSpedizioneDTO();
        try {
			dto = indSpedHelper.saveIndirizziSpedizione(indSped,  httpHeaders, httpRequest );
		
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }

		return Response.ok(dto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

	@Override
	public Response updateIndirizziSpedizione(IndirizzoSpedizioneDTO indSped, String modVerificaS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericExceptionList {		
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		IndirizzoSpedizioneDTO dto = new IndirizzoSpedizioneDTO();
        try {
        	Integer  modVerifica= null;
        	if(modVerificaS != null)
			  modVerifica = ValidationFilter.validateParameter("modVerifica", modVerificaS, 0,Integer.MAX_VALUE);
			dto = indSpedHelper.updateIndirizziSpedizione(indSped, modVerifica,  httpHeaders, httpRequest );	
	        
		} catch (ParameterValidationException e) {
		        	return handleException(e, className, methodName);   

        } catch (GenericExceptionList e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }

		return Response.ok(dto).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();
	}

}
