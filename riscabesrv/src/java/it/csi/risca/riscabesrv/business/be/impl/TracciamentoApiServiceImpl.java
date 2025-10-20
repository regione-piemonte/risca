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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.TracciamentoApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.TracciamentoDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.SpedizioneDTO;
import it.csi.risca.riscabesrv.dto.TracciamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class TracciamentoApiServiceImpl extends BaseApiServiceImpl  implements TracciamentoApi {

	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private TracciamentoDAO tracciamentoDAO;

    @Autowired
    private BusinessLogic businessLogic;
    
	@Override
	public Response saveTracciamento(TracciamentoDTO tracciamento, SecurityContext securityContext,
			javax.ws.rs.core.HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        try {
			businessLogic.validatorDTO(tracciamento, null, null);
			TracciamentoDTO trac = tracciamentoDAO.saveTracciamento(tracciamento);
			return Response.ok(trac).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		} catch (ValidationException ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:" + ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003, ve.getMessage(), null, null);
			return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
		} catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}

	}


}
