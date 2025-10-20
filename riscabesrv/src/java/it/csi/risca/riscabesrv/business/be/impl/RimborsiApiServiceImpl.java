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

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import it.csi.risca.riscabesrv.business.be.RimborsiApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.business.be.impl.dao.RimborsoDAO;

/**
 * The type RimborsiApiService api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class RimborsiApiServiceImpl extends BaseApiServiceImpl implements RimborsiApi {

	private static final String IDENTITY = "identity";

	@Autowired
	RimborsoDAO rimborsoDAO;

	@Override
	public Response updateRimborsoFromRimborsoUpd(String attore, Long idElabora, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericExceptionList, ParseException {
		Integer ret;
		try {
			ret = rimborsoDAO.updateRimborsoFromRimborsoUpd(attore, idElabora);
		} catch (DAOException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			LOGGER.error("[RimborsiApiServiceImpl::updateRimborsoFromRimborsoUpd:: EXCEPTION ]: " , e);
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(ret).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
