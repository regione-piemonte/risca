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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.SpedizioneActaDTO;
import it.csi.risca.riscabesrv.dto.SpedizioneDTO;

/**
 * The interface Elabora api.
 *
 * @author CSI PIEMONTE
 */
@Path("/spedizione")
@Produces(MediaType.APPLICATION_JSON)
public interface SpedizioneApi {

	/**
	 * Load parametri elaborazione by idSpedizione
	 *
	 * @param idSpedizione    idSpedizione
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response response
	 */
	@GET
	@Path("/{idSpedizione}")
	Response loadSpedizioneByIdSpedizione(@PathParam("idSpedizione") String idSpedizione,
			@QueryParam("isWorking") boolean isWorking, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	Response loadSpedizioneByElaborazione(@QueryParam("idAmbito") String idAmbito,
			@QueryParam("idElabora") String idElabora, @QueryParam("codTipoSpedizione") String codTipoSpedizione,
			@QueryParam("isWorking") boolean isWorking, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveSpedizione(@RequestBody SpedizioneDTO spedizione,@QueryParam("fruitore") String fruitore,  @QueryParam("isWorking") boolean isWorking,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateSpedizione(@RequestBody SpedizioneDTO spedizione,@QueryParam("fruitore") String fruitore, @QueryParam("isWorking") boolean isWorking,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/_copy_from_wrk")
	@Consumes(MediaType.APPLICATION_JSON)
	Response copySpedizioneFromWorking(@QueryParam("idSpedizione")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idSpedizione,
			@QueryParam("attore") String attore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/acta")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveSpedizioneActa(@RequestBody SpedizioneActaDTO dto, @QueryParam("fruitore") String fruitore,@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}
