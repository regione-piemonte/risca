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

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.StatoContribuzioneApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatoContribuzioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.StatoContribuzioneDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Tipi recapito api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class StatoContribuzioneApiServiceImpl implements StatoContribuzioneApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
    	
    @Autowired
    private StatoContribuzioneDAO statoContribuzioneDAO;

		@Override
		public Response loadStatoContribuzione() {
	        LOGGER.debug("[StatoContribuzioneApiServiceImpl::loadStatoContribuzione] BEGIN");
	        List<StatoContribuzioneDTO> listStatoContribuzione;
			try {
				listStatoContribuzione = statoContribuzioneDAO.loadStatoContribuzione();
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	        LOGGER.debug("[StatoContribuzioneApiServiceImpl::loadStatoContribuzione] END");
	        return Response.ok(listStatoContribuzione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}

		@Override
		public Response loadStatoContribuzioneById(Long idStatoContribuzione) {
	        LOGGER.debug("[StatoContribuzioneApiServiceImpl::loadStatoContribuzioneById] BEGIN");
	        StatoContribuzioneDTO listStatoContribuzione;
			try {
				listStatoContribuzione = statoContribuzioneDAO.loadStatoContribuzioneById(idStatoContribuzione);
			} catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
	        LOGGER.debug("[StatoContribuzioneApiServiceImpl::loadStatoContribuzioneById] END");
	        return Response.ok(listStatoContribuzione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
		}
}
