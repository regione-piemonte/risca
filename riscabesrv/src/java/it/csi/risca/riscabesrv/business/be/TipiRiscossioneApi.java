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
 * The interface Tipi riscossione api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiRiscossioneApi {

	/**
	 * Load tipi riscossione response.
	 *
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-riscossione")
	Response loadTipiRiscossione(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipi riscossione tramite idAmbito and data inizio validita or data fine
	 * validita.
	 *
	 * @param idAmbito        idAmbito
	 * @param dataIniVal      dataIniVal
	 * @param dataFineVal     dataFineVal
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ambiti/{idAmbito}/tipi-riscossione")
	Response getTipiRiscossioneByIdAmbitoAndDateValidita(@PathParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Long idAmbito,
			@QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipo riscossione by idTipoRiscossione or cod TipoRiscossione and
	 * idAmbito.
	 *
	 * @param idTipoRiscossione idTipoRiscossione
	 * @param idAmbito          idAmbito
	 * @param securityContext   SecurityContext
	 * @param httpHeaders       HttpHeaders
	 * @param httpRequest       HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ambiti/{idAmbito}/tipi-riscossione/{idTipoRiscossione}")
	Response loadTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(
			@PathParam("idTipoRiscossione") String idOrCodTipoRiscossione, @PathParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Long idAmbito,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipo riscossione by codTipoRiscossione and idAmbito.
	 *
	 * @param codTipoRiscossione codTipoRiscossione
	 * @param idAmbito           idAmbito
	 * @param securityContext    SecurityContext
	 * @param httpHeaders        HttpHeaders
	 * @param httpRequest        HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/codice/{codTipoRiscossione}/id-ambito/{idAmbito}")
	Response loadTipoRiscossioneByCodeAndIdAmbito(@PathParam("codTipoRiscossione") String codTipoRiscossione,
			@PathParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")   Long idAmbito, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
