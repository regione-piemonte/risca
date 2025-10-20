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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.RicalcoloLottoDTO;

@Path("/ricalcolo-iuv")
@Produces(MediaType.APPLICATION_JSON)
public interface RicalcoloIUVApi {

	@GET
	@Path("/init-ricalcolo")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response initRicalcolo(
			@QueryParam(value = "idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idElabora,
			@QueryParam(value = "attore") String attore);

	@GET
	@Path("/crea-lotti")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response creaLotti(
			@QueryParam(value = "idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idElabora,
			@QueryParam(value = "idAmbito") Long idAmbito, @QueryParam(value = "attore") String attore);

	@POST
	@Path("/invia-lotto")
	@Consumes(MediaType.APPLICATION_JSON)
	Response inviaLotto(@RequestBody RicalcoloLottoDTO ricalcoloLotto, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/posizioni-debitorie")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loadPosizioniDebitorieDaInviare(
			@QueryParam(value = "idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idElabora);

}
