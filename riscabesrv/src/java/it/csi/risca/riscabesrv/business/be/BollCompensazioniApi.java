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
import javax.ws.rs.DefaultValue;
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

import it.csi.risca.riscabesrv.dto.RiscossioneBollData;

@Path("/_boll-compensazioni")
@Produces(MediaType.APPLICATION_JSON)
public interface BollCompensazioniApi {
	
	@POST
	@Path("/init")
	@Consumes(MediaType.APPLICATION_JSON)
	Response initCompensazioni(
			@QueryParam(value = "idElabora")@Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam(value = "idAmbito")@Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idAmbito,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@POST
	@Path("/elabora-bs")
	@Consumes(MediaType.APPLICATION_JSON)
	Response elaboraCompensazioniBollSpeciale(@RequestBody RiscossioneBollData riscossioneBsData,
			@QueryParam(value = "giro") @DefaultValue("1") int giro,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@POST
	@Path("/elabora-bo-sd-comp-sogg-pratica")
	@Consumes(MediaType.APPLICATION_JSON)
	Response elaboraCompensazioniSoggettoPraticaBollOrdinaria(@RequestBody RiscossioneBollData riscossioneBsData,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@POST
	@Path("/elabora-bo-soggetto")
	@Consumes(MediaType.APPLICATION_JSON)
	Response elaboraCompensazioniSoggettoBollOrdinaria(@RequestBody RiscossioneBollData riscossioneBsData,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

}
