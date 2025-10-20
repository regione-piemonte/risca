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

import it.csi.risca.riscabesrv.business.be.TipiSedeApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiSedeDAO;
import it.csi.risca.riscabesrv.dto.TipiSedeDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi sede api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiSedeApiServiceImpl implements TipiSedeApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
    	
    @Autowired
    private TipiSedeDAO tipiSedeDAO;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiSede(String tipoSoggetto, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiSedeApiServiceImpl::loadTipiSede] BEGIN");
	        List<TipiSedeDTO> listTipiSede;
			try {
				listTipiSede = tipiSedeDAO.loadTipiSede(tipoSoggetto);
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}
	        LOGGER.debug("[TipiSedeApiServiceImpl::loadTipiSede] END");
	        return Response.ok(listTipiSede).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
}
