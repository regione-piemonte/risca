/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The interface TipoRicercaMorositaApi.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipoRicercaMorositaApi {

	/**
	 * load All Tipo Ricerca Morosita.
	 * 
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-ricerca-morosita")
	Response loadAllTipoRicercaMorosita(@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

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
			@QueryParam("tipoRicercaMorosita") String tipoRicercaMorosita, @QueryParam("anno") String anno,
			@QueryParam("flgRest") String flgRest, @QueryParam("flgAnn") String flgAnn, @QueryParam("lim") String lim,
			@QueryParam(value = "offset")  String offset,
			@QueryParam(value = "limit")  String limit,
			@QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}
