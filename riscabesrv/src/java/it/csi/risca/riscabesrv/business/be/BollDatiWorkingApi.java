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
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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

import it.csi.risca.riscabesrv.dto.RiscossioneBollData;

@Path("/_boll-working")
@Produces(MediaType.APPLICATION_JSON)
public interface BollDatiWorkingApi {

	@POST
	@Path("/speciale")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveBsDatiWorking(@RequestBody RiscossioneBollData riscossioneBollData,
			@DefaultValue("true") @QueryParam(value = "insertAvvisoPag") Boolean insertAvvisoPag,
			@DefaultValue("true") @QueryParam(value = "insertTitolare") Boolean insertTitolare,
			@QueryParam(value = "progrPerTitolare") int progrPerTitolare, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/ordinaria/avviso-pag-titolare")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveBoDatiWorkingAvvisoPagTitolare(@RequestBody RiscossioneBollData riscossioneBollData,
			@QueryParam(value = "progrPerTitolare") int progrPerTitolare, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/ordinaria/rata-annualita-sd-dati-ammin")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveBoDatiWorkingRataAnnualitaSdDatiAmmin(@RequestBody RiscossioneBollData riscossioneBollData,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/ordinaria/usi-rid-aum")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveBoDatiWorkingUsiRidAum(@RequestBody RiscossioneBollData riscossioneBollData,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/ordinaria/update-avviso-pag-titolare")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateBoDatiWorkingAvvisoPagTitolare(@QueryParam(value = "idElabora") Long idElabora,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/deleteWrkForNap")
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteWorkingForNap(@QueryParam(value = "nap") String nap,
			@QueryParam(value = "idElabora") Long idElabora, @QueryParam(value = "idSpedizione") Long idSpedizione,
			@QueryParam(value = "codFase") String codFase, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/deleteWrk")
	@Consumes(MediaType.APPLICATION_JSON)
	Response deleteWorking(@QueryParam(value = "idElabora") Long idElabora,
			@QueryParam(value = "idSpedizione") Long idSpedizione, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
