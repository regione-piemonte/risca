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

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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

import it.csi.risca.riscaboweb.business.be.exception.GenericExceptionList;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AttivitaStatoDebitorioDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RicercaPagamentiDaVisionareDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StatoDebitorioExtendedDTO;

/**
 * The interface Stato Debitorio api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface StatoDebitorioApi {

	@GET
	@Path("/stati-debitori")
	Response loadAllStatoDebitorioOrByIdRiscossione(@QueryParam("idRiscossione") String idRiscossione,
			@QueryParam("fruitore") String fruitore, @DefaultValue("1") @QueryParam(value = "offset") String offset,
			@DefaultValue("20") @QueryParam(value = "limit") String limit,
			@DefaultValue("") @QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/stati-debitori/{idStatoDebitorio}")
	Response loadStatoDebitorioByIdStatoDebitorio(@PathParam("idStatoDebitorio") String idStatoDebitorio,
			@QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/stati-debitori")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveStatoDebitorio(@RequestBody StatoDebitorioExtendedDTO statoDebitorio,
			@QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws GenericExceptionList;

	@PUT
	@Path("/stati-debitori")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateStatoDebitorio(@RequestBody StatoDebitorioExtendedDTO statoDebitorio,
			@QueryParam("fruitore") String fruitore,
			@QueryParam("flgUpDataScadenza") String flgUpDataScadenza, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws GenericExceptionList;

	@POST
	@Path("/_verify_stato_debitorio")
	@Consumes(MediaType.APPLICATION_JSON)
	Response verifyStatoDebitorio(@QueryParam("modalita") String modalita,
			@RequestBody StatoDebitorioExtendedDTO statoDebitorio, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws GenericExceptionList, ParseException;

	@GET
	@Path("/stati-debitori/idRimborso/{idRimborso}")
	Response loadStatoDebitorioByIdRimborso(@PathParam("idRimborso") String idRimborso,
			@QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/_verify_stato_debitorio_invio_speciale/idRiscossione/{idRiscossione}")
	Response verifyStatoDebitorioInvioSpeciale(@PathParam("idRiscossione") String idRiscossione,
			@QueryParam("idStatoDebitorio") String idStatoDebitorio, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/stati-debitori/nap/{nap}")
	Response loadStatoDebitorioByNap(@PathParam("nap") String nap,
			@QueryParam("fruitore") String fruitore,
			@RequestBody List<Integer> sdDaEscludere,
			@QueryParam(value = "offset") String offset,
			@QueryParam(value = "limit") String limit,
		    @QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/stati-debitori/codUtenza/{codUtenza}")
	Response loadStatoDebitorioByCodUtenza(@PathParam("codUtenza") String codUtenza,
			@QueryParam("fruitore") String fruitore,
			@RequestBody List<Integer> sdDaEscludere,
			@QueryParam(value = "offset") String offset,
			@QueryParam(value = "limit") String limit,
		    @QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@PUT
	@Path("/stati_debitori/attivita")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateAttivitaStatoDebitorio(@RequestBody List<StatoDebitorioExtendedDTO> statoDebitorio,
			@QueryParam("fruitore") String fruitore, @QueryParam("idAttivitaSD") String idAttivitaSD,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/stati_debitori/attivita-morosita-list")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateAttivitaForAllSDByFilter(@RequestBody AttivitaStatoDebitorioDTO attivitaStatoDeb,
			@QueryParam("tipoRicercaMorosita") String tipoRicercaMorosita, @QueryParam("anno") String anno,
			@QueryParam("flgRest") String flgRest, @QueryParam("flgAnn") String flgAnn, @QueryParam("lim") String lim,
			@QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT
	@Path("/stati_debitori/attivita-rimborsi-list")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateAttivitaForAllSDByFilterRimborsi(@RequestBody AttivitaStatoDebitorioDTO attivitaStatoDeb,
			@QueryParam("tipoRicercaRimborsi") String tipoRicercaRimborsi, @QueryParam("anno") String anno,
			@QueryParam("fruitore") String fruitore, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/stati-debitori/nap/{nap}/canone-dovuto")
	Response sommaAllCanoneDovutoByNap(@PathParam("nap") String nap,  @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
	@POST
	@Path("/stati-debitori/pagamenti-da-visionare")
	Response loadStatiDebitoriPerPagamentiDaVisionare(@RequestBody RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO,
			@QueryParam("fruitore") String fruitore,
			@QueryParam(value = "offset") String offset,
			@QueryParam(value = "limit") String limit,
		    @QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
