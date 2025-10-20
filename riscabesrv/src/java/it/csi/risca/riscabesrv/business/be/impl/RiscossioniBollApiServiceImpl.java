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

import it.csi.risca.riscabesrv.business.be.RiscossioniBollApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioniBollDAO;
import it.csi.risca.riscabesrv.dto.RiscossioneBollDTO;

@Component
public class RiscossioniBollApiServiceImpl implements RiscossioniBollApi {

	private static final String IDENTITY = "identity";

	@Autowired
	private RiscossioniBollDAO riscossioniBollDao;

	@Override
	public Response loadRiscossioniBollettazioneSpeciale(String codAmbito, String dtScadPagamento, String tipoElabora,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<RiscossioneBollDTO> listRiscossioniBs = null;
		try {
			listRiscossioniBs = riscossioniBollDao.loadRiscossioniBollettazioneSpeciale(codAmbito, dtScadPagamento,
					tipoElabora);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(listRiscossioniBs).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadRiscossioniBollettazioneOrdinaria(String codAmbito, String dtScadPagamento, String anno,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<RiscossioneBollDTO> listRiscossioniBs = null;
		try {
			listRiscossioniBs = riscossioniBollDao.loadRiscossioniBollettazioneOrdinaria(codAmbito, dtScadPagamento,
					anno);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR)
					.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();
		}
		return Response.ok(listRiscossioniBs).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
