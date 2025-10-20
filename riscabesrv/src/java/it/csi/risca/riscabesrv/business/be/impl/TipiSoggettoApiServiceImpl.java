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

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.TipiSoggettoApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiSoggettoDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipiSoggettoDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi soggetto api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiSoggettoApiServiceImpl implements TipiSoggettoApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	  @Autowired
	    private TipiSoggettoDAO tipoSoggettoDAO;


	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiSoggetto(SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiSoggettoApiServiceImpl::loadTipiSoggetto] BEGIN");
	        List<TipiSoggettoDTO> listTipiSoggetto;
			try {
				listTipiSoggetto = tipoSoggettoDAO.loadTipiSoggetto();
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	        
	        LOGGER.debug("[TipiSoggettoApiServiceImpl::loadTipiSoggetto] END");
	        return Response.ok(listTipiSoggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

	    /**
	     * @param idTipoSoggetto idTipoSoggetto
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiSoggettoByIdOrCodTipoSoggetto(String idTipoSoggetto, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiSoggettoApiServiceImpl::loadTipiSoggettoByIdOrCodTipoSoggetto] BEGIN");
	        LOGGER.debug("[TipiSoggettoApiServiceImpl::loadTipiSoggettoByIdOrCodTipoSoggetto] Parametro in input idTipoSoggetto [" + idTipoSoggetto + "]");
	        TipiSoggettoDTO tipoSoggetto;
			try {
				tipoSoggetto = tipoSoggettoDAO.loadTipiSoggettoByIdOrCodTipoSoggetto(idTipoSoggetto);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	
	        LOGGER.debug("[TipiSoggettoApiServiceImpl::loadTipiSoggettoByIdOrCodTipoSoggetto] END");
	        return Response.ok(tipoSoggetto).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}


}
