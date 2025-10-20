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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscabesrv.business.be.NazioniApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.NazioniDAO;
import it.csi.risca.riscabesrv.dto.NazioniDTO;

@Component
public class NazioniApiServiceImpl implements NazioniApi{
	
	private static final String IDENTITY = "identity";

	@Autowired
    private NazioniDAO nazioniDAO;
	
	@Override
	public Response loadNazioni(boolean attivo, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<NazioniDTO> listNazioni = nazioniDAO.loadNazioni(attivo);
		return Response.ok(listNazioni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}



}
