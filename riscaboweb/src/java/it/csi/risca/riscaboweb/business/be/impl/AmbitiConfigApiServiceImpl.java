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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.AmbitiConfigApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.AmbitiConfigApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AmbitoConfigDTO;
import it.csi.risca.riscaboweb.util.Constants;
/**
 * The type AmbitiConfigApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class AmbitiConfigApiServiceImpl extends AbstractApiServiceImpl implements AmbitiConfigApi {
	
	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	private AmbitiConfigApiServiceHelper ambitiConfigApiServiceHelper;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadAmbitiConfig( HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			  List<AmbitoConfigDTO> listAmbitiConfig;
			try {
				listAmbitiConfig = ambitiConfigApiServiceHelper.loadAmbitiConfig(httpHeaders, httpRequest);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listAmbitiConfig).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * 
	     * @param idAmbito idAmbito
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadAmbitiConfigByIdOrCodAmbito(String idAmbito, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			 List<AmbitoConfigDTO> listAmbitoConfig;
			try {
				listAmbitoConfig = ambitiConfigApiServiceHelper.loadAmbitiConfigByIdOrCodAmbito(idAmbito,
						 httpHeaders, httpRequest);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listAmbitoConfig).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadAmbitiConfigByCodeAndKey(String codAmbito, String chiave, 
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
	        List<AmbitoConfigDTO> listAmbitoConfig;
			try {
				listAmbitoConfig = ambitiConfigApiServiceHelper.loadAmbitiConfigByCodeAndKey(getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), codAmbito, chiave);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listAmbitoConfig).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	
}
