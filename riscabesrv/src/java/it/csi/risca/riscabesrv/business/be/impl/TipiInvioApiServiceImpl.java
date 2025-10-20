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

import it.csi.risca.riscabesrv.business.be.TipiInvioApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiInvioDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipiInvioDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi invio api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiInvioApiServiceImpl implements TipiInvioApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	  @Autowired
	    private TipiInvioDAO tipoInvioDAO;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiInvio(SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiInvioApiServiceImpl::loadTipiInvio] BEGIN"); 
	        List<TipiInvioDTO> listTipiInvio;
			try {
				listTipiInvio = tipoInvioDAO.loadTipiInvio();
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	        LOGGER.debug("[TipiInvioApiServiceImpl::loadTipiInvio] END");
	        return Response.ok(listTipiInvio).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
}
