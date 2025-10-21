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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The interface Comuni api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface ComuniApi {
	
	 /**
     * Load comuni response.
     *
     * @param attivo          attivo
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/comuni")
    Response loadComuni(@QueryParam("attivo") boolean attivo,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load comuni by id or code (regione, provincia comune).
     *
     * @param idRegione        idRegione
     * @param idProvincia      idProvincia
     * @param codIstatComune    codIstatComune
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/regioni/{idRegione}/province/{idProvincia}/comuni/{codIstatComune}")
    Response loadComuniByIdOrCod(@PathParam("idRegione") String idRegione, @PathParam("idProvincia") String idProvincia, @PathParam("codIstatComune") String codIstatComune,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load comuni by codice regione.
     *
     * @param codRegione codRegione
     * @param codRegione codProvincia
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/regioni/{codRegione}/province/{codProvincia}/comuni")
    Response loadComuniByCodRegione(@PathParam("codRegione") String codRegione, @PathParam("codProvincia") String codProvincia,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load comuni by search string
     *
     * @param q        q
     * @param attivo      attivo
     * @param codIstatComune    codIstatComune
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/comuni-qry")
    Response loadComuniByRicerca(@QueryParam("q") String q, @QueryParam("attivo") boolean attivo,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load comune by codIstatComune.
     *
     * @param codIstatComune codIstatComune
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/comuni/{codIstatComune}")
    Response loadComuneByCodIstatComune(@PathParam("codIstatComune") String codIstatComune,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

}
