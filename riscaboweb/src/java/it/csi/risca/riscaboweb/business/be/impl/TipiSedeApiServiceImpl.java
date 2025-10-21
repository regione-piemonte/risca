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

import it.csi.risca.riscaboweb.business.be.TipiSedeApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipiSedeApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiSedeDTO;

/**
 * The type Tipi sede api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiSedeApiServiceImpl extends AbstractApiServiceImpl implements TipiSedeApi {
	
	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	  @Autowired
	  private TipiSedeApiServiceHelper tipiSedeApiServiceHelper;


	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiSede(String tipoSoggetto, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) { 
			 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
			 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			List<TipiSedeDTO> listTipiSede = new ArrayList<>();
			try {
				listTipiSede = tipiSedeApiServiceHelper.loadTipiSede(tipoSoggetto,  httpHeaders, httpRequest);
	        } catch (GenericException e) {
	        	return handleException(e, className, methodName);   
	        } catch (ProcessingException e) {
	        	return handleException(e, className, methodName);   
	        } finally {
	        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
	        return Response.ok(listTipiSede).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

}
