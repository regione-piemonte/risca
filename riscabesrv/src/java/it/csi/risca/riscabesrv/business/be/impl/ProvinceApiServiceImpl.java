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

import it.csi.risca.riscabesrv.business.be.ProvinceApi;
import it.csi.risca.riscabesrv.business.be.impl.dao.ProvinceDAO;
import it.csi.risca.riscabesrv.dto.ProvinceExtendedDTO;

@Component
public class ProvinceApiServiceImpl implements ProvinceApi{
	
	private static final String IDENTITY = "identity";

	@Autowired
    private ProvinceDAO provinceDAO;
	
	@Override
	public Response loadProvince(boolean attivo ,SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		List<ProvinceExtendedDTO> listProvince = null;
		try {
			listProvince = provinceDAO.loadProvince(attivo);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		return Response.ok(listProvince).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadProvinceByCodIstatComune(String codIstatComune, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<ProvinceExtendedDTO> listProvince = null;
		try {
			listProvince = provinceDAO.loadProvinceByCodIstatComune(codIstatComune);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		return Response.ok(listProvince).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadProvinceByCodRegione(String codRegione, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		List<ProvinceExtendedDTO> listProvince = null;
		try {
			listProvince = provinceDAO.loadProvinceByCodRegione(codRegione);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		return Response.ok(listProvince).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia(String codRegione, String codProvincia,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    ProvinceExtendedDTO listProvince = null;
		try {
			listProvince = provinceDAO.loadProvinciaByIdOrCodRegioneAndIdOrCodProvincia(codRegione, codProvincia);
		} catch (Exception e) {
			return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR).status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).build();

		}
		return Response.ok(listProvince).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
