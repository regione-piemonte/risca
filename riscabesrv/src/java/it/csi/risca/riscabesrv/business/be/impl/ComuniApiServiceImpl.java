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

import it.csi.risca.riscabesrv.business.be.ComuniApi;
import it.csi.risca.riscabesrv.business.be.helper.scriva.dto.ComuneExtendedDTO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ComuniDAO;
import it.csi.risca.riscabesrv.dto.ComuneDTO;

@Component
public class ComuniApiServiceImpl implements ComuniApi{
	
	private static final String IDENTITY = "identity";

	@Autowired
    private ComuniDAO comuniDAO;
	
	@Override
	public Response loadComuni(boolean attivo, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<ComuneExtendedDTO> listComuni = comuniDAO.loadComuni(attivo);
		return Response.ok(listComuni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadComuniByIdOrCod(String idRegione, String idProvincia, String codIstatComune,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<ComuneDTO> listComuni = comuniDAO.loadComuniByIdOrCod(idRegione, idProvincia, codIstatComune);
		return Response.ok(listComuni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadComuniByCodRegione(String codRegione, String codProvincia, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<ComuneDTO> listComuni = comuniDAO.loadComuniByCodRegione(codRegione, codProvincia);
		return Response.ok(listComuni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadComuniByRicerca(String q, boolean attivo, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<ComuneExtendedDTO> listComuni = comuniDAO.loadComuniByRicerca(q, attivo);
		return Response.ok(listComuni).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadComuneByCodIstatComune(String codIstatComune, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		ComuneDTO comune = comuniDAO.loadComuneByCodIstatComune(codIstatComune);
		return Response.ok(comune).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
