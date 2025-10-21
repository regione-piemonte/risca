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

import it.csi.risca.riscaboweb.business.be.ImmagineApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.ImmagineApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ImmagineDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

@Component
public class ImmagineApiServiceImpl extends AbstractApiServiceImpl implements ImmagineApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private ImmagineApiServiceHelper immagineApiServiceHelper;

	@Override
	public Response saveImmagine(ImmagineDTO immagine, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		ImmagineDTO immagineDTO = new ImmagineDTO();
		try {
			immagineDTO = immagineApiServiceHelper.saveImmagine(immagine, fruitore, httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(immagineDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadImmagineById(String fruitore, String idImmagineS, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		ImmagineDTO immagineDTO = new ImmagineDTO();
		try {

			if (idImmagineS == null) {
				return Response.serverError().entity(Response.Status.BAD_REQUEST)
						.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
			} else {
				Integer idImmagine = ValidationFilter.validateParameter("idImmagine", idImmagineS, 0,
						Integer.MAX_VALUE);
				immagineDTO = immagineApiServiceHelper.loadImmagineById(fruitore, idImmagine, httpHeaders, httpRequest);
			}

		} catch (ParameterValidationException e) {
			return handleException(e, className, methodName);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(immagineDTO).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
