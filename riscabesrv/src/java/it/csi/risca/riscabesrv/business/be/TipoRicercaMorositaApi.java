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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * The interface TipoRicercaMorositaApi.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipoRicercaMorositaApi {

	/**
	 * load All Tipo Ricerca Morisita.
	 * 
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-ricerca-morosita")
	Response loadAllTipoRicercaMorosita(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * ricerca Morosita.
	 * 
	 * @param fruitore            fruitore
	 * @param tipoRicercaMorosita tipoRicercaMorosita
	 * @param anno                anno
	 * @param flgRest             flgRest
	 * @param flgAnn              flgAnn
	 * @param lim                 lim
	 * @param securityContext     SecurityContext
	 * @param httpHeaders         HttpHeaders
	 * @param httpRequest         HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ricerca_morosita")
	Response ricercaMorosita(@QueryParam("fruitore") String fruitore,
			@QueryParam("tipoRicercaMorosita") String tipoRicercaMorosita,
			@QueryParam("anno")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Integer anno,
			@QueryParam("flgRest") @Min(value = 0, message = "flgRest deve essere 0 o 1") @Max(value = 1, message = "flgRest deve essere 0 o 1")  Integer flgRest,
			@QueryParam("flgAnn") @Min(value = 0, message = "flgAnn deve essere 0 o 1") @Max(value = 1, message = "flgAnn deve essere 0 o 1")  Integer flgAnn,
			@QueryParam("lim") String lim,
			@QueryParam(value = "offset")  @Min(value = 0, message = "offset deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer")   Integer offset,
			@QueryParam(value = "limit")  @Min(value = 0, message = "limit deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer")   Integer limit,
			@QueryParam(value = "sort") String sort,
			@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
