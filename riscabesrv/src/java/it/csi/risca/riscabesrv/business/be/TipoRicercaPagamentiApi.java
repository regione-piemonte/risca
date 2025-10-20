/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.RicercaPagamentoDTO;

/**
 * The interface TipoRicercaPagamentiApi.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipoRicercaPagamentiApi {

	/**
	 * load All Tipi Ricerca Pagamenti.
	 * 
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-ricerca-pagamenti")
	Response loadAllTipiRicercaPagamenti(@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	/**
	 * ricercaPagamenti.
	 * 
	 * @param fruitore        fruitore
	 * @param
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@POST
	@Path("/ricerca_pagamenti")
	Response ricercaPagamenti(@RequestBody RicercaPagamentoDTO ricercaPagamentoDTO,
			@QueryParam("fruitore") String fruitore, 
			@QueryParam(value = "offset") @Min(value = 0, message = "offset deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer") Integer offset,
			@QueryParam(value = "limit")  @Min(value = 0, message = "limit deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer")  Integer limit,
			@QueryParam(value = "sort") String sort,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
