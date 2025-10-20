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

import it.csi.risca.riscabesrv.dto.SoggettiExtendedDTO;

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
     * @throws Exception 
     */
    @GET
    @Path("/soggetti")
    Response loadSoggettiByIdAmbAndIdTipoSoggAndCfSogg(@QueryParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idAmbito, 
    		@QueryParam("idTipoSoggetto")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idTipoSoggetto, 
    		@QueryParam("cfSoggetto") String cfSoggetto,
    		@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;
	
    @GET
    @Path("/soggetti/{idSoggetto}")
    Response loadSoggettoById(@PathParam("idSoggetto") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long iddSoggetto, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @POST
    @Path("/soggetti")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveSoggetto(@RequestBody SoggettiExtendedDTO soggetto,@QueryParam("fruitore") String fruitore,  @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @PUT
    @Path("/soggetti")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateSoggetto(@RequestBody SoggettiExtendedDTO soggetto,@QueryParam("fruitore") String fruitore,  @QueryParam("indModManuale")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long indModManuale, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @DELETE
    @Path("/soggetti/{idSoggetto}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response deleteSoggetto(@PathParam("idSoggetto") Long idSoggetto, @QueryParam("fruitore") String fruitore, @QueryParam("idRecapito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idRecapito, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
    @GET
    @Path("/soggetti-qry")
    Response loadSoggettiByCampoRicerca(@QueryParam("idAmbito")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer") Long idAmbito, @QueryParam("campoRicerca")  String campoRicerca,
    		@QueryParam(value = "offset")  @Min(value = 0, message = "offset deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "offset deve essere minore o uguale a max value integer")  Integer offset,
              @QueryParam(value = "limit")  @Min(value = 0, message = "limit deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "limit deve essere minore o uguale a max value integer")  Integer limit,
              @DefaultValue("") @QueryParam(value = "sort") String sort,
              @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/soggetti-ext/{idSoggetto}")
    Response loadSoggettoExtByIdSoggetto(@PathParam("idSoggetto")  @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idSoggetto, 
    		@QueryParam("fruitore") String fruitore,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    
}
