/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.AmbitiApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiDAO;
import it.csi.risca.riscabesrv.dto.AmbitoDTO;

/**
 * The type Abiti api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class AmbitiApiServiceImpl extends BaseApiServiceImpl implements AmbitiApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	private AmbitiDAO ambitiDAO;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */
	@Override
	public Response loadAmbiti(String codTipoElabora, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<AmbitoDTO> listAmbiti;
		try {
			if (codTipoElabora != null) {
				listAmbiti = ambitiDAO.loadAmbitiByCodTipoElabora(codTipoElabora);
			} else {
				listAmbiti = ambitiDAO.loadAmbiti();
			}
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listAmbiti).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadAmbitoById(Long idAmbito, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		AmbitoDTO ambito;
		try {
			ambito = ambitiDAO.loadAmbitoByIdAmbito(idAmbito);
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(ambito).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
