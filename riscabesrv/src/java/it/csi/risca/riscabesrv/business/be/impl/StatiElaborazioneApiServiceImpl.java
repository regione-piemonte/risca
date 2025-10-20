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

import it.csi.risca.riscabesrv.business.be.StatiElaborazioneApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.StatiElaborazioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.StatoElaborazioneDTO;
import it.csi.risca.riscabesrv.util.Constants;

@Component
public class StatiElaborazioneApiServiceImpl implements StatiElaborazioneApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
    
	@Autowired
    private StatiElaborazioneDAO statiElaborazioneDAO;


	@Override
	public Response loadStatiElaborazioneByIdAmbitoAndIdFunzionalita(Long idAmbito, Long idFunzionalita, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        LOGGER.debug("[StatiElaborazioneApiServiceImpl::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita] BEGIN");
        
        List<StatoElaborazioneDTO> listStatiElaborazione;
		try {
			listStatiElaborazione = statiElaborazioneDAO.loadStatiElaborazioneByIdAmbitoAndIdFunzionalita(idAmbito, idFunzionalita);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
        LOGGER.debug("[StatiElaborazioneApiServiceImpl::loadStatiElaborazioneByIdAmbitoAndIdFunzionalita]  END");
        return Response.ok(listStatiElaborazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
