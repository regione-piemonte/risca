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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * The interface Tipi provvedimento api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiProvvedimentoApi {

	/**
	 * Load tipi provvedimento response.
	 *
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-provvedimentoistanza")
	Response loadTipiProvvedimento(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipi provvedimento tramite idAmbito e flgIstanza.
	 *
	 * @param idAmbito        idAmbito
	 * @param flgIstanza      flgIstanza
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ambiti/{idAmbito}/tipi-provvedimentoistanza")
	Response loadTipiProvvedimentoByIdAmbitoAndFlgIstanza(
			@PathParam("idAmbito") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idAmbito,
			@QueryParam("flgIstanza") @Min(value = 0, message = "flgIstanza deve essere 0 o 1") @Max(value = 1, message = "flgIstanza deve essere 0 o 1") int flgIstanza,
			@QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipo provvedimento by idTipoProvvedimento and idAmbito.
	 *
	 * @param idTipoProvvedimento idTipoProvvedimento
	 * @param idAmbito            idAmbito
	 * @param securityContext     SecurityContext
	 * @param httpHeaders         HttpHeaders
	 * @param httpRequest         HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-provvedimentoistanza/{idTipoProvvedimento}/idAmbito/{idAmbito}")
	Response loadTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito(
			@PathParam("idTipoProvvedimento")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Long idTipoProvvedimento,
			@PathParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Long idAmbito,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipo provvedimento by codTipoProvvedimento or idTipoProvvedimento and
	 * idAmbito.
	 *
	 * @param codOrIdTipoProvvedimento codOrIdTipoProvvedimento
	 * @param idAmbito                 idAmbito
	 * @param securityContext          SecurityContext
	 * @param httpHeaders              HttpHeaders
	 * @param httpRequest              HttpServletRequest
	 * @return Response response
	 */

	@GET
	@Path("/ambiti/{idAmbito}/tipi-provvedimentoistanza/{codOrIdTipoProvvedimento}")
	Response loadTipoProvvedimentoByCodeAndIdAmbito(
			@PathParam("codOrIdTipoProvvedimento") String codOrIdTipoProvvedimento,
			@PathParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Long idAmbito, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
