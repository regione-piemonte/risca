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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * The interface Tipi Soggetto api.
 *
 * @author CSI PIEMONTE
 */
@Path("/tipi-soggetto")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiSoggettoApi {

	 /**
     * Load tipi soggetto response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    Response loadTipiSoggetto( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipi soggetto tramite idTipoSoggetto or codTipoSoggetto.
     *
     * @param idTipoSoggetto    idTipoSoggetto
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/{idTipoSoggetto}")
    Response loadTipiSoggettoByIdOrCodTipoSoggetto(@PathParam("idTipoSoggetto") String idTipoSoggetto,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);



}
