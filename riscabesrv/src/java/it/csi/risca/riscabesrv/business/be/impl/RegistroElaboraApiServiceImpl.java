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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.RegistroElaboraApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegistroElaboraDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraDTO;
import it.csi.risca.riscabesrv.dto.RegistroElaboraExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class RegistroElaboraApiServiceImpl extends BaseApiServiceImpl implements RegistroElaboraApi {

	private static final String IDENTITY = "identity";
	protected static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private RegistroElaboraDAO registroElaboraDAO;

	@Autowired
	public BusinessLogic businessLogic;
	
	@Override
	public Response saveRegistroElabora(RegistroElaboraDTO registroElabora,String fruitore,  SecurityContext securityContext,
			javax.ws.rs.core.HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		RegistroElaboraDTO regElab= null;
		try {
			setGestAttoreInsUpd(registroElabora, fruitore, httpRequest, httpHeaders);

			businessLogic.validatorDTO(registroElabora, null, null);
			long start = System.currentTimeMillis();
			 regElab = registroElaboraDAO.saveRegistroElabora(registroElabora);
			long stop = System.currentTimeMillis();
			LOGGER.debug("[RegistroElaboraApiServiceImpl::saveRegistroElabora] QueryExecutionTime: " + (stop - start));
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



		return Response.ok(regElab).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();

	}

	@Override
	public Response loadRegistroElaboraByElaboraAndAmbito(String idElabora, String idAmbito, Integer esito,
			String codFaseElabora, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<RegistroElaboraExtendedDTO> listElabora;
		try {
			listElabora = registroElaboraDAO
					.loadRegistroElaboraByElaboraAndAmbito(idElabora, idAmbito, esito, codFaseElabora);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(listElabora).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
