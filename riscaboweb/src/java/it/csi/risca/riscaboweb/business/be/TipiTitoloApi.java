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
 * The interface Tipi titolo api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiTitoloApi {

	 /**
     * get tipi titolo response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @Path("/tipi-titolo")
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    Response getTipiTitolo( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * get tipi titolo tramite idAmbito.
     *
     * @param idAmbito    idAmbito
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-titolo")
    Response getTipiTitoloByIdAmbito(@PathParam("idAmbito") String idAmbito,  @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * get tipo titolo by idTipoTitolo and idAmbito.
     *
     * @param idTipoTitolo idTipoTitolo
     * @param idAmbito    idAmbito
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-titolo/{idTipoTitolo}/idAmbito/{idAmbito}")
    Response getTipoTitoloByIdTipoTitoloAndIdAmbito(@PathParam("idTipoTitolo") String idTipoTitolo, @PathParam("idAmbito")  String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * get tipo titolo by codOrIdTipoTitolo and idAmbito.
     *
     * @param codTipoTitolo codTipoTitolo
     * @param idAmbito    idAmbito
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-titolo/{codTipoTitolo}")
    Response getTipoTitoloByCodeAndIdAmbito(@PathParam("codTipoTitolo") String codOrIdTipoTitolo, @PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
