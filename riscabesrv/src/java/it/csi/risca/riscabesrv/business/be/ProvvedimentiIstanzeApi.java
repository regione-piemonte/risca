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

import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;


/**
 * The interface Provvedimenti Istanze Api .
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface ProvvedimentiIstanzeApi {
	
    /**
     * get Provvedimenti Istanze.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response list Provvedimenti Istanze
     */
	@GET
    @Path("/provvedimenti-istanze")
    Response getProvvedimentiIstanze(@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;
    /**
     * get Provvedimenti Istanze by Id Riscossione .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response list Provvedimenti Istanze
     */
    
    @GET
    @Path("/riscossioni/{idRiscossione}/provvedimenti-istanze")
    Response getProvvedimentiIstanzeByidRiscossione(@PathParam("idRiscossione") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idRiscossione, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    /**
     * get Provvedimenti Istanze by Id Provvedimenti Istanze .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response  Provvedimento Istanza
     */
    
    @GET
    @Path("/provvedimenti-istanze/{idProvvedimentiIstanze}")
    Response getProvvedimentoIstanzaByIdProvvedimenti(@PathParam("idProvvedimentiIstanze") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idProvvedimentiIstanze, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    /**
     * save Provvedimenti Istanze .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response  Provvedimento Istanza
     */
    
    @POST
    @Path("/provvedimenti-istanze")
    @Consumes(MediaType.APPLICATION_JSON)
    Response saveProvvedimentiIstanze(@RequestBody ProvvedimentoDTO provvedimentoDTO,@QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
    /**
     * update Provvedimenti Istanze .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response  Provvedimento Istanza
     */
    @PUT
    @Path("/provvedimenti-istanze")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateProvvedimentiIstanze(@RequestBody  ProvvedimentoDTO provvedimentoDTO, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest) throws Exception;
    /**
     * delete Provvedimenti Istanze .
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response  Provvedimento Istanza
     */
    @DELETE
    @Path("/provvedimenti-istanze/{idProvvedimentiIstanze}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response deleteProvvedimentiIstanze(@PathParam("idProvvedimentiIstanze") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idProvvedimentiIstanze, @Context SecurityContext securityContext, @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
 

}
