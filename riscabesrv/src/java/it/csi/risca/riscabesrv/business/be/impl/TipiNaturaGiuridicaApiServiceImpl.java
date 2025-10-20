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

import it.csi.risca.riscabesrv.business.be.TipiNaturaGiuridicaApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiNaturaGiuridicaDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipiNaturaGiuridicaDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi natura giuridica api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiNaturaGiuridicaApiServiceImpl implements TipiNaturaGiuridicaApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);

	  @Autowired
	    private TipiNaturaGiuridicaDAO tipiNaturaGiuridicaDAO;

	    /**
	     * @param securityContext SecurityContext
	     * @param httpHeaders     HttpHeaders
	     * @param httpRequest     HttpServletRequest
	     * @return Response
	     */
		@Override
		public Response loadTipiNaturaGiuridica(SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiNaturaGiuridicaApiServiceImpl::loadTipiNaturaGiuridica] BEGIN");
	        List<TipiNaturaGiuridicaDTO> listTipiNaturaGiuridica;
			try {
				listTipiNaturaGiuridica = tipiNaturaGiuridicaDAO.loadTipiNaturaGiuridica();
			} catch (Exception e) {
				return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
						.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
			}
	        
	        LOGGER.debug("[TipiNaturaGiuridicaApiServiceImpl::loadTipiNaturaGiuridica] END");
	        return Response.ok(listTipiNaturaGiuridica).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response loadTipoNaturaGiuridicaByIdOrCod(String idTipoNaturaGiuridica, SecurityContext securityContext,
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipiNaturaGiuridicaApiServiceImpl::loadTipoNaturaGiuridicaByIdOrCod] BEGIN");
	        TipiNaturaGiuridicaDTO tipoNaturaGiuridica;
			try {
				tipoNaturaGiuridica = tipiNaturaGiuridicaDAO.loadTipoNaturaGiuridicaByIdOrCod(idTipoNaturaGiuridica);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
			}
	       
	        LOGGER.debug("[TipiNaturaGiuridicaApiServiceImpl::loadTipoNaturaGiuridicaByIdOrCod] END");
	        return Response.ok(tipoNaturaGiuridica).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
}
