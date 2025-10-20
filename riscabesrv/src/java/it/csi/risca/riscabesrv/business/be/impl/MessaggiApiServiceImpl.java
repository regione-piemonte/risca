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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.MessaggiApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;

@Component
public class MessaggiApiServiceImpl implements MessaggiApi{
	
	private static final String IDENTITY = "identity";

	@Autowired
    private MessaggiDAO messaggiDAO;
	
	@Override
	public Response loadMessaggi(SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<MessaggiDTO> messaggi = messaggiDAO.loadMessaggi();
		return Response.ok(messaggi).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadMessaggiByCodMessaggio(String codMessaggio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		MessaggiDTO messaggi = null;
		try {
			messaggi = messaggiDAO.loadMessaggiByCodMessaggio(codMessaggio);
		} catch (SQLException e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		return Response.ok(messaggi).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}


}
