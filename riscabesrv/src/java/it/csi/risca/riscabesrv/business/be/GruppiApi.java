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
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.GruppiDTO;

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
    Response loadGruppiSoggetto(@QueryParam("fruitore") String fruitore,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @GET
    @Path("/gruppi/{idGruppo}")
    Response loadGruppiById(@QueryParam("fruitore") String fruitore, @PathParam("idGruppo") String codGruppo, @QueryParam("desGruppo") String desGruppo, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @POST
    @Path("/gruppi")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveGruppi(@RequestBody GruppiDTO gruppi,@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @PUT
    @Path("/gruppi")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateGruppi(@RequestBody GruppiDTO gruppi,@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @DELETE
    @Path("/gruppi/{idGruppo}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response deleteGruppi( @QueryParam("fruitore") String fruitore, @PathParam("idGruppo")@Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idGruppo,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/gruppi-qry")
    Response loadGruppiByIdAmbitoAndCampoRicerca(@QueryParam("fruitore") String fruitore,
    		@QueryParam("idAmbito") Long idAmbito, 
    		@QueryParam("campoRicerca") String campoRicerca, 
    		@QueryParam("flgTipoRicerca") String flgTipoRicerca, 
    		@QueryParam(value = "offset") @Min(value = 0, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Integer offset,
    		@QueryParam(value = "limit") @Min(value = 0, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Integer limit,
    		@DefaultValue("") @QueryParam(value = "sort") String sort,
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
}
