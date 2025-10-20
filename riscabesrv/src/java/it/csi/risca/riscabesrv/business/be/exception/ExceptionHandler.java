/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.exception;

import java.util.Map;

import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.util.Constants;


/**
 * The type Exception handler.
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Throwable>{
	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Context private ResourceInfo resourceInfo;
	
	public Response toResponse(Throwable ex) {
		if(ex instanceof BusinessException) {
			BusinessException be = (BusinessException)ex;
			return buildErrorRepsonse(be.getHttpStatus(), be.getMessageCode(), be.getMessage(), be.getDetail());
		}
		else if(ex instanceof GenericExceptionList) {
			GenericExceptionList gel = (GenericExceptionList)ex;
			return Response.serverError().entity(gel.getErrors()).status(400).build();
		}
		// raise generic exception
		return buildErrorRepsonse(400 /* bad request */, "E001", ex.getMessage(), null);
	}
	
	protected Response buildErrorRepsonse(int httpStatus, String errcode, String message, Map<String, String> details) {
        //LOGGER.debug("[" + resourceInfo.getResourceClass().getSimpleName() + "::" + resourceInfo.getResourceMethod().getName() + "] END");

        ErrorDTO error = new ErrorDTO(""+httpStatus, errcode, message, details, null);
        return Response.serverError().entity(error).status(httpStatus).build();
	}
	
}