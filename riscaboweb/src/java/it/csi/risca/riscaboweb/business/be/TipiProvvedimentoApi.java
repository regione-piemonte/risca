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
 * The interface Tipi provvedimento api.
 *
 * @author CSI PIEMONTE
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiProvvedimentoApi {

	 /**
     * Load tipi provvedimento response.
     *
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-provvedimentoistanza")
    Response getTipiProvvedimento( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipi provvedimento tramite idAmbito e flgIstanza.
     *
     * @param idAmbito    idAmbito
     * @param flgIstanza  flgIstanza
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-provvedimentoistanza")
    Response getTipiProvvedimentoByIdAmbitoAndFlgIstanza(@PathParam("idAmbito") String idAmbito, @QueryParam("flgIstanza") String flgIstanza,  @QueryParam("dataIniVal") String dataIniVal, @QueryParam("dataFineVal") String dataFineVal,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo provvedimento by idTipoProvvedimento and idAmbito.
     *
     * @param idTipoProvvedimento idTipoProvvedimento
     * @param idAmbito    idAmbito
     * @param securityContext   SecurityContext
     * @param httpHeaders       HttpHeaders
     * @param httpRequest       HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/tipi-provvedimentoistanza/{idTipoProvvedimento}/idAmbito/{idAmbito}")
    Response getTipoProvvedimentoByIdTipoProvvedimentoAndIdAmbito(@PathParam("idTipoProvvedimento") String idTipoProvvedimento, @PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    /**
     * Load tipo provvedimento by codTipoProvvedimento and idAmbito.
     *
     * @param codTipoProvvedimento codTipoProvvedimento
     * @param idAmbito    idAmbito
     * @param securityContext    SecurityContext
     * @param httpHeaders        HttpHeaders
     * @param httpRequest        HttpServletRequest
     * @return Response response
     */
    @GET
    @RolesAllowed({"AMMINISTRATORE", "GESTORE_BASE", "GESTORE_DATI", "CONSULTATORE"})
    @Path("/ambiti/{idAmbito}/tipi-provvedimentoistanza/{codOrIdTipoProvvedimento}")
    Response getTipoProvvedimentoByCodeAndIdAmbito(@PathParam("codOrIdTipoProvvedimento") String codOrIdTipoProvvedimento, @PathParam("idAmbito") String idAmbito,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
