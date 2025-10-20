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

import it.csi.risca.riscabesrv.business.be.AvvisoDatiTitolareApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.AvvisoDatiTitolareDAO;
import it.csi.risca.riscabesrv.dto.AvvisoDatiTitolareDTO;

@Component
public class AvvisoDatiTitolareApiServiceImpl implements AvvisoDatiTitolareApi {

	private static final String IDENTITY = "identity";

	@Autowired
	private AvvisoDatiTitolareDAO avvisoDatiTitolareDAO;

	@Override
	public Response loadAvvisoDatiTitolareWorkingBySpedizione(Long idSpedizione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<AvvisoDatiTitolareDTO> avvisiTitolari = avvisoDatiTitolareDAO
				.loadAvvisoDatiTitolareWorkingBySpedizione(idSpedizione);
		return Response.ok(avvisiTitolari).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
