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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.GruppoDelegaApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.dto.GruppoDelegaExtendedDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;

/**
 * The GruppoDelegaApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class GruppoDelegaApiServiceImpl  extends BaseApiServiceImpl implements GruppoDelegaApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();
	
	@Autowired
	private IdentitaDigitaleManager identitaDigitaleManager;
	
	@Autowired
	private BusinessLogic businessLogic;

	@Override
	public Response loadGruppiDelegaByIdDelegato(String fruitore, Long idDelegato, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<GruppoDelegaExtendedDTO> listGruppoDelega = new ArrayList<>();
		try {
			
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, null);
			listGruppoDelega = businessLogic.loadGruppiDelegaByIdDelegato(idDelegato);
			
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listGruppoDelega).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
