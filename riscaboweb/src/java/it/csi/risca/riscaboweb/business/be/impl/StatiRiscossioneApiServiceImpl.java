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

import it.csi.risca.riscaboweb.business.be.StatiRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.StatiRiscossioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StatiRiscossioneExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

@Component
public class StatiRiscossioneApiServiceImpl extends AbstractApiServiceImpl implements StatiRiscossioneApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	  @Autowired
	  private StatiRiscossioneApiServiceHelper statiRiscossioneApiServiceHelper;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response getStatiRiscossione(String XRequestId, String XForwardedFor,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    	 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    	 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));

	    	 List<StatiRiscossioneExtendedDTO> listStatiRiscossioni;
			try {
				listStatiRiscossioni = statiRiscossioneApiServiceHelper.getStatiRiscossione(XRequestId, XForwardedFor, httpHeaders,httpRequest);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listStatiRiscossioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

	    /**
	     * @param idCompilante    idCompilante
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response getStatiRiscossioneByIdAmbito(String idAmbitoS,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    	 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	    	 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
             List<StatiRiscossioneExtendedDTO> listStatiRiscossione = new ArrayList<>();
	        try {
				Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

	        	listStatiRiscossione = statiRiscossioneApiServiceHelper.getStatiRiscossioneByIdAmbito(idAmbito,  httpHeaders, httpRequest);
			} catch (ParameterValidationException e) {
	        	return handleException(e, className, methodName);   

	        }  catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) { 
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listStatiRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

}
