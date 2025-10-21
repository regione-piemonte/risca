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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TipoUsoRegolaApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipoUsoRegolaApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CreaUsoRegolaDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoUsoRegolaExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Tipo Uso Regola ApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component 
public class TipoUsoRegolaApiServiceImpl extends AbstractApiServiceImpl implements TipoUsoRegolaApi {

	private final String className = this.getClass().getSimpleName();

	private static final String IDENTITY = "identity";

	@Autowired
	private TipoUsoRegolaApiServiceHelper tipoUsoRegolaApiServiceHelper;

	@Override
	public Response loadAllAnniFromDTInizio(String idAmbitoS, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Integer idAmbito = null;
			if (idAmbitoS != null)
				idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			List<Integer> listAnni = tipoUsoRegolaApiServiceHelper.loadAllAnniFromDTInizio(idAmbito, fruitore,
					httpHeaders, httpRequest);
			return Response.ok(listAnni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (ParameterValidationException e) {
			return handleException(e, className, methodName);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

	}

	@Override
	public Response loadAllUsoRegolaByIdAmbitoAndAnno(String idAmbitoS, String annoS,String fruitore, String offsetS, String limitS,
			String sort,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Integer idAmbito = null;
			if (idAmbitoS != null)
				idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
			Integer anno = null;
			if (annoS != null)
				anno = ValidationFilter.validateParameter("anno", annoS, 0, Integer.MAX_VALUE);
			Integer offset = ValidationFilter.validateParameter("offset", offsetS, 0, Integer.MAX_VALUE);
			Integer limit = ValidationFilter.validateParameter("limit", limitS, 0, Integer.MAX_VALUE);

			return tipoUsoRegolaApiServiceHelper.loadAllUsoRegolaByIdAmbitoAndAnno(idAmbito, anno, offset, limit, sort,
					fruitore, httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
			return handleException(e, className, methodName);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public Response updateTipoUsoRegola(TipoUsoRegolaExtendedDTO tipoUsoRegola, String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			tipoUsoRegola = tipoUsoRegolaApiServiceHelper.updateTipoUsoRegola(tipoUsoRegola, fruitore, httpHeaders,
					httpRequest);
			return Response.ok(tipoUsoRegola).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (ParameterValidationException e) {
			return handleException(e, className, methodName);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public Response saveTipoUsoRegola(String fruitore, CreaUsoRegolaDTO usoRegola,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			return tipoUsoRegolaApiServiceHelper.saveTipoUsoRegola(fruitore, usoRegola, httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
			return handleException(e, className, methodName);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

	@Override
	public Response updateAllTipoUsoRegola(List<TipoUsoRegolaExtendedDTO> tipoUsoRegola, String fruitore,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			tipoUsoRegola = tipoUsoRegolaApiServiceHelper.updateAllTipoUsoRegola(tipoUsoRegola, fruitore, httpHeaders,
					httpRequest);
			return Response.ok(tipoUsoRegola).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (ParameterValidationException e) {
			return handleException(e, className, methodName);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
	}

}
