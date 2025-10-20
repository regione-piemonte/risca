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
 * The interface Tipi autorizzazione api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiAutorizzazioneApi {

	/**
	 * Load tipi autorizzazione response.
	 *
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/tipi-autorizzazione")
	Response loadTipiAutorizzazione(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipi autorizzazione tramite idAmbito.
	 *
	 * @param idAmbito        idAmbito
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ambiti/{idAmbito}/tipi-autorizzazione")
	Response loadTipiAutorizzazioneByIdAmbito(@PathParam("idAmbito") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idAmbito,
			@QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipo autorizzazione by idTipoAutorizzazione and idAmbito.
	 *
	 * @param idTipoAutorizzazione idTipoAutorizzazione
	 * @param idAmbito             idAmbito
	 * @param securityContext      SecurityContext
	 * @param httpHeaders          HttpHeaders
	 * @param httpRequest          HttpServletRequest
	 * @return Response response
	 */
	// Da cancellare
	@GET
	@Path("/tipi-autorizzazione/{idTipoAutorizza}/id-ambito/{idAmbito}")
	Response loadTipoAutorizzazioneByIdTipoAutorizzazioneAndIdAmbito(
			@PathParam("idTipoAutorizza") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idTipoAutorizzazione,
			@PathParam("idAmbito") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idAmbito,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	/**
	 * Load tipo autorizzazione by codTipoAutorizzazione or idTipoAutorizzazione and
	 * idAmbito.
	 *
	 * @param codOrIdTipoAutorizzazione codOrIdTipoAutorizzazione
	 * @param idAmbito                  idAmbito
	 * @param securityContext           SecurityContext
	 * @param httpHeaders               HttpHeaders
	 * @param httpRequest               HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/ambiti/{idAmbito}/tipi-autorizzazione/{codTipoAutorizza}")
	Response loadTipoAutorizzazioneByCodeAndIdAmbito(@PathParam("codTipoAutorizza") String codOrIdTipoAutorizzazione,
			@PathParam("idAmbito") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idAmbito, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
