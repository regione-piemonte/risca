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

import it.csi.risca.riscabesrv.business.be.TipiAccertamentoApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiAccertamentoDAO;
import it.csi.risca.riscabesrv.dto.TipiAccertamentoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi titolo api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiAccertamentoApiServiceImpl implements TipiAccertamentoApi {
	
	private static final String IDENTITY = "identity";
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	@Autowired
	private TipiAccertamentoDAO tipiAccertamentoDAO;


	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
	    @Override
	    public Response loadTipiAccertamento(SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiAccertamentoApiServiceImpl::loadTipiAccertamento] BEGIN");
	        List<TipiAccertamentoDTO> listTitolo;
			try {
				listTitolo = tipiAccertamentoDAO.loadTipiAccertamento();
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

			}
	        LOGGER.debug("[TipiAccertamentoApiServiceImpl::loadTipiAccertamento] END");
	        return Response.ok(listTitolo).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	    }

}
