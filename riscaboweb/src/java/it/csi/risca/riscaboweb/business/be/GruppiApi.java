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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.GruppiDTO;

/**
 * The interface Gruppi api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface GruppiApi {
	
    /**
     * Save gruppi response.
     *
     * @param gruppi  GruppiDTO
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response AccreditamentoDTO
     */
    @GET
    @Path("/gruppi")
    Response loadGruppiSoggetto(@QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @GET
    @Path("/gruppi/{idGruppo}")
    Response loadGruppiById(@QueryParam("fruitore") String fruitore, @PathParam("idGruppo") String codGruppo, @QueryParam("desGruppo") String desGruppo,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @POST
    @Path("/gruppi")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveGruppi(@RequestBody GruppiDTO gruppi,@QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @PUT
    @Path("/gruppi")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateGruppi(@RequestBody GruppiDTO gruppi,@QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @DELETE
    @Path("/gruppi/{idGruppo}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response deleteGruppi(@QueryParam("fruitore") String fruitore, @PathParam("idGruppo") String idGruppo,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/gruppi-qry")
    Response loadGruppiByIdAmbitoAndCampoRicerca(@QueryParam("fruitore") String fruitore, @QueryParam("idAmbito") String idAmbito, @QueryParam("campoRicerca") String campoRicerca, @QueryParam("flgTipoRicerca") String flgTipoRicerca,  @DefaultValue("1") @QueryParam(value = "offset") String offset, @DefaultValue("20") @QueryParam(value = "limit") String limit, @DefaultValue("") @QueryParam(value = "sort") String sort, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
}
