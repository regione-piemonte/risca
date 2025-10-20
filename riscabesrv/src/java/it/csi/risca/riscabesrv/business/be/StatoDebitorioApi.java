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

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

import it.csi.risca.riscabesrv.business.be.exception.GenericExceptionList;
import it.csi.risca.riscabesrv.dto.RicercaPagamentiDaVisionareDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;

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
	Response loadAllStatoDebitorioOrByIdRiscossione(
			@QueryParam("idRiscossione") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idRiscossione,
			@QueryParam("fruitore") String fruitore,
			@QueryParam(value = "offset") @Min(value = 0, message = "offset deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer") Integer offset,
			@QueryParam(value = "limit") @Min(value = 0, message = "limit deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer") Integer limit,
			@DefaultValue("") @QueryParam(value = "sort") String sort, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/stati-debitori/{idStatoDebitorio}")
	Response loadStatoDebitorioByIdStatoDebitorio(
			@PathParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idStatoDebitorio,
			@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/stati-debitori")
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveStatoDebitorio(@RequestBody StatoDebitorioExtendedDTO statoDebitorio,
			@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
					throws GenericExceptionList, ParseException;

	@PUT
	@Path("/stati-debitori")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateStatoDebitorio(@RequestBody StatoDebitorioExtendedDTO statoDebitorio,
			@QueryParam("fruitore") String fruitore,
			@QueryParam("flgUpDataScadenza") String flgUpDataScadenza, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
					throws GenericExceptionList, ParseException;

	@POST
	@Path("/_verify_stato_debitorio")
	@Consumes(MediaType.APPLICATION_JSON)
	Response verifyStatoDebitorio(@QueryParam("modalita") String modalita,
			@RequestBody StatoDebitorioExtendedDTO statoDebitorio, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest)
					throws GenericExceptionList, ParseException;

	@GET
	@Path("/stati-debitori/idRimborso/{idRimborso}")
	Response loadStatoDebitorioByIdRimborso(
			@PathParam("idRimborso") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idRimborso,
			@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/stati-debitori/nap/{nap}")
	Response loadStatoDebitorioByNap(@PathParam("nap") String nap, 
			@QueryParam("fruitore") String fruitore,
			@RequestBody List<Integer> sdDaEscludere,
			@QueryParam(value = "offset") @Min(value = 0, message = "offset deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer") Integer offset,
			@QueryParam(value = "limit") @Min(value = 0, message = "limit deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer") Integer limit,
			@QueryParam(value = "sort") String sort, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/_verify_stato_debitorio_invio_speciale/idRiscossione/{idRiscossione}")
	Response verifyStatoDebitorioInvioSpeciale(
			@PathParam("idRiscossione") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idRiscossione,
			@QueryParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idStatoDebitorio,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/stati-debitori/codUtenza/{codUtenza}")
	Response loadStatoDebitorioByCodUtenza(@PathParam("codUtenza") String codUtenza,
			@QueryParam("fruitore") String fruitore,
			@RequestBody List<Integer> sdDaEscludere,
		    @QueryParam(value = "offset") @Min(value = 0, message = "offset deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer") Integer offset,
			@QueryParam(value = "limit") @Min(value = 0, message = "limit deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer") Integer limit,
			@QueryParam(value = "sort") String sort, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/stati_debitori/attivita")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateAttivitaStatoDebitorio(@RequestBody List<StatoDebitorioExtendedDTO> statoDebitorio,
			@QueryParam("fruitore") String fruitore, @QueryParam("idAttivitaSD") String idAttivitaSD,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/_stati_debitori_upd_wrk")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateStatoDebitorioFromStatoDebitorioUpd(@QueryParam("attore") String attore,
			@QueryParam("idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idElabora,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@PUT
	@Path("/stato-debitoro/spese-notifica")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateSpeseNotificaStatoDebitorio(
			@QueryParam("idStatoDebitorio") @Min(value = 1, message = "L'id deve essere maggiore di 0") @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idStatoDebitorio,
			@QueryParam("impSpeseNotifica") BigDecimal impSpeseNotifica, @QueryParam("attore") String attore,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest) throws GenericExceptionList, ParseException;


	@GET
	@Path("/stati-debitori-simple")
	Response loadAllStatoDebitorio(@QueryParam("fruitore") String fruitore,  @QueryParam(value = "offset") Integer offset,  @QueryParam(value = "limit") Integer limit, @DefaultValue("") @QueryParam(value = "sort") String sort, @QueryParam(value = "isNotturnoTurnOn") Boolean isNotturnoTurnOn, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) ;


	@PUT
	@Path("/stati-debitori/stato-contribuzione")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateStatoContribuzione(@RequestBody List<StatoDebitorioExtendedDTO> statoDebitorio, @QueryParam("fruitore") String fruitore,@QueryParam("idElabora") Long idElabora,  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws GenericExceptionList, ParseException;

		
	@GET
	@Path("/stati-debitori/nap/{nap}/canone-dovuto")
	Response sommaAllCanoneDovutoByNap(@PathParam("nap") String nap,  @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@POST
	@Path("/stati-debitori/pagamenti-da-visionare")
	Response loadStatiDebitoriPerPagamentiDaVisionare(@RequestBody RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO,
			@QueryParam("fruitore") String fruitore,
			@QueryParam(value = "offset") Integer offset,
			@QueryParam(value = "limit") Integer limit,
		    @QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
}
