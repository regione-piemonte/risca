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

import it.csi.risca.riscaboweb.business.be.TipiRiscossioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipiRiscossioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRiscossioneExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Tipi riscossione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiRiscossioneApiServiceImpl extends AbstractApiServiceImpl implements TipiRiscossioneApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	TipiRiscossioneApiServiceHelper tipiRiscossioneApiServiceHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipiRiscossione(String XRequestId, String XForwardedFor, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipoRiscossioneExtendedDTO> listTipiRiscossione = new ArrayList<>();
		try {
			listTipiRiscossione = tipiRiscossioneApiServiceHelper.getTipiRiscossione(XRequestId, XForwardedFor,
					 httpHeaders, httpRequest);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipiRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipiRiscossioneByIdAmbitoAndDateValidita(String idAmbitoS, String dataIniVal, String dataFineVal,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();  
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipoRiscossioneExtendedDTO> listTipiRiscossione = new ArrayList<>();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			listTipiRiscossione = tipiRiscossioneApiServiceHelper.getTipiRiscossioneByIdAmbito(idAmbito, dataIniVal,
					dataFineVal,  httpHeaders, httpRequest); 
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipiRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param idTipoRiscossione idTipoRiscossione
	 * @param idAmbito          idAmbito
	 * @param securityContext   SecurityContext
	 * @param httpHeaders       HttpHeaders
	 * @param httpRequest       HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(String idOrCodTipoRiscossione, String idAmbitoS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipoRiscossioneExtendedDTO tipoRiscossione = new TipoRiscossioneExtendedDTO();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);

			tipoRiscossione = tipiRiscossioneApiServiceHelper.getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(
					idOrCodTipoRiscossione, idAmbito,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	/**
	 * @param codTipoRiscossione codTipoRiscossione
	 * @param idAmbito           idAmbito
	 * @param securityContext    SecurityContext
	 * @param httpHeaders        HttpHeaders
	 * @param httpRequest        HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response getTipoRiscossioneByCodeAndIdAmbito(String codTipoRiscossione, String idAmbitoS,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		TipoRiscossioneExtendedDTO tipoRiscossione = new TipoRiscossioneExtendedDTO();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
			tipoRiscossione = tipiRiscossioneApiServiceHelper.getTipoRiscossioneByCodeAndIdAmbito(codTipoRiscossione,
					idAmbito,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(tipoRiscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
