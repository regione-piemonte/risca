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

import it.csi.risca.riscabesrv.business.be.TipiRecapitoApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiRecapitoDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipiRecapitoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi recapito api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiRecapitoApiServiceImpl implements TipiRecapitoApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
    	
    @Autowired
    private TipiRecapitoDAO tipiRecapitoDAO;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiRecapito(SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiRecapitoApiServiceImpl::loadTipiRecapito] BEGIN");
	        List<TipiRecapitoDTO> listTipiRecapito;
			try {
				listTipiRecapito = tipiRecapitoDAO.loadTipiRecapito();
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	       
	        LOGGER.debug("[TipiRecapitoApiServiceImpl::loadTipiRecapito] END");
	        return Response.ok(listTipiRecapito).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
}
