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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.TipiElaborazioneApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipiElaborazioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipoElaboraExtendedDTO;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type Elaborazione Api Service Impl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiElaborazioneApiServiceImpl extends BaseApiServiceImpl  implements TipiElaborazioneApi {
	
	private static final String IDENTITY = "identity";
    private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
    
	@Autowired
    private TipiElaborazioneDAO tipiElaborazioneDAO;

	@Override
	public Response loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile(Long idAmbito, Long idFunzionalita,
			Integer flgVisible, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<TipoElaboraExtendedDTO> listTipiElaborazione= null;
        try {
			  LOGGER.debug("[TipiElaborazioneApiServiceImpl::loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile] BEGIN");
			  listTipiElaborazione  = tipiElaborazioneDAO.loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile(idAmbito, idFunzionalita, flgVisible);
		  
		} catch (BusinessException  e) {
			return handleBusinessException(400, e);
		} catch (Exception e) {
            LOGGER.error("[TipiElaborazioneApiServiceImpl::loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile:: ERROR ]: "+e);
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
		}
            LOGGER.debug("[TipiElaborazioneApiServiceImpl::loadTipiElaborazioneByIdAmbitoAndIdFunzionalitaAndFlgVisibile]  END");
        return Response.ok(listTipiElaborazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
