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

import it.csi.risca.riscaboweb.business.be.TipiProvvedimentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipiProvvedimentoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipiProvvedimentoExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Tipi Provvedimento api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiProvvedimentoApiServiceImpl extends AbstractApiServiceImpl implements TipiProvvedimentoApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	TipiProvvedimentoApiServiceHelper tipiProvvedimentoApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipiProvvedimento( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipiProvvedimentoExtendedDTO> listTipiProvvedimento = new ArrayList<>();
		try {
			listTipiProvvedimento = tipiProvvedimentoApiServiceHelper.getTipiProvvedimento( httpHeaders,
					httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipiProvvedimento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipiProvvedimentoByIdAmbitoAndFlgIstanza(String idAmbitoS, String flgIstanzaS, String dataIniVal,
			String dataFineVal,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) { 
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipiProvvedimentoExtendedDTO> listTipiProvvedimento = new ArrayList<>();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
			Integer  flgIstanza = ValidationFilter.validateParameter("flgIstanza", flgIstanzaS, 0,1);

			listTipiProvvedimento = tipiProvvedimentoApiServiceHelper.getTipiProvvedimentoByIdAmbitoAndFlgIstanza(
					idAmbito, flgIstanza, dataIniVal, dataFineVal,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipiProvvedimento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idTipoProvvedimento idTipoProvvedimento
	 * @param idAmbito            idAmbito
	 * @param securityContext     SecurityContext
	 * @param httpHeaders         HttpHeaders
	 * @param httpRequest         HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito(String idTipoProvvedimentoS, String idAmbitoS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipiProvvedimentoExtendedDTO tipoProvvedimento = new TipiProvvedimentoExtendedDTO();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
			Integer  idTipoProvvedimento = ValidationFilter.validateParameter("idTipoProvvedimento", idTipoProvvedimentoS, 0, Integer.MAX_VALUE);
			tipoProvvedimento = tipiProvvedimentoApiServiceHelper.getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito( 
					idTipoProvvedimento, idAmbito,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoProvvedimento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param codTipoProvvedimento codTipoProvvedimento
	 * @param idAmbito             idAmbito
	 * @param securityContext      SecurityContext
	 * @param httpHeaders          HttpHeaders
	 * @param httpRequest          HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipoProvvedimentoByCodeAndIdAmbito(String codOrIdTipoProvvedimento, String idAmbitoS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipiProvvedimentoExtendedDTO tipoProvvedimento = new TipiProvvedimentoExtendedDTO();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
 
			tipoProvvedimento = tipiProvvedimentoApiServiceHelper.getTipoProvvedimentoByCodeAndIdAmbito(
					codOrIdTipoProvvedimento, idAmbito,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoProvvedimento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
