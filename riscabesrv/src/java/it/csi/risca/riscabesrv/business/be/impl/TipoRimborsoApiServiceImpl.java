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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.TipoRimborsoApi;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRimborsoDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.TipoRimborsoDTO;
/**
 * The type Tipo Rimborso api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoRimborsoApiServiceImpl extends BaseApiServiceImpl implements TipoRimborsoApi {

	private static final String IDENTITY = "identity";
	
    @Autowired
    private TipoRimborsoDAO tipoRimborsoDAO;


	@Override
	public Response loadTipiRimborso(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoRimborsoApiServiceImpl::loadTipiRimborso] BEGIN");
	        List<TipoRimborsoDTO> tipiRimborso = new ArrayList<TipoRimborsoDTO>();
			try {
				tipiRimborso = tipoRimborsoDAO.loadTipiRimborso(securityContext, httpHeaders, httpRequest);
		       
			} catch (DAOException e) {
				LOGGER.debug("[TipoRimborsoApiServiceImpl::loadTipiRimborso] END");
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}

	        LOGGER.debug("[TipoRimborsoApiServiceImpl::loadTipiRimborso] END");
	        return Response.ok(tipiRimborso).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
