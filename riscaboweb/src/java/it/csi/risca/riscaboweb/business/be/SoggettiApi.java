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

import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.SoggettiExtendedDTO;

/**
 * The interface Soggetto api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface SoggettiApi {
	
    /**
     * Save soggetto response.
     *
     * @param soggetto  SoggettiDTO
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response AccreditamentoDTO
     */
    @GET
    @Path("/soggetti")
    Response loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(@QueryParam("idAmbito")  String idAmbito, @QueryParam("idTipoSoggetto") String idTipoSoggetto, @QueryParam("cfSoggetto") String cfSoggetto,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @GET
    @Path("/soggetti/{idSoggetto}")
    Response loadSoggettoById(@PathParam("idSoggetto") String idSoggetto,@QueryParam("fruitore") String fruitore,   @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @POST
    @Path("/soggetti")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveSoggetto(@RequestBody SoggettiExtendedDTO soggetto,@QueryParam("fruitore") String fruitore,   @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @PUT
    @Path("/soggetti")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateSoggetto(@RequestBody SoggettiExtendedDTO soggetto, @QueryParam("fruitore") String fruitore, @QueryParam("indModManuale") String indModManuale,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @DELETE
    @Path("/soggetti/{idSoggetto}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response deleteSoggetto(@PathParam("idSoggetto") String idSoggetto,@QueryParam("fruitore") String fruitore,  @QueryParam("idRecapito") String idRecapito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @GET
    @Path("/soggetti-qry")
    Response loadSoggettiByCampoRicerca(@QueryParam("idAmbito" ) String idAmbito, @QueryParam("campoRicerca") String campoRicerca,   @DefaultValue("1") @QueryParam(value = "offset") String offset, @DefaultValue("20") @QueryParam(value = "limit") String limit, @DefaultValue("") @QueryParam(value = "sort") String sort,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    
}
