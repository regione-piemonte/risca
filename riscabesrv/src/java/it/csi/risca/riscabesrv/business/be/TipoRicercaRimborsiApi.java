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
 * The interface TipoRicercaRimborsiApi.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipoRicercaRimborsiApi {

	/**
	 * load All Tipo Ricerca Rimborsi.
	 * 
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-ricerca-rimborsi")
	Response loadAllTipoRicercaRimborsi(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * ricercaRimborsi.
	 * 
	 * @param fruitore            fruitore
	 * @param tipoRicercaRimborsi tipoRicercaRimborsi
	 * @param anno                anno
	 * @param securityContext     SecurityContext
	 * @param httpHeaders         HttpHeaders
	 * @param httpRequest         HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ricerca_rimborsi")
	Response ricercaRimborsi(@QueryParam("fruitore") String fruitore,
			@QueryParam("tipoRicercaRimborsi") String tipoRicercaRimborsi, @QueryParam("anno") Integer anno,
			@QueryParam(value = "offset") @Min(value = 0, message = "offset deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer") Integer offset,
            @QueryParam(value = "limit")  @Min(value = 0, message = "limit deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer")  Integer limit,
			@QueryParam(value = "sort") String sort, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
