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

import javax.annotation.security.RolesAllowed;
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

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneSearchDTO;

/**
 * The interface Riscossione api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface RiscossioneApi {

	/**
	 * Save riscossione response.
	 *
	 * @param riscossione     RiscossioneDTO
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response AccreditamentoDTO
	 */
	@POST
	@Path("/riscossioni")
	@RolesAllowed({ "AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI" })
	@Consumes(MediaType.APPLICATION_JSON)
	Response saveRiscossione(@RequestBody RiscossioneDTO riscossione, @QueryParam("fruitore") String fruitore,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@PUT
	@Path("/riscossioni")
	@Consumes(MediaType.APPLICATION_JSON)
	Response updateRiscossione(@RequestBody RiscossioneDTO riscossione, @QueryParam("fruitore") String fruitore,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/riscossioni/{codRiscossione}")
	Response getRiscossione(@PathParam("codRiscossione") String codRiscossione, @QueryParam("fruitore") String fruitore,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/riscossioni")
	Response getRiscossioniGruppo(@QueryParam("idGruppo") String idGruppo, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@POST
	@Path("/_search/riscossioni")
	@Consumes(MediaType.APPLICATION_JSON)
	Response searchRiscossione(@RequestBody RiscossioneSearchDTO RiscossioneSearch,
			@QueryParam("fruitore") String fruitore, @QueryParam("modalitaRicerca") String modalitaRicerca,
			@DefaultValue("1") @QueryParam(value = "offset") String offset,
			@DefaultValue("20") @QueryParam(value = "limit") String limit,
			@DefaultValue("") @QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@GET
	@Path("/riscossioni/{idRiscossione}/dati-tecnici")
	Response loadDatoTecnico(@PathParam("idRiscossione") String idRiscossione, @QueryParam("fruitore") String fruitore,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@GET
	@Path("/_verify_riscossioni_stdebitori/{idOggetto}")
	Response verifyRiscossioniSTDebitori(@PathParam("idOggetto") String idOggetto,
			@QueryParam("indTipoOggetto") String indTipoOggetto, @QueryParam("idTipoOper") String idTipoOper,
			@QueryParam("idRiscossione") String idRiscossione,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	
	@GET
	@Path("/utenze-comp/idStatoDebitorio/{idStatoDebitorio}")
	Response getCodiciUtenzaByIdStatoDebitorio(@PathParam("idStatoDebitorio") String idStatoDebitorio,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}
