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

import it.csi.risca.riscaboweb.business.be.TipiAutorizzazioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipiAutorizzazioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiAutorizzazioneExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Tipi Autorizzazione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiAutorizzazioneApiServiceImpl extends AbstractApiServiceImpl implements TipiAutorizzazioneApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	TipiAutorizzazioneApiServiceHelper tipiAutorizzazioneApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipiAutorizzazione(String XRequestId, String XForwardedFor, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipiAutorizzazioneExtendedDTO> listTipiAutorizzazione = new ArrayList<>();
		try {
			listTipiAutorizzazione = tipiAutorizzazioneApiServiceHelper.getTipiAutorizzazione(XRequestId, XForwardedFor,
					 httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipiAutorizzazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipiAutorizzazioneByIdAmbito(String idAmbitoS, String dataIniVal, String dataFineVal,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipiAutorizzazioneExtendedDTO> listTipiAutorizzazione = new ArrayList<>();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
 
			listTipiAutorizzazione = tipiAutorizzazioneApiServiceHelper.getTipiAutorizzazioneByIdAmbito(idAmbito,
					dataIniVal, dataFineVal,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipiAutorizzazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idTipoAutorizzazione idTipoAutorizzazione
	 * @param idAmbito             idAmbito
	 * @param securityContext      SecurityContext
	 * @param httpHeaders          HttpHeaders
	 * @param httpRequest          HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito(String idTipoAutorizzazioneS, String idAmbitoS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipiAutorizzazioneExtendedDTO tipoAutorizzazione = new TipiAutorizzazioneExtendedDTO();
		try {
			Integer  idTipoAutorizzazione = ValidationFilter.validateParameter("idTipoAutorizzazione", idTipoAutorizzazioneS, 0, Integer.MAX_VALUE);
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			tipoAutorizzazione = tipiAutorizzazioneApiServiceHelper
					.getTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito(idTipoAutorizzazione, idAmbito,
							 httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoAutorizzazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param codTipoAutorizzazione codTipoAutorizzazione
	 * @param idAmbito              idAmbito
	 * @param securityContext       SecurityContext
	 * @param httpHeaders           HttpHeaders
	 * @param httpRequest           HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipoAutorizzazioneByCodeAndIdAmbito(String codTipoAutorizzazione, String idAmbitoS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipiAutorizzazioneExtendedDTO tipoAutorizzazione = new TipiAutorizzazioneExtendedDTO();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			tipoAutorizzazione = tipiAutorizzazioneApiServiceHelper.getTipoAutorizzazioneByCodeAndIdAmbito(
					codTipoAutorizzazione, idAmbito,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoAutorizzazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
