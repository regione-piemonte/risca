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

import it.csi.risca.riscabesrv.business.be.RegioniApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.RegioniDAO;
import it.csi.risca.riscabesrv.dto.RegioneExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class RegioniApiServiceImpl implements RegioniApi{

	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
    

	@Autowired
    private RegioniDAO regioniDAO;
	
	@Override
	public Response loadRegioni(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
	    LOGGER.debug("[RegioniApiServiceImpl::loadRegioni] BEGIN");
		List<RegioneExtendedDTO> listRegione = null;
		try {
			listRegione = regioniDAO.loadRegioni();
		} catch (Exception e) {
  			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
	    LOGGER.debug("[RegioniApiServiceImpl::loadRegioni] END");
		return Response.ok(listRegione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadRegioneByCodRegione(String codRegione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
	    LOGGER.debug("[RegioniApiServiceImpl::loadRegioneByCodRegione] BEGIN");
		RegioneExtendedDTO listRegione = null;
		try {
			listRegione = regioniDAO.loadRegioniByCodRegione(codRegione);
		} catch (Exception e) {
  			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
	    LOGGER.debug("[RegioniApiServiceImpl::loadRegioneByCodRegione] END");
		return Response.ok(listRegione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
